package org.hzai.system.sysdict.entity.mapper;

import org.hzai.system.sysdict.entity.SysDict;
import org.hzai.system.sysdict.entity.dto.SysDictDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface SysDictMapper {
    SysDict toEntity(SysDictDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysDictDto dto, @MappingTarget SysDict entity);
}