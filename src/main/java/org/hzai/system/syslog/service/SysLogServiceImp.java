package org.hzai.system.syslog.service;

import java.util.List;

import org.hzai.system.syslog.entity.SysLog;
import org.hzai.system.syslog.entity.dto.SysLogQueryDto;
import org.hzai.system.syslog.repository.SysLogRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

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
        repository.persist(entity);
        return true;
    }

}