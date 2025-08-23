package org.hzai.ai.aimcp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpParamDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.ai.aimcp.repository.AiMcpRepository;
import org.hzai.ai.aimcp.service.strategy.CommandEnvStrategy;
import org.hzai.ai.aimcp.service.strategy.CommandEnvStrategyFactory;
import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolParam;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.hzai.ai.aimcptools.repository.AiMcpToolsRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.ai.common.SelectOption;
import org.hzai.config.FileConfig;
import org.hzai.util.CommonConstants;
import org.hzai.util.FileUtil;
import org.hzai.util.JsonUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.hzai.util.SecurityUtil;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.util.StringUtil;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AiMcpServiceImp implements AiMcpService {
    @Inject
    AiMcpRepository repository;

    @Inject
    SecurityUtil securityUtil;

    @Inject
    FileConfig fileConfig;

    @Inject
    MCPClientRegistry mapClientRegistry;

    @Inject
    CommandEnvStrategyFactory strategyFactory;

    @Inject
    AiMcpToolsRepository aiMcpToolsRepository;

    @Override
    public List<AiMcp> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<AiMcp> listEntitysByDto(AiMcpQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiMcp> listPage(AiMcpQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiMcp entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getMcpCount() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> mcpCountByDay = repository.getMcpCountByDay();

        List<String> lastSevenDays = DateUtil.getLastSevenDays();

        if (!mcpCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : mcpCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }

        return data;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void replaceByDto(AiMcpDto dto) {
        repository.updateById(dto);
    }

    @Override
    public List<Long> getMcpCountBefore() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> mcpCountByDay = repository.getMcpCountByDay();

        List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

        if (!mcpCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : mcpCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }

        return data;
    }

    @Override
    public R<Object> uploadFile(FileUpload file, AiMcp aiMcp) throws Exception {
        R<Object> checkFile = checkFile(file, aiMcp);
        if (checkFile.getCode() != CommonConstants.SUCCESS) {
            return checkFile;
        }
        String mcpFilePath = fileConfig.basePath() + "/mcp" + securityUtil.getUserId();
        FileUtil.saveFile(file, mcpFilePath);
        aiMcp.setMcpFilePath(mcpFilePath + "/" + file.fileName());
        McpClient mcpClient = getMcpClient(aiMcp);

        // 解析mcp工具
		List<ToolSpecification> toolSpecifications = mcpClient.listTools();
        if (aiMcp.getId() != null) {
            // update
			repository.updateById(aiMcp);
            // 1. 查询数据库中已有的工具
            AiMcpToolsQueryDto queryDto = new AiMcpToolsQueryDto();
            queryDto.setMcpId(aiMcp.getId());
            List<AiMcpTools> existingTools = aiMcpToolsRepository.selectList(queryDto);
            Map<String, AiMcpTools> existingToolMap = existingTools.stream()
					.collect(Collectors.toMap(AiMcpTools::getName, Function.identity()));
            List<AiMcpTools> toolsToUpdate = new ArrayList<>();
			List<AiMcpTools> toolsToInsert = new ArrayList<>();
			Set<String> newToolNames = new HashSet<>();
            for (ToolSpecification toolSpecification : toolSpecifications) {
                String toolName = toolSpecification.name();
				newToolNames.add(toolName);

				AiMcpTools tool = existingToolMap.getOrDefault(toolName, new AiMcpTools());
				tool.setMcpId(aiMcp.getId());
				tool.setName(toolName);
				tool.setDescription(toolSpecification.description());

				JsonObjectSchema parameters = toolSpecification.parameters();
				AiMcpParamDto aiMcpParamDto = new AiMcpParamDto();
				aiMcpParamDto.setProperties(String.valueOf(parameters.properties()));
				aiMcpParamDto.setRequired(String.valueOf(parameters.required()));

				String inputjson = new ObjectMapper().writeValueAsString(aiMcpParamDto);
				tool.setParameters(getPatameters(inputjson));

				if (existingToolMap.containsKey(toolName)) {
					toolsToUpdate.add(tool); // 已存在则更新
				} else {
					toolsToInsert.add(tool); // 新增工具
				}
            }
            // 2. 更新已有的工具
			if (!toolsToUpdate.isEmpty()) {
				aiMcpToolsRepository.updateList(toolsToUpdate);
			}
			// 3. 插入新工具
			if (!toolsToInsert.isEmpty()) {
				aiMcpToolsRepository.insertBatch(toolsToInsert);
			}
			// 4. 删除不再存在的工具
			List<String> toDeleteNames = existingToolMap.keySet().stream()
					.filter(name -> !newToolNames.contains(name))
					.collect(Collectors.toList());
			if (!toDeleteNames.isEmpty()) {
                aiMcpToolsRepository.delete("mcpId = ?1 and name in (?2) ", aiMcp.getId(), String.join(",", toDeleteNames));
			}
        } else {
           // insert
			aiMcp.setEnable("启用");
			aiMcp.setOriginFileName(file.fileName());
			aiMcp.setToolNum(toolSpecifications.size());
			repository.persist(aiMcp);

			List<AiMcpTools> aiMcpTools = new ArrayList<>();
			for (ToolSpecification toolSpecification : toolSpecifications) {
				AiMcpTools oneTool = new AiMcpTools();
				oneTool.setMcpId(aiMcp.getId());
				oneTool.setName(toolSpecification.name());
				oneTool.setDescription(toolSpecification.description());
				JsonObjectSchema parameters = toolSpecification.parameters();
				ObjectMapper objectMapper = new ObjectMapper();
				AiMcpParamDto aiMcpParamDto = new AiMcpParamDto();
				aiMcpParamDto.setProperties(String.valueOf(parameters.properties()));
				aiMcpParamDto.setRequired(String.valueOf(parameters.required()));
				String inputjson = objectMapper.writeValueAsString(aiMcpParamDto);
				oneTool.setParameters(getPatameters(inputjson));
				aiMcpTools.add(oneTool);
			}
			aiMcpToolsRepository.insertBatch(aiMcpTools);
        }
        mapClientRegistry.register(aiMcp.getId(), mcpClient);
	    return R.ok("上传成功！");
    }

    private R<Object> checkFile(FileUpload file, AiMcp aiMcp) {
        if (aiMcp.getId() == null) {
            String fileName = file.fileName();
            String mcpName = aiMcp.getName();
            long count = repository.count("name =?1 or originFileName = ?2", mcpName, fileName);
            if (count > 0) {
                return R.failed("上传的mcp已存在");
            }
        }
        return R.ok();
    }

    /**
	 * 获取mcp客户端
	 */
	public McpClient getMcpClient(AiMcp aiMcp) {
		McpClient defaultMcpClient = mapClientRegistry.get(aiMcp.getId());
		if (defaultMcpClient != null) {
			return defaultMcpClient;
		}
		List<String> commands;
		commands = List.of(aiMcp.getCommand().split(" "));

		// 获取命令类型
		String commandType = aiMcp.getCommandType();
		// 获取对应的环境变量策略
		CommandEnvStrategy envStrategy = strategyFactory.getStrategy(commandType);
		// 设置环境变量
		Map<String, String> envVars = envStrategy.getEnvironmentVariables();
		envVars.put("MCP_FILE_PATH", aiMcp.getMcpFilePath());
		List<String> commandResult = envStrategy.getCommand(commands);
		// 对命令转化
		this.convertCommand(commandResult, envVars);
		aiMcp.setCommand(commandResult.toString());

		McpTransport transport = new StdioMcpTransport.Builder()
				.environment(envVars)
				.command(commandResult)
				.logEvents(true)
				.build();
		DefaultMcpClient defaultClient = new DefaultMcpClient.Builder()
				.clientName(aiMcp.getName())
				.transport(transport)
				.build();
		mapClientRegistry.register(aiMcp.getId(), defaultClient);
		return defaultClient;
	}

    private void convertCommand(List<String> commandResult, Map<String, String> envVars) {
		// 寻找到已$符号开始的
		for (int i = 0; i < commandResult.size(); i++) {
			String command = commandResult.get(i);
			if (command.startsWith("$")) {
				String envName = command.substring(1);
				String envValue = envVars.get(envName);
				commandResult.set(i, envValue);
			}
		}
	}

    private String getPatameters(String inputJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> raw = JsonUtil.fromJsonToMap(inputJson);

        // 解析 required
        String requiredRaw = raw.get("required").toString();
        requiredRaw = requiredRaw.replaceAll("[\\[\\]\"]", "");
        Set<String> requiredSet = new HashSet<>(Arrays.asList(requiredRaw.split(",")));

        // 解析 properties（字符串模拟的 map）
        String propertiesRaw = raw.get("properties").toString();
        propertiesRaw = propertiesRaw.replaceAll("[\\{\\}]", ""); // 去除大括号
        String[] entries = propertiesRaw.split(",");

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (String entry : entries) {
            String[] kv = entry.split("=", 2);
            if (kv.length < 2)
                continue;

            String key = kv[0].trim();
            String value = kv[1].trim();
            String type;
            if (value.contains("JsonStringSchema")) {
                type = "string";
            } else if (value.contains("JsonIntegerSchema")) {
                type = "integer";
            } else if (value.contains("JsonNumberSchema")) {
                type = "number";
            } else if (value.contains("JsonBooleanSchema")) {
                type = "boolean";
            } else {
                type = "unknown";
            }

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("property", key);
            item.put("type", type);
            item.put("required", requiredSet.contains(key));
            resultList.add(item);
        }

        // 输出结果
        return mapper.writeValueAsString(resultList);
    }

    @Override
    public void replaceById(AiMcp entity) {
        repository.updateById(entity);
    }

    @Override
    public Object findAllBySelectOption() {
        AiMcpQueryDto queryDto = new AiMcpQueryDto();
        queryDto.setEnable("启用");
        List<AiMcp> list = repository.selectList(queryDto);
		List<SelectOption> selectOption = new ArrayList<>();
		for (AiMcp aiMcp : list) {
			SelectOption option = new SelectOption();
			option.setLabel(aiMcp.getName());
			option.setValue(aiMcp.getId());
			selectOption.add(option);
		}
		return selectOption;
    }

    @Override
    public String callMcpTools(Long mcpId, Long mcpToolId, String arguments, String question) {
		AiMcp aiMcp = repository.findById(mcpId);
		AiMcpTools oneTool = aiMcpToolsRepository.findById(mcpToolId);
		JsonObject paramObject = new JsonObject();
		if (StringUtil.isNullOrEmpty(oneTool.getParameters())) {
			// 提取参数
			List<AiMcpToolParam> params = JsonUtil.fromJsonToList(oneTool.getParameters(), AiMcpToolParam.class);
			for (AiMcpToolParam param : params) {
				if ("question".equals(param.getProperty())) {
					paramObject.put(param.getProperty(), question);
				}
			}
		}
		if (!paramObject.isEmpty()) {
			arguments = paramObject.toString();
		}
		log.info("mcp执行参数：name{},arguments: {}", oneTool.getName(), arguments);
		McpClient mcpClient = getMcpClient(aiMcp);
		ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder()
				.name(oneTool.getName())
				.arguments(arguments)
				.build();
		return mcpClient.executeTool(toolExecutionRequest);
	}

}