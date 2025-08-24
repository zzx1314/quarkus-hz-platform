package org.hzai.ai.aiapplication.service.flow;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import io.quarkus.runtime.util.StringUtil;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.hzai.ai.aidocument.service.AiDocumentService;
import org.hzai.ai.aimcp.service.AiMcpService;
import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.repository.AiModelRepository;
import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.hzai.ai.aiparagraph.repository.AiParagraphRepository;
import org.hzai.ai.aiprocess.entity.NodeEntity;
import org.hzai.ai.assistant.Assistant;


@Slf4j
@ApplicationScoped
public class FlowEngine {
	@Inject
	private AiMcpService aiMcpService;

	@Inject
	private AiModelRepository aiModelService;

	@Inject
	private ChatMemoryProvider chatMemoryProvider;

	@Inject
	private AiParagraphRepository aiParagraphService;

	@Inject
	private AiDocumentService aiDocumentService;

	@Inject
	private ExecutorService flowExecutor;

	public Object startProcess(ChatModel chatModel,
							   Map<String, List<String>> edgeMap,
							   Map<String, NodeEntity> nodeMap,
							   String startNodeId,
							   Map<String, Object> context,
							   Object lastOutput,
							   String filepath) {

		Map<String, Set<String>> nodePredecessors = buildPredecessorMap(edgeMap);
		ConcurrentMap<String, Set<String>> arrivedPredecessors = new ConcurrentHashMap<>();

		CountDownLatch endLatch = new CountDownLatch(1);
		AtomicReference<Object> finalOutput = new AtomicReference<>();
		ConcurrentMap<String, Boolean> submittedFlags = new ConcurrentHashMap<>();

		submitNode(chatModel, edgeMap, nodeMap, startNodeId, context, lastOutput, filepath,
				nodePredecessors, arrivedPredecessors, finalOutput, endLatch, submittedFlags);

		try {
			endLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return finalOutput.get();
	}

	private void submitNode(ChatModel chatModel,
			Map<String, List<String>> edgeMap,
			Map<String, NodeEntity> nodeMap,
			String currentNodeId,
			Map<String, Object> context,
			Object lastOutput,
			String filepath,
			Map<String, Set<String>> nodePredecessors,
			ConcurrentMap<String, Set<String>> arrivedPredecessors,
			AtomicReference<Object> finalOutput,
			CountDownLatch endLatch,
			ConcurrentMap<String, Boolean> submittedFlags) {

		flowExecutor.submit(() -> {
			NodeEntity current = nodeMap.get(currentNodeId);
			// 执行当前节点
			Object output = executeNode(current, lastOutput, chatModel, filepath, nodeMap);
			context.put(currentNodeId, output);
			// 若为结束节点，设置最终输出
			if ("end".equals(current.getType())) {
				finalOutput.set(output);
				endLatch.countDown();
				return;
			}
			// 获取当前节点指向的所有后继节点
			List<String> nextList = edgeMap.getOrDefault(currentNodeId, Collections.emptyList());
			for (String nextId : nextList) {
				// 并发安全地记录已到达的前置节点
				Set<String> arrivedSet = arrivedPredecessors.computeIfAbsent(nextId,
						k -> ConcurrentHashMap.newKeySet());
				arrivedSet.add(currentNodeId); // 多线程安全的 add

				// 获取该后继节点的所有前置节点
				Set<String> requiredPre = nodePredecessors.getOrDefault(nextId, Collections.emptySet());
				// 如果所有前置节点都已到达，则触发后继节点执行
				if (arrivedSet.containsAll(requiredPre)) {
					Boolean alreadySubmitted = submittedFlags.putIfAbsent(nextId, true);
					if (alreadySubmitted == null) { // 第一次提交
						Object mergedInput;
						if (requiredPre.size() == 1) {
							// 单前置节点，直接传递对应 output
							mergedInput = context.get(requiredPre.iterator().next());
						} else {
							// 多前置节点，合并成一个 Map 传递
							Map<String, Object> inputMap = new HashMap<>();
							for (String preId : requiredPre) {
								inputMap.put(preId, context.get(preId));
							}
							mergedInput = inputMap;
						}
						// 提交后继节点任务
						submitNode(chatModel, edgeMap, nodeMap, nextId, context, mergedInput, filepath,
								nodePredecessors, arrivedPredecessors, finalOutput, endLatch, submittedFlags);
					}

				}
			}
		});
	}

	/**
	 * 构建节点的依赖关系
	 * key 是节点 ID，代表“终止节点”（from）；
	 * value 是一个 Set，包含所有指向该节点的前置节点 ID（to）。
	 */
	private Map<String, Set<String>> buildPredecessorMap(Map<String, List<String>> edgeMap) {
		Map<String, Set<String>> result = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : edgeMap.entrySet()) {
			String from = entry.getKey();
			for (String to : entry.getValue()) {
				result.computeIfAbsent(to, k -> new HashSet<>()).add(from);
			}
		}
		return result;
	}

