package org.huazhi.system.syslog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.syslog.entity.SysLog;
import org.huazhi.system.syslog.entity.dto.SysLogQueryDto;
import org.huazhi.system.syslog.repository.SysLogRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysLogServiceImp implements SysLogService {
    @Inject
    SysLogRepository repository;
    @Override
    public List<SysLog> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysLog> listEntitysByDto(SysLogQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysLog> listPage(SysLogQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(SysLog entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

}