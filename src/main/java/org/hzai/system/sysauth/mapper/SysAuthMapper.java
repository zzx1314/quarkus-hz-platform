package org.hzai.system.sysauth.mapper;

import org.hzai.system.sysauth.vo.SysAuthTitleVo;
import org.hzai.system.sysmenu.entity.SysMenu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface SysAuthMapper {
    SysAuthTitleVo toSysAuthTitleVo(SysMenu sysAuthMenuVo);
}
