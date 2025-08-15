package org.hzai.system.sysrole.entity.mapper;

import org.hzai.system.sysrole.entity.SysRole;
import org.hzai.system.sysrole.entity.dto.SysRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface SysRoleMapper {
    SysRole toEntity(SysRoleDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysRoleDto dto, @MappingTarget SysRole entity);
}
