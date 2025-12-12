package org.huazhi.system.sysorg.entity.mapper;

import org.huazhi.system.sysorg.entity.SysOrg;
import org.huazhi.system.sysorg.entity.dto.SysOrgDto;
import org.huazhi.system.sysorg.entity.dto.SysOrgTreeDto;
import org.huazhi.system.sysorg.entity.vo.SysOrgVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysOrgMapper {
    SysOrg toEntity(SysOrgDto dto);

    SysOrgVo toVo(SysOrg entity);

    SysOrg toEntity(SysOrgTreeDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysOrgDto dto, @MappingTarget SysOrg entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysOrgTreeDto dto, @MappingTarget SysOrg entity);
}
