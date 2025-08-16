package org.hzai.system.sysdictitem.service;

import java.util.List;

import org.hzai.system.sysdictitem.entity.SysDictItem;
import org.hzai.system.sysdictitem.entity.dto.SysDictItemQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysDictItemService {
   List<SysDictItem> listEntitys();

   List<SysDictItem> listEntitysByDto(SysDictItemQueryDto sysOrgDto);

   PageResult<SysDictItem> listPage(SysDictItemQueryDto dto, PageRequest pageRequest);

   Boolean register(SysDictItem entity);
}