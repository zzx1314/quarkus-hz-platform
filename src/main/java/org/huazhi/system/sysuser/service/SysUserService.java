package org.huazhi.system.sysuser.service;

import java.util.List;

import org.huazhi.system.sysuser.entity.SysUser;
import org.huazhi.system.sysuser.entity.dto.SysUserDto;
import org.huazhi.system.sysuser.entity.dto.SysUserQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;

public interface SysUserService {
   List<SysUser> listUsers();

   List<SysUser> listUsersByDto(SysUserQueryDto sysUserDto);

   PageResult<SysUser> listUserPage(SysUserQueryDto dto, PageRequest pageRequest);

   SysUser listOne(SysUserQueryDto queryDto);

   R<SysUserDto> authenticate(String username, String password);

   Boolean registerUser(SysUser sysUser);
}
