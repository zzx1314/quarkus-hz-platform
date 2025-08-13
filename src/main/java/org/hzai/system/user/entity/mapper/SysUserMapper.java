package org.hzai.system.user.entity.mapper;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface SysUserMapper {
    SysUser toEntity(SysUserDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysUserDto dto, @MappingTarget SysUser entity);
}

