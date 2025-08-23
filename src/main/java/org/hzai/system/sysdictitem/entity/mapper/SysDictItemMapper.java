package org.hzai.system.sysdictitem.entity.mapper;

import org.hzai.system.sysdictitem.entity.SysDictItem;
import org.hzai.system.sysdictitem.entity.dto.SysDictItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictItemMapper {
    SysDictItem toEntity(SysDictItemDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysDictItemDto dto, @MappingTarget SysDictItem entity);
}