package org.huazhi.ai.aidocument.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.huazhi.ai.aidocument.entity.AiDocument;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentStoreDto;
import org.huazhi.ai.aidocument.entity.dto.ParagraphsDto;
import org.huazhi.ai.aidocument.entity.dto.PreviewFileDto;
import org.huazhi.ai.aidocument.entity.dto.SplitterStrategy;
import org.huazhi.ai.aidocument.entity.mapper.AiDocumentMapper;
import org.huazhi.ai.aidocument.entity.vo.ParagraphVo;
import org.huazhi.ai.aidocument.repository.AiDocumentRepository;
import org.huazhi.ai.aidocument.service.factory.DocumentSplitterFactory;
import org.huazhi.ai.aidocument.service.strategy.DocumentSplittingStrategy;
import org.huazhi.ai.aiparagraph.entity.AiParagraph;
import org.huazhi.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.huazhi.ai.aiparagraph.repository.AiParagraphRepository;
import org.huazhi.ai.aistatistics.util.DateUtil;
import org.huazhi.ai.assistant.StreamedAssistant;
import org.huazhi.config.FileConfig;
import org.huazhi.util.FileUtil;
import org.huazhi.util.IdUtil;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.fasterxml.jackson.databind.JsonNode;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.util.StringUtil;
import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AiDocumentServiceImp implements AiDocumentService {
	private static final Logger logger = Logger.getLogger(AiDocumentServiceImp.class);

	@Inject
    AiDocumentMapper mapper;

    @Inject
    AiDocumentRepository repository;

	@Inject
	AiParagraphRepository aiParagraphRepository;

	@Inject
    DocumentSplitterFactory documentSplitterFactory;

	@Inject
	EmbeddingStoreRegistry embeddingStoreRegistry;

	@Inject
	EmbeddingModel embeddingModel;

	@Inject
	StreamingChatModel streamingChatModel;

	@Inject
	FileConfig fileConfig;
    @Override
    public List<AiDocument> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiDocument> listEntitysByDto(AiDocumentQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public PageResult<AiDocument> listPage(AiDocumentQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiDocument entity) {
        entity.setCreateTime(LocalDateTime.now());
		entity.setIsDeleted(0);
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getDocumentCount() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> documentCountByDay = repository.getDocumentCountByDay();

		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (!documentCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : documentCountByDay) {
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
    public List<Long> getDocumentCountBefore() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> documentCountByDay = repository.getDocumentCountByDay();

		List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

		if (!documentCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : documentCountByDay) {
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
    public List<Map<String, Object>> countDocumentsByKnowledgeBase() {
        return repository.countDocumentsByKnowledgeBase();
    }

	@Override
	@Transactional
    public AiDocument updateById(AiDocument aiDocument) {
        AiDocument entity = AiDocument.findById(aiDocument.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        mapper.updateEntity(aiDocument, entity);
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }

	@Override
	public R<JsonObject> uploadFile(FileUpload file, String strategyStr) {
        String defaultStrategy = "regexSplitter";
		if (StringUtil.isNullOrEmpty(defaultStrategy)) {
			SplitterStrategy strategy = JsonUtil.fromJson(defaultStrategy, SplitterStrategy.class);
            defaultStrategy = strategy.getStrategy();
		}
		Path saveFile = FileUtil.saveFile(file, fileConfig.basePath() + "/temp");
        Document document = FileSystemDocumentLoader.loadDocument(saveFile);

        DocumentSplittingStrategy strategy = documentSplitterFactory.getStrategy(defaultStrategy, null);
        List<TextSegment> split = strategy.getSplitter().split(document);
        List<ParagraphVo> jsonParagraphs = split.stream().map(textSegment -> {
			String paragraph = textSegment.text();
			ParagraphVo paragraphVo = new ParagraphVo();
            paragraphVo.setContent(paragraph);
            paragraphVo.setLength(paragraph.length());
            paragraphVo.setTitle(paragraph.substring(0, Math.min(10, paragraph.length())));
			return paragraphVo;
		}).toList();
		JsonObject result = new JsonObject();
		result.put("name", file.fileName());
		result.put("content", jsonParagraphs);
		result.put("tempFilePath", saveFile.toAbsolutePath().toString());
		return R.ok(result);
    }

	@Override
	public R<Object> vectorDocument(List<AiDocumentStoreDto> storeDtos) throws Exception {
		checkDocName(storeDtos);
		for (AiDocumentStoreDto storeDto : storeDtos) {
			// 保存文档
			AiDocument aiDocument = saveDocument(storeDto);
			// 临时文档
			File tempFile = new File(storeDto.getTempFilePath());
			String fileName = "%s%s%s".formatted(IdUtil.simpleUUID(), '_', storeDto.getFileName());
			FileUtil.saveFile(fileConfig.basePath() + "/knowledge/" + aiDocument.getId(), fileName, new FileInputStream(tempFile));

			EmbeddingStore<Object> knowledgeStore = embeddingStoreRegistry.getStore(aiDocument.getKnowledgeId());
			// 保存段落
			List<AiParagraph> aiParagraphs = new ArrayList<>();
			int index = 0;
			for (ParagraphsDto paragraphsDto : storeDto.getParagraphs()) {
				AiParagraph aiParagraph = new AiParagraph();
				aiParagraph.setContent(paragraphsDto.getContent());
				aiParagraph.setDocId(aiDocument.getId());
				aiParagraph.setCharacterNumber(paragraphsDto.getLength());
				// 在每个段落添加标签
				String content = paragraphsDto.getContent();
				content = "%s%s".formatted("标签:"+storeDto.getFileName() + "\n" , content);
				Response<Embedding> embed = embeddingModel.embed(content);
				Metadata metadata = new Metadata();
				metadata.put("fileName", storeDto.getFileName());
				metadata.put("index", index);
				metadata.put("bucketName", "knowledge/" + aiDocument.getId());
				TextSegment textSegment = new TextSegment(content, metadata);
				String embeddingId = knowledgeStore.add(embed.content(), textSegment);
				aiParagraph.setVectorId(embeddingId);
				Map<String, Object> metadataMap = metadata.toMap();
				aiParagraph.setMetadata(JsonUtil.toJson(metadataMap));
				aiParagraphs.add(aiParagraph);
				index++;
			}
			aiParagraphRepository.insertList(aiParagraphs);
			// 删除临时文件
			boolean deleted = tempFile.delete();
			if (!deleted) {
				logger.error("Failed to delete temporary file: " + tempFile.getAbsolutePath());
			}
		}
		return R.ok("success");
	}

	/**
	 * check document name is unique
	 * delete repeated document in database, and save new document
	 */
	private void checkDocName(List<AiDocumentStoreDto> storeDtos) {
		for (AiDocumentStoreDto storeDto : storeDtos) {
			AiDocumentQueryDto query = new AiDocumentQueryDto().setFileName(storeDto.getFileName()).setKnowledgeId(storeDto.getKnowledgeBaseId());
			List<AiDocument> list = repository.selectList(query);
			if (!list.isEmpty()) {
				// delete repeated document
				List<Long> docIds = list.stream().map(AiDocument::getId).toList();
				repository.deleteByIds(docIds);
				// delete paragraphs
				List<String> vertexIds = new ArrayList<>();
				for (Long docId : docIds) {
					AiParagraphQueryDto paragraphQueryDto = new AiParagraphQueryDto().setDocId(docId);
					List<AiParagraph> paragraphs = aiParagraphRepository.selectList(paragraphQueryDto);
					vertexIds.addAll(paragraphs.stream().map(AiParagraph::getVectorId).toList());
					aiParagraphRepository.deleteByIds(paragraphs.stream().map(AiParagraph::getId).toList());;
				}
				// delete vertices
				if (!vertexIds.isEmpty()){
					embeddingStoreRegistry.getStore(storeDto.getKnowledgeBaseId()).removeAll(vertexIds);
				}
			}
		}
	}

	private AiDocument saveDocument(AiDocumentStoreDto storeDto) {
		AiDocument aiDocument = new AiDocument();
		aiDocument.setId(storeDto.getId());
		aiDocument.setKnowledgeId(storeDto.getKnowledgeBaseId());
		aiDocument.setDocName(storeDto.getFileName());
		aiDocument.setCharacterNumber(storeDto.getParagraphs().stream().mapToInt(ParagraphsDto::getLength).sum());
		aiDocument.setSectionNumber(storeDto.getParagraphs().size());
		aiDocument.setStatus("成功");
		aiDocument.setEnableStatus("启用");
		aiDocument.setSplitterFlag(storeDto.getFlag());
		aiDocument.setSplitterLength(storeDto.getLength());
		aiDocument.setSplitterStrategy(storeDto.getStrategy());
		if (aiDocument.getId() == null){
			repository.persist(aiDocument);
		} else {
			this.updateById(aiDocument);
			// 将关联的段落删除，后面会新增新的段落
			aiParagraphRepository.deleteByDocumentId(aiDocument.getId());
		}
		return aiDocument;
	}

	@Override
	public void removeVertices(Long knowledgeId, List<String> embeddingStoreIds) {
		EmbeddingStore<TextSegment> embeddingStore = embeddingStoreRegistry.getStore(knowledgeId);
		embeddingStore.removeAll(embeddingStoreIds);
	}

	@Override
	public String vectorParagraph(Long knowledgeId, String content, Metadata metadata) {
		EmbeddingStore<Object> knowledgeStore = embeddingStoreRegistry.getStore(knowledgeId);
		Response<Embedding> embed = embeddingModel.embed(content);
		TextSegment textSegment = new TextSegment(content, metadata);
		return knowledgeStore.add(embed.content(), textSegment);
	}

	@Override
	public R<Object> uploadFileDoc(FileUpload file, String strategyStr) throws Exception {
		JsonNode jsonObject = JsonUtil.toJsonObject(strategyStr);
		List<AiDocumentStoreDto> storeDtos = new ArrayList<>();
		// 上传文档
		R<JsonObject> objectR = this.uploadFile(file, strategyStr);
		AiDocumentStoreDto storeDto = new AiDocumentStoreDto();
		storeDto.setId(objectR.getData().getLong("id"));
		storeDto.setKnowledgeBaseId(jsonObject.get("knowledgeBaseId").asLong());
		storeDto.setStrategy(jsonObject.get("strategy").asText());
		storeDto.setFlag(jsonObject.get("flag").asText());
		storeDto.setLength(jsonObject.get("length").asText());
		storeDto.setFileName(file.fileName());
		storeDto.setTempFilePath(objectR.getData().getString("tempFilePath"));
		JsonArray content = objectR.getData().getJsonArray("content");

		List<ParagraphsDto> paragraphsDtos = JsonUtil.fromJsonToList(content.toString(), ParagraphsDto.class);
		storeDto.setParagraphs(paragraphsDtos);
		storeDtos.add(storeDto);
		return this.vectorDocument(storeDtos);
	}

	@Override
	public JsonObject getParagraphs(PreviewFileDto previewFileDto) {
		Document document = FileSystemDocumentLoader.loadDocument(previewFileDto.getPath());
		Map<String, Object> params = new HashMap<>();
		params.put("segmentSize", previewFileDto.getLength());
		params.put("flag", previewFileDto.getFlag());
		DocumentSplittingStrategy strategy = documentSplitterFactory.getStrategy(previewFileDto.getStrategy(), params);
		List<TextSegment> split = strategy.getSplitter().split(document);
		List<JsonObject> jsonParagraphs = split.stream().map(textSegment -> {
			String paragraph = textSegment.text();
			JsonObject jsonObject = new JsonObject();
			if (textSegment.metadata() != null && textSegment.metadata().containsKey("HEADING")) {
				jsonObject.put("title", textSegment.metadata().getString("HEADING"));
			} else {
				jsonObject.put("title", paragraph.substring(0, Math.min(10, paragraph.length())));
			}
			jsonObject.put("content", paragraph);
			jsonObject.put("length", paragraph.length());
			return jsonObject;
		}).toList();
		JsonObject result = new JsonObject();
		result.put("name", previewFileDto.getFileName());
		result.put("content", jsonParagraphs);
		result.put("tempFilePath", previewFileDto.getPath());
		return result;
	}

	@Override
	public Multi<String> hitTest(String message, Long knowledgeBaseId) {
		EmbeddingStore<TextSegment> embeddingStore = embeddingStoreRegistry.getStore(knowledgeBaseId);
		StreamedAssistant assistant = AiServices.builder(StreamedAssistant.class)
				.streamingChatModel(streamingChatModel)
				.chatMemory(MessageWindowChatMemory.withMaxMessages(10))
				.contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
				.build();
		return assistant.respondToQuestion(message);
	}

	@Override
	public List<String> getParagraphsByKnowledgeBaseId(Long knowledgeBaseId, String text, Integer maxResult,
			Double minScore) {
		List<String> result = new ArrayList<>();
		// 需要查询的内容 向量化
		Embedding queryEmbedding = embeddingModel.embed(text).content();
		EmbeddingStore<TextSegment> embeddingStore = embeddingStoreRegistry.getStore(knowledgeBaseId);

		// 去向量数据库查询
		// 构建查询条件
		EmbeddingSearchRequest build = EmbeddingSearchRequest.builder()
				.queryEmbedding(queryEmbedding)
				.maxResults(maxResult)
				.minScore(minScore)
				.build();

		EmbeddingSearchResult<TextSegment> segmentEmbeddingSearchResult = embeddingStore.search(build);
		segmentEmbeddingSearchResult.matches().forEach(embeddingMatch -> {
			result.add(embeddingMatch.embedded().text());
		});
		return result;
	}
}