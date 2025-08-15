package org.hzai.system.sysuser.service;

import java.util.List;

import org.hzai.system.sysuser.entity.SysUser;
import org.hzai.system.sysuser.entity.dto.SysUserQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;

public interface SysUserService {
   List<SysUser> listUsers();

   List<SysUser> listUsersByDto(SysUserQueryDto sysUserDto);

    R<String> authenticate(String username, String password);

   PageResult<SysUser> listUserPage(SysUserQueryDto dto, PageRequest pageRequest);

   Boolean registerUser(SysUser sysUser);
}
