package org.hzai.system.sysuser.entity.mapper;

import org.hzai.system.sysuser.entity.SysUser;
import org.hzai.system.sysuser.entity.dto.SysUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface SysUserMapper {
    SysUser toEntity(SysUserDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysUserDto dto, @MappingTarget SysUser entity);
}

