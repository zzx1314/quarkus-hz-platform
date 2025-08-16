package org.hzai.system.syslog.service;

import java.util.List;

import org.hzai.system.syslog.entity.SysLog;
import org.hzai.system.syslog.entity.dto.SysLogQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysLogService {
   List<SysLog> listEntitys();

   List<SysLog> listEntitysByDto(SysLogQueryDto sysOrgDto);

   PageResult<SysLog> listPage(SysLogQueryDto dto, PageRequest pageRequest);

   Boolean register(SysLog entity);
}