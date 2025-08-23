package org.hzai.ai.aimodel.service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.entity.dto.AiModelQueryDto;
import org.hzai.ai.aimodel.entity.dto.AiModelDto;
import org.hzai.ai.aimodel.repository.AiModelRepository;
import org.hzai.ai.common.SelectOption;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiModelServiceImp implements AiModelService {
    @Inject
    AiModelRepository repository;
    @Override
    public List<AiModel> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiModel> listEntitysByDto(AiModelQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiModel listOne(AiModelQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiModel> listPage(AiModelQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiModel entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiModel entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiModelDto dto) {
        repository.updateByDto(dto);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void removeByIds(List<Long> ids) {
        repository.deleteByIds(ids);
    }

    @Override
    public Object findAllBySelectOption() {
        AiModelQueryDto queryDto = new AiModelQueryDto();
        queryDto.setEnable("true");
        List<AiModel> list = repository.selectList(queryDto);
		List<SelectOption> selectOption = new ArrayList<>();
		for (AiModel aiMcp : list) {
			SelectOption option = new SelectOption();
			option.setLabel(aiMcp.getModelName());
			option.setValue(aiMcp.getId());
			selectOption.add(option);
		}
		return selectOption;
    }

    @Override
    public ChatModel getChatModel() {
        AiModelQueryDto queryDto = new AiModelQueryDto();
        queryDto.setEnable("true");
        queryDto.setModelType("chat");
        AiModel mode = repository.selectOne(queryDto);
		return OpenAiChatModel.builder()
				.baseUrl(mode.getBaseUrl())
				.temperature(mode.getTemperature())
				.maxTokens(mode.getMaxTokens())
				.frequencyPenalty(mode.getFrequencyPenalty())
				.apiKey(mode.getApiKey())
				.modelName(mode.getModelName())
				.build();
    }

    @Override
    public StreamingChatModel getStreamingChatModel() {
        AiModelQueryDto queryDto = new AiModelQueryDto();
        queryDto.setEnable("true");
        queryDto.setModelType("chat");
        AiModel mode = repository.selectOne(queryDto);
		return OpenAiStreamingChatModel.builder()
				.baseUrl(mode.getBaseUrl())
				.apiKey(mode.getApiKey())
				.modelName(mode.getModelName())
				.build();
    }

}