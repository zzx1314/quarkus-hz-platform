package org.hzai.system.sysorg.service;

import java.util.List;

import org.hzai.system.sysorg.entity.SysOrg;
import org.hzai.system.sysorg.entity.dto.SysOrgQueryDto;
import org.hzai.system.sysorg.entity.dto.SysOrgTreeDto;
import org.hzai.system.sysorg.entity.vo.SysOrgVo;
import org.hzai.util.R;

public interface SysOrgService {
    
   List<SysOrg> listOrgs();

   List<SysOrg> listOrgsByDto(SysOrgQueryDto sysOrgDto);

   List<SysOrgTreeDto> listOrgTrees(SysOrgQueryDto dto);

   List<SysOrgVo> listAllOrgVo(SysOrgQueryDto queryDto);

   R<Object> registerOrg(SysOrgTreeDto sysOrg);

   R<Object> updateOrg(SysOrgTreeDto sysOrg);
}
