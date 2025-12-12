package org.huazhi.system.sysrole.service;

import java.util.List;

import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface SysRoleService {
    List<SysRole> listRoles();

    List<SysRole> listRolesByDto(SysRoleQueryDto sysRoleDto);

    PageResult<SysRole> listRolePage(SysRoleQueryDto dto, PageRequest pageRequest);

    SysRole listRoleByDto(SysRoleQueryDto sysRoleQueryDto);

    Long registerRole(SysRole sysRole);
}
