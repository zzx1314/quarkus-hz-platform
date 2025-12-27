package org.huazhi.ai.aidocument.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.*;

import org.huazhi.ai.aidocument.entity.AiDocument;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentStoreDto;
import org.huazhi.ai.aidocument.entity.dto.PreviewFileDto;
import org.huazhi.ai.aidocument.service.AiDocumentService;
import org.huazhi.ai.aiparagraph.entity.AiParagraph;
import org.huazhi.ai.aiparagraph.entity.dto.AiParagraphDto;
import org.huazhi.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.huazhi.ai.aiparagraph.service.AiParagraphService;
import org.huazhi.ai.common.SelectOption;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.RestForm;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import dev.langchain4j.data.document.Metadata;

@Path("/aiDocument")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiDocumentController {
    @Inject
    AiDocumentService aiDocumentService;

    @Inject
    AiParagraphService aiParagraphService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiDocument>> getPage(@BeanParam AiDocumentQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiDocumentService.listPage(dto, pageRequest));
    }

    /**
	 * 段落分页
	 */
    @GET
	@Path("paragraphPage")
	public R<Object> findParagraphListByPage(@BeanParam AiParagraphQueryDto queryDto, @BeanParam PageRequest pageRequest) {
		return R.ok(aiParagraphService.listPage(queryDto, pageRequest));
	}

    @GET
    @Path("/getByDto")
    public R<List<AiDocument>> getByDto(@BeanParam AiDocumentQueryDto dto) {
        return R.ok(aiDocumentService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiDocument>> getAll() {
        return R.ok(aiDocumentService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiDocument entity) {
        return R.ok(aiDocumentService.register(entity));
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiDocument entity = AiDocument.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        // 同步删除段落
        aiParagraphService.removeByDocumentId(id);
        return R.ok();
    }

    @DELETE
    @Path("/deleteParagraph/{id}/{knowledgeId}")
    @Transactional
	public R<Object> deleteParagraph(@PathParam("id") Long id, @PathParam("knowledgeId") Long knowledgeId) {
		AiParagraph aiParagraph = aiParagraphService.listById(id);
		aiParagraphService.removeById(id);
		List<String> embeddingStoreIds = new ArrayList<>();
		embeddingStoreIds.add(aiParagraph.getVectorId());
		aiDocumentService.removeVertices(knowledgeId, embeddingStoreIds);
		return R.ok("success");
	}

     /**
	 * 文件上传-预览分段
	 */
    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public R<JsonObject> uploadFile(@RestForm("file") FileUpload file, @RestForm("strategy") String strategy) {
        return aiDocumentService.uploadFile(file, strategy);
    }

    /**
	 * 文档内部上传文档
	 */
    @POST
	@Path(value = "/uploadFileDoc")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	public R<Object> uploadFileDoc(@RestForm("file") FileUpload file, @RestForm("strategy") String strategy) throws Exception {
		return aiDocumentService.uploadFileDoc(file, strategy);
	}

    /**
	 * 文件预览
	 */
	@GET
    @Path(value = "/previewFile")
	public R<Object> previewFile(@BeanParam PreviewFileDto previewFileDto) {
		JsonObject paragraphs = aiDocumentService.getParagraphs(previewFileDto);
		return R.ok(paragraphs);
	}

    /**
	 * 通过知识库id查询文档
	 */
    @GET
	@Path("getDocByKbId/{kbId}")
	public R<Object> getDocByKbId(@PathParam("kbId") Long kbId) {
		List<SelectOption> selectOptions = new ArrayList<>();
        AiDocumentQueryDto dto = new AiDocumentQueryDto().setKnowledgeId(kbId);
		List<AiDocument> list = aiDocumentService.listEntitysByDto(dto);
		if (!list.isEmpty()) {
			for (AiDocument aiDocument : list) {
				SelectOption selectOption = new SelectOption();
				selectOption.setLabel(aiDocument.getDocName());
				selectOption.setValue(aiDocument.getId());
				selectOptions.add(selectOption);
			}
		}
		return R.ok(selectOptions);
	}

    /**
	 * 存储段落
	 */
    @POST
	@Path("storeParagraph")
	public R<Object> storeParagraph(List<AiDocumentStoreDto> storeDto) throws Exception {
		return aiDocumentService.vectorDocument(storeDto);
	}

    /**
	 * 命中测试
	 */
    @GET
	@Path(value = "/hitTest")
    @Produces("text/stream;charset=UTF-8")
	public Multi<String> hitTest(@QueryParam("message") String message, @QueryParam("knowledgeBaseId")Long knowledgeBaseId) {
		return aiDocumentService.hitTest(message, knowledgeBaseId);
	}
    @PUT
    @Path("/update")
	public R<Object> update(AiDocument aiDocument) {
		aiDocumentService.updateById(aiDocument);
		return R.ok("success");
	}

    @PUT
    @Path("updateParagraph")
	public R<Object> updateParagraph(AiParagraphDto aiParagraph) {
		// get metadata
		AiParagraph oneParagraph = aiParagraphService.listById(aiParagraph.getId());
		Map<String, Object> metaDataMap = JsonUtil.fromJsonToMap(oneParagraph.getMetadata());
		Metadata metadata = Metadata.from(metaDataMap);
		// delete old vectorId
		List<String> embeddingStoreIds = new ArrayList<>();
		embeddingStoreIds.add(aiParagraph.getVectorId());
		aiDocumentService.removeVertices(aiParagraph.getKnowledgeId(), embeddingStoreIds);
		// replace old vectorId，over again vector
		String vectorId = aiDocumentService.vectorParagraph(aiParagraph.getKnowledgeId(), aiParagraph.getContent(), metadata);
		aiParagraph.setVectorId(vectorId);
		aiParagraphService.replaceByDto(aiParagraph);
		return R.ok("success");
	}

}