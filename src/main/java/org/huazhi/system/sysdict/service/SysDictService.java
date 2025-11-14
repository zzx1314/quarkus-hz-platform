package org.huazhi.system.sysdict.service;

import java.util.List;

import org.huazhi.system.sysdict.entity.SysDict;
import org.huazhi.system.sysdict.entity.dto.SysDictQueryDto;
import org.huazhi.system.sysdictitem.entity.SysDictItem;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface SysDictService {
   List<SysDict> listEntitys();

   List<SysDict> listEntitysByDto(SysDictQueryDto sysOrgDto);

   PageResult<SysDict> listPage(SysDictQueryDto dto, PageRequest pageRequest);

   Boolean register(SysDict entity);

   List<SysDictItem> getItemByType(String string);
}