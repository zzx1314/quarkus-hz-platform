package org.huazhi.system.syslog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.entity.SysLog;
import org.huazhi.system.syslog.entity.dto.SysLogQueryDto;
import org.huazhi.system.syslog.entity.mapper.SysLogMapper;
import org.huazhi.system.syslog.repository.SysLogRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysLogServiceImp implements SysLogService {
    @Inject
    SysLogRepository sysLogRepository;

    @Inject
    SysLogMapper sysLogMapper;

    @Override
    public List<SysLog> listEntitys() {
        return sysLogRepository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<SysLog> listEntitysByDto(SysLogQueryDto sysOrgDto) {
        return sysLogRepository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysLog> listPage(SysLogQueryDto dto, PageRequest pageRequest) {
        return sysLogRepository.selectPage(dto, pageRequest);
    }

    @Override
    public Long register(SysLog entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        sysLogRepository.persist(entity);
        return entity.getId();
    }

    @Override
    public Long registerByLogRecord(LogRecord logRecord) {
        SysLog sysLog = sysLogMapper.toEntityByLogRecord(logRecord);
        return register(sysLog);
    }

    @Override
    public List<SysLog> queryByBizNoAndType(String bizNo, String type) {
        return sysLogRepository.list("bizNo = ?1 and type = ?2", bizNo, type);
    }
}