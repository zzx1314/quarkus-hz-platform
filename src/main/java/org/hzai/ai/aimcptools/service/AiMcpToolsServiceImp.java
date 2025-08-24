package org.hzai.ai.aimcptools.service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.hzai.ai.aimcptools.repository.AiMcpToolsRepository;
import org.hzai.ai.common.SelectOption;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiMcpToolsServiceImp implements AiMcpToolsService {
    @Inject
    AiMcpToolsRepository repository;
    @Override
    public List<AiMcpTools> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiMcpTools> listEntitysByDto(AiMcpToolsQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiMcpTools listOne(AiMcpToolsQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiMcpTools> listPage(AiMcpToolsQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiMcpTools entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiMcpTools entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiMcpToolsDto dto) {
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
    public Object findAllBySelectOption(Long id) {
        AiMcpToolsQueryDto queryDto = new AiMcpToolsQueryDto().setMcpId(id);
        List<AiMcpTools> list = repository.selectList(queryDto);
		List<SelectOption> selectOption = new ArrayList<>();
		for (AiMcpTools aiMcp : list) {
			SelectOption option = new SelectOption();
			option.setLabel(aiMcp.getName());
			option.setValue(aiMcp.getId());
			selectOption.add(option);
		}
		return selectOption;
    }

}