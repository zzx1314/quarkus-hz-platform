package org.huazhi.system.sysorg.service;

import java.util.List;

import org.huazhi.system.sysorg.entity.SysOrg;
import org.huazhi.system.sysorg.entity.dto.SysOrgQueryDto;
import org.huazhi.system.sysorg.entity.dto.SysOrgTreeDto;
import org.huazhi.system.sysorg.entity.vo.SysOrgVo;
import org.huazhi.util.R;

public interface SysOrgService {

   List<SysOrg> listOrgs();

   List<SysOrg> listOrgsByDto(SysOrgQueryDto sysOrgDto);

   List<SysOrgTreeDto> listOrgTrees(SysOrgQueryDto dto);

   List<SysOrgVo> listAllOrgVo(SysOrgQueryDto queryDto);

   R<Void> registerOrg(SysOrgTreeDto sysOrg);

   R<Object> updateOrg(SysOrgTreeDto sysOrg);
}
