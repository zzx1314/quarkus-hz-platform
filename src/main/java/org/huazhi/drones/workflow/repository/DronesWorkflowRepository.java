package org.huazhi.drones.workflow.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.drones.workflow.entity.DronesWorkflow;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowDto;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowQueryDto;
import org.huazhi.drones.workflow.entity.mapper.DronesWorkflowMapper;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DronesWorkflowRepository implements PanacheRepository<DronesWorkflow> {
    @Inject
    DronesWorkflowMapper mapper;

     public List<DronesWorkflow> selectList(DronesWorkflowQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesWorkflow selectOne(DronesWorkflowQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("taskId", queryDto.getTaskId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<DronesWorkflow> selectPage(DronesWorkflowQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesWorkflow> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesWorkflow dto) {
        DronesWorkflow entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesWorkflowDto dto) {
        DronesWorkflow entity = this.findById(dto.getId());
        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByQeryDto(DronesWorkflowQueryDto queryDto, DronesWorkflowDto dto) {
        DronesWorkflow dronesWorkflow = this.selectOne(queryDto);
        mapper.updateEntityFromDto(dto, dronesWorkflow);
        dronesWorkflow.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                this.deleteById(id);
            }
        }
    }

}