package org.hzai.system.user.entity.mapper;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface SysUserMapper {
    SysUser toEntity(SysUserDto dto);

    SysUserDto toDto(SysUser entity);
}

