package org.huazhi.system.sysrole.entity.mapper;

import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRoleMapper {
    SysRole toEntity(SysRoleDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysRoleDto dto, @MappingTarget SysRole entity);
}
