package org.huazhi.system.sysdictitem.service;

import java.util.List;

import org.huazhi.system.sysdictitem.entity.SysDictItem;
import org.huazhi.system.sysdictitem.entity.dto.SysDictItemQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface SysDictItemService {
   List<SysDictItem> listEntitys();

   List<SysDictItem> listEntitysByDto(SysDictItemQueryDto sysOrgDto);

   PageResult<SysDictItem> listPage(SysDictItemQueryDto dto, PageRequest pageRequest);

   Long register(SysDictItem entity);

   SysDictItem getOneByType(String type);
}