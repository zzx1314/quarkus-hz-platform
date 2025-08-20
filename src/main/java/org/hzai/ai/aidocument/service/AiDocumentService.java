package org.hzai.ai.aidocument.service;

import java.util.List;
import java.util.Map;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.hzai.ai.aidocument.entity.dto.AiDocumentStoreDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;

import io.vertx.core.json.JsonObject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface AiDocumentService {
   List<AiDocument> listEntitys();

   List<AiDocument> listEntitysByDto(AiDocumentQueryDto sysOrgDto);

   PageResult<AiDocument> listPage(AiDocumentQueryDto dto, PageRequest pageRequest);

   Boolean register(AiDocument entity);

   List<Map<String, Object>> countDocumentsByKnowledgeBase();

   List<Long> getDocumentCount();

   List<Long> getDocumentCountBefore();

   long count();

   AiDocument updateById(AiDocument aiDocument);

   /**
    * 上传成功处理，返回段落列表
    */
   R<JsonObject> uploadFile(FileUpload file, String strategyStr);


   /**
	 * 段落向量化
	 */
    R<Object> vectorDocument(List<AiDocumentStoreDto> storeDtos) throws Exception;
}