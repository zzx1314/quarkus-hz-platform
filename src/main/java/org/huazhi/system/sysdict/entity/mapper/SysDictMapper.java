package org.huazhi.system.sysdict.entity.mapper;

import org.huazhi.system.sysdict.entity.SysDict;
import org.huazhi.system.sysdict.entity.dto.SysDictDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictMapper {
    SysDict toEntity(SysDictDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysDictDto dto, @MappingTarget SysDict entity);
}