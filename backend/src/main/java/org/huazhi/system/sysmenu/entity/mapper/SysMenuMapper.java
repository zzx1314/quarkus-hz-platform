package org.huazhi.system.sysmenu.entity.mapper;

import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuDto;
import org.huazhi.system.sysmenu.entity.vo.MenuVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuMapper {
    SysMenu toEntity(SysMenuDto dto);

    MenuVo toMenuVo(SysMenu sysMenu);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysMenuDto dto, @MappingTarget SysMenu entity);
}