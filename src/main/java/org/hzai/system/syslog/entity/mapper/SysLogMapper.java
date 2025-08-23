package org.hzai.system.syslog.entity.mapper;

import org.hzai.system.syslog.entity.SysLog;
import org.hzai.system.syslog.entity.dto.SysLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysLogMapper {
    SysLog toEntity(SysLogDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysLogDto dto, @MappingTarget SysLog entity);
}