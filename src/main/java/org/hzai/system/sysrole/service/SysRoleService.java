package org.hzai.system.sysrole.service;

import java.util.List;

import org.hzai.system.sysrole.entity.SysRole;
import org.hzai.system.sysrole.entity.dto.SysRoleQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysRoleService {
    List<SysRole> listRoles();

   List<SysRole> listRolesByDto(SysRoleQueryDto sysRoleDto);

   PageResult<SysRole> listRolePage(SysRoleQueryDto dto, PageRequest pageRequest);

   Boolean registerRole(SysRole sysRole);
}