	public Object executeNode(NodeEntity node, Object input, ChatModel chatModel, String filepath, Map<String, NodeEntity> nodeMap) {
		log.info("执行节点：" + node.getType());
		switch (node.getType()) {
			case "start":
				return "流程启动";
			case "model":
				return modelCall(node, input, nodeMap);
			case "mcp":
				return mcpCall(node, input.toString(), chatModel, filepath);
			case "knowledge":
				return kbCall(node, input.toString(), chatModel);
			case "system":
				return systemCall(node, input.toString(), chatModel, filepath);
			case "end":
				return input;
			default:
				throw new RuntimeException("未知节点类型：" + node.getType());
		}
	}

	/**
	 * 系统调用
	 */
	private Object systemCall(NodeEntity systemNode, String inputData, ChatModel chatModel, String filepath) {
		JsonObject config = systemNode.getData();
		if ("文档解析".equals(config.getString("toolName"))) {
			if (StringUtil.isNullOrEmpty(filepath)) {
				// 解析全文
				Document document = FileSystemDocumentLoader.loadDocument(filepath);
				return document.text();
			}
		}
		return "";
	}


	/**
	 * 模型调用
	 */
	private String modelCall(NodeEntity modelNode, Object inputQuestion, Map<String, NodeEntity> nodeMap) {
		log.info("inputQuestion" + inputQuestion.toString());
		String resultQuestion = "";
		JsonObject data = modelNode.getData();
		AiModel aiModel = aiModelService.findById(data.getLong("modelId"));
		if ((inputQuestion instanceof String) && StringUtil.isNullOrEmpty(data.getString("command"))) {
			// 如果配置给模型命令
			String command = data.getString("command");
			resultQuestion = command.replaceAll("\\$\\{data\\}", inputQuestion.toString());
		} else if (inputQuestion instanceof Map<?,?> && StringUtil.isNullOrEmpty(data.getString("command"))) {
			log.info("inputQuestion 是 map 结构");
			// 是map结构
			String command = data.getString("command");
			@SuppressWarnings("unchecked")
			Map<String, Object> mapData = (Map<String, Object>) inputQuestion;
			Map<String, String> resulData = new HashMap<>();
			if (!mapData.isEmpty()) {
				for (Map.Entry<String, Object> entry : mapData.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					NodeEntity nodeEntity = nodeMap.get(key);
					if (nodeEntity != null && nodeEntity.getType().equals("system")){
						resulData.put("inputDoc", value.toString());
					} else if (nodeEntity != null && nodeEntity.getType().equals("knowledge")){
						resulData.put("kbDoc", value.toString());
					}
				}
			}
			// 替换 command 中的 ${inputData} 和 ${kbData}
			for (Map.Entry<String, String> entry : resulData.entrySet()) {
				String placeholder = "${" + entry.getKey() + "}";
				command = command.replace(placeholder, entry.getValue());
			}
			resultQuestion = command;
		}
		ChatModel chatModel = OpenAiChatModel.builder()
				.baseUrl(aiModel.getBaseUrl())
				.apiKey(aiModel.getApiKey())
				.modelName(aiModel.getModelName())
				.timeout(Duration.ofSeconds(300))
				.build();
		Assistant assistant = AiServices.builder(Assistant.class)
				.chatMemoryProvider(chatMemoryProvider).chatModel(chatModel).build();
		return assistant.chatProcess(resultQuestion);
	}

