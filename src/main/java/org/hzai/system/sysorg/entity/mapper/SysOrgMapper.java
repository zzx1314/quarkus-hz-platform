package org.hzai.system.sysorg.entity.mapper;

import org.hzai.system.sysorg.entity.SysOrg;
import org.hzai.system.sysorg.entity.dto.SysOrgDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface SysOrgMapper {
    SysOrg toEntity(SysOrgDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysOrgDto dto, @MappingTarget SysOrg entity);
}
