package org.hzai.system.org.service;

import java.util.List;

import org.hzai.system.org.entity.SysOrg;
import org.hzai.system.org.entity.dto.SysOrgQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysOrgService {
    
    List<SysOrg> listOrgs();

   List<SysOrg> listOrgsByDto(SysOrgQueryDto sysOrgDto);

   PageResult<SysOrg> listOrgsPage(SysOrgQueryDto dto, PageRequest pageRequest);

   Boolean registerOrg(SysOrg sysOrg);
}
