package org.huazhi.system.syslog.service;

import java.util.List;

import org.huazhi.system.syslog.entity.SysLog;
import org.huazhi.system.syslog.entity.dto.SysLogQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface SysLogService {
   List<SysLog> listEntitys();

   List<SysLog> listEntitysByDto(SysLogQueryDto sysOrgDto);

   PageResult<SysLog> listPage(SysLogQueryDto dto, PageRequest pageRequest);

   Long register(SysLog entity);
}