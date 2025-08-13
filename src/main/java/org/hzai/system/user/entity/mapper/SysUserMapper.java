package org.hzai.system.user.entity.mapper;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface SysUserMapper {
    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    SysUser toEntity(SysUserDto dto);

    SysUserDto toDto(SysUser entity);
}

