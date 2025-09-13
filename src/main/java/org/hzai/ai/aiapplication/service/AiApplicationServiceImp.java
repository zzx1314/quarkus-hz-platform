package org.hzai.ai.aiapplication.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.IntentRecognition;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.ai.aiapplication.entity.mapper.AiApplicationMapper;
import org.hzai.ai.aiapplication.entity.vo.AiApplicationVo;
import org.hzai.ai.aiapplication.repository.AiApplicationRepository;
import org.hzai.ai.aiapplication.service.flow.FlowEngine;
import org.hzai.ai.aidocument.service.EmbeddingStoreRegistry;
import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.service.AiKnowledgeBaseService;
import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.service.AiMcpService;
import org.hzai.ai.aimodel.service.AiModelService;
import org.hzai.ai.aiprocess.entity.AiProcess;
import org.hzai.ai.aiprocess.entity.NodeEntity;
import org.hzai.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.hzai.ai.aiprocess.entity.vo.AiProcessNet;
import org.hzai.ai.aiprocess.service.AiProcessService;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.ai.assistant.Assistant;
import org.hzai.ai.assistant.AssistantAnaly;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.SecurityUtil;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.util.StringUtil;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiApplicationServiceImp implements AiApplicationService {
    @Inject
    AiApplicationRepository repository;

	@Inject
	private AiMcpService aiMcpService;

	@Inject
	private AiKnowledgeBaseService aiKnowledgeBaseService;

	@Inject
	private EmbeddingModel embeddingModel;

	@Inject
	private EmbeddingStoreRegistry embeddingStoreRegistry;

	@Inject
	private ContentAggregator contentAggregator;

	@Inject
	private ChatMemoryProvider chatMemoryProvider;

	@Inject
	private AiModelService aiModelService;

	@Inject
	private AiProcessService aiProcessService;

	@Inject
	private FlowEngine flowEngine;

	@Inject
	SecurityUtil securityUtil;

	@Inject
	AiApplicationMapper aiApplicationMapper;
    @Override
    public List<AiApplication> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiApplicationVo> listPage(AiApplicationQueryDto queryDto, PageRequest pageRequest) {
		queryDto.setCreateId(securityUtil.getUserId());
		queryDto.setRoleIdList(securityUtil.getRole());
        return repository.selectPage(queryDto, pageRequest);
    }

    @Override
    public Boolean register(AiApplicationDto aiApplicationDto) {
		AiApplication entity = aiApplicationMapper.toEntity(aiApplicationDto);
        entity.setCreateTime(LocalDateTime.now());

		entity.setCreateId(securityUtil.getUserId());
		entity.setRoles(aiApplicationDto.getRoleIdList());

		if ("简单应用".equals(aiApplicationDto.getType())) {
			entity.setKnowledgeIds(String.join(",", aiApplicationDto.getKnowledgeIdList()));
			if (!aiApplicationDto.getMcpIdList().isEmpty()){
				entity.setMcpIds(String.join(",", aiApplicationDto.getMcpIdList()));
			}

			repository.persist(entity);
		} else {
			repository.persist(entity);
			// 复杂应用处理，添加流程
			AiProcess aiProcess = new AiProcess();
			aiProcess.setAppId(entity.getId());
			aiProcess.setNodes(aiApplicationDto.getNodes());
			aiProcess.setEdges(aiApplicationDto.getEdges());
			aiProcessService.register(aiProcess);
		}

        return true;
    }

    @Override
    public List<Long> getApplicationCount() {
         List<Long> data = new ArrayList<>();
		List<Map<String, Object>> applicationCountByDay = repository.getApplicationCountByDay();

		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (applicationCountByDay != null  && !applicationCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : applicationCountByDay) {
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
    public List<Long> getApplicationCountBefore() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> applicationCountByDay = repository.getApplicationCountByDay();

		List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

		if (!applicationCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : applicationCountByDay) {
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
	public Multi<String> chat(Long appId, String question, String filepath) {
		ChatModel chatModel = aiModelService.getChatModel();
		StreamingChatModel streamingChatModel = aiModelService.getStreamingChatModel();
		Assistant assistant;
		// 获取应用信息
		AiApplication aiApplication = repository.findById(appId);
		if ("简单应用".equals(aiApplication.getType())) {
			IntentRecognition intentRecognition = getIntentRecognition(question, chatModel);
			if (intentRecognition == IntentRecognition.GREETING){
				return Multi.createFrom().items("你好呀，我是clever_copilot智能助手，有什么可以帮你的吗？");
			} else if (intentRecognition == IntentRecognition.ASK_IDENTITY){
				return Multi.createFrom().items("我是clever_copilot智能助手，您可以问我任何问题，我会尽力帮助您。");
			}
			assistant = this.simpleAssistant(aiApplication, chatModel, streamingChatModel);
			return assistant.chatForEachUse(securityUtil.getUserId(), question);
		} else {
			return this.complexAssistant(aiApplication, chatModel, streamingChatModel, securityUtil.getUserId(), question, filepath);
		}
	}

	/**
	 * 复杂应用
	 */
	private Multi<String> complexAssistant(AiApplication aiApplication,
										  ChatModel chatModel, StreamingChatModel streamingChatModel, Long userId, 
										  String question, String filepath) {
		// 获取流程信息
		AiProcessNet aiProcessNet = aiProcessService.getAiProcessNet(aiApplication.getId());
		NodeEntity startNode = aiProcessNet.getStartNode();
		Map<String, List<String>> edgeMap = aiProcessNet.getEdgeMap();
		Map<String, NodeEntity> nodeMap = aiProcessNet.getNodeMap();

		String currentNodeId = startNode.getId();
		Map<String, Object> context = new HashMap<>();
		Object lastOutput = "";

		// 系统级别的Assistant
		Assistant assistant = AiServices.builder(Assistant.class)
				.streamingChatModel(streamingChatModel)
				.chatMemoryProvider(chatMemoryProvider)
				.build();
		// 语义分析
		IntentRecognition intentRecognition = getIntentRecognition(question, chatModel);
		if (intentRecognition == IntentRecognition.GREETING){
			return Multi.createFrom().items("你好呀，我是clever_copilot智能助手，有什么可以帮你的吗？");
		} else if (intentRecognition == IntentRecognition.ASK_IDENTITY){
			return Multi.createFrom().items("我是clever_copilot智能助手，您可以问我任何问题，我会尽力帮助您。");
		}
		// 执行流程
		if ("启动".equals(question)) {
			lastOutput = flowEngine.startProcess(chatModel, edgeMap, nodeMap, currentNodeId, context, lastOutput, filepath);
			// 最后总结输出
			return Multi.createFrom().items(lastOutput.toString());
		} else {
			lastOutput = question;
			return assistant.chatForEachUse(userId, lastOutput.toString());
		}
	}

	/**
	 * 获取意图识别
	 */
	private IntentRecognition getIntentRecognition(String question, ChatModel chatModel){
		AssistantAnaly assistant = AiServices.create(AssistantAnaly.class, chatModel);
		IntentRecognition intentRecognition = assistant.analyzeIntention(question);
		return intentRecognition;
	}

	/**
	 * 简单应用
	 */
	private Assistant simpleAssistant(AiApplication aiApplication, ChatModel chatModel, StreamingChatModel streamingChatModel) {
		Assistant assistant;
		// 获取知识库信息
		String knowledgeId = aiApplication.getKnowledgeIds();
		List<Long> knowledgeIds = Arrays.stream(knowledgeId.split(",")).map(Long::parseLong).toList();

		List<AiKnowledgeBase> aiKnowledgeBases = aiKnowledgeBaseService.listByIds(knowledgeIds);
		Map<Long, String> idToNameMap = aiKnowledgeBases.stream()
				.collect(Collectors.toMap(
						AiKnowledgeBase::getId,
						AiKnowledgeBase::getKnowledgeBaseName
				));

		Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
		for (Long oneId : knowledgeIds) {
			EmbeddingStore<TextSegment> store = embeddingStoreRegistry.getStore(oneId);

			ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
					.embeddingStore(store)
					.embeddingModel(embeddingModel)
					.maxResults(5)
					.build();
			retrieverToDescription.put(contentRetriever, idToNameMap.get(oneId));
		}
		QueryRouter queryRouter = new LanguageModelQueryRouter(chatModel, retrieverToDescription);

		RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
				.contentAggregator(contentAggregator)
				.queryRouter(queryRouter)
				.build();

		if (StringUtil.isNullOrEmpty(aiApplication.getMcpIds())) {
			McpToolProvider mcpToolProvider = getMcpToolProvider(aiApplication);
			assistant = AiServices.builder(Assistant.class)
					.streamingChatModel(streamingChatModel)
					.toolProvider(mcpToolProvider)
					.retrievalAugmentor(retrievalAugmentor)
					.chatMemoryProvider(chatMemoryProvider)
					.build();
		} else {
			assistant = AiServices.builder(Assistant.class)
					.streamingChatModel(streamingChatModel)
					.retrievalAugmentor(retrievalAugmentor)
					.chatMemoryProvider(chatMemoryProvider)
					.build();
		}
		return assistant;
	}


	/**
	 * 获取MCP工具提供者
	 */
	private McpToolProvider getMcpToolProvider(AiApplication aiApplication) {
		McpToolProvider mcpToolProvider;
		List<Long> mcpIds = Arrays.stream(String.valueOf(aiApplication.getMcpIds()).split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .map(Long::parseLong)
        .collect(Collectors.toList());

		List<McpClient> mcpClients = new ArrayList<>();
		for (Long oneId : mcpIds) {
			AiMcp oneMcp = aiMcpService.listEntityById(oneId);
			if (oneMcp.getStatus().equals("启用")) {
				McpClient mcpClient = aiMcpService.getMcpClientById(oneMcp);
				mcpClients.add(mcpClient);
			}
		}
		mcpToolProvider = McpToolProvider.builder()
			   .mcpClients(mcpClients)
			   .build();
		return mcpToolProvider;
	}

	@Override
	public void replaceById(AiApplication aiApplication) {
		repository.updateById(aiApplication);
	}

	@Override
	public void replaceData(AiApplicationDto aiApplication) {
		if ("简单应用".equals(aiApplication.getType())) {
			repository.updateByDto(aiApplication);
		} else { 
			// 复杂应用的修改
		    repository.updateByDto(aiApplication);
			AiProcessQueryDto dto = new AiProcessQueryDto().setAppId(aiApplication.getId());
			AiProcess onepRrocess = aiProcessService.listOne(dto);
			onepRrocess.setNodes(aiApplication.getNodes());
			onepRrocess.setEdges(aiApplication.getEdges());
			aiProcessService.replaceById(onepRrocess);
		}
	}

}