package org.hzai.system.user.service;

import java.util.List;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface UserService {
   List<SysUser> listUsers();

   List<SysUser> listUsersByDto(SysUserDto sysUserDto);

   boolean authenticate(String username, String password);

   PageResult<SysUser> listUserPage(SysUserDto dto, PageRequest pageRequest);

   Boolean registerUser(SysUser sysUserDto);
}
