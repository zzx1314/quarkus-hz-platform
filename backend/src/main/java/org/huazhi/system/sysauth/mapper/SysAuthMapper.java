package org.huazhi.system.sysauth.mapper;

import org.huazhi.system.sysauth.vo.SysAuthTitleVo;
import org.huazhi.system.sysmenu.entity.SysMenu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface SysAuthMapper {
    SysAuthTitleVo toSysAuthTitleVo(SysMenu sysAuthMenuVo);
}