	/**
	 * mcp调用
	 */
	private String mcpCall(NodeEntity mcpNode, String question, ChatModel chatModel, String filepath) {
		JsonObject config = mcpNode.getData();
		if (StringUtil.isNullOrEmpty(filepath)) {
			// 文件路径
			JsonObject param = new JsonObject();
			param.put("docPath", filepath);
			config.put("arguments", param.toString());
		}
		String callResult = aiMcpService.callMcpTools(config.getLong("mcpId"), config.getLong("toolId"),
				config.getString("arguments"), question);
		// 检查返回值中是否有系统系统关键词，系统做出处理，将处理结果拼接到后面
		log.info("mcp返回数据：" +callResult);
		if (callResult.contains("[system]:")) {
			String[] parts = callResult.split("\\[system\\]:", 2);
			String systemMess = parts.length > 1 ? "[system]:" + parts[1].trim() : "";
			log.info("系统处理结果：" + systemMess);
			// 判断是否有filePath
			if (systemMess.contains("filePath=")) {
				String filePath = systemMess.substring(systemMess.indexOf("filePath=") + 9).trim();
				String docLink = "[文档地址](http://192.168.41.227:9999/upms/aiApplication/downFile?filePath=" + filePath + ")";
				callResult = parts[0].trim();
				callResult = callResult + "\n" + docLink;
			} else {
				// 只有 system 信息，没有 filePath，也要去掉 system 信息
				callResult = parts[0].trim();
			}
		}
		if (Boolean.TRUE.equals(config.getBoolean("callMode"))) {
			Assistant assistant = AiServices.builder(Assistant.class).chatModel(chatModel).build();
			return assistant.chatProcess(callResult);
		} else {
			return callResult;
		}
	}
	/**
	 * 知识库调用
	 */
	private String kbCall(NodeEntity kbNode, String question, ChatModel chatModel) {
		JsonObject config = kbNode.getData();
		Long id = config.getLong("id");
		Long docId = config.getLong("docId");
		Integer maxResult = config.getInteger("maxResult");
		Double minScore = config.getDouble("minScore");

		if (docId != null) {
			AiParagraphQueryDto queryDto = new AiParagraphQueryDto().setDocId(docId);
			List<AiParagraph> allParagraph = aiParagraphService.selectList(queryDto);
			String paragraph = allParagraph.stream().map(AiParagraph::getContent).collect(Collectors.joining("\n"));
			return paragraph;
		} else {
			List<String> paragraphsByKnowledgeBaseId = aiDocumentService.getParagraphsByKnowledgeBaseId(id, question, maxResult, minScore);
			if (Boolean.TRUE.equals(config.getBoolean("callMode"))) {
				Assistant assistant = AiServices.builder(Assistant.class).chatModel(chatModel).build();
				if (!paragraphsByKnowledgeBaseId.isEmpty()) {
					return assistant.chatProcess(String.join("\n", paragraphsByKnowledgeBaseId));
				} else {
					return "不好意思，没有找到相关知识";
				}
			} else {
				if (!paragraphsByKnowledgeBaseId.isEmpty()) {
					return String.join("\n", paragraphsByKnowledgeBaseId);
				} else {
					return "不好意思，没有找到相关知识";
				}
			}
		}
	}



}
