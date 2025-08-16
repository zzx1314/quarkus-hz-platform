package org.hzai.system.sysdict.service;

import java.util.List;

import org.hzai.system.sysdict.entity.SysDict;
import org.hzai.system.sysdict.entity.dto.SysDictQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysDictService {
   List<SysDict> listEntitys();

   List<SysDict> listEntitysByDto(SysDictQueryDto sysOrgDto);

   PageResult<SysDict> listPage(SysDictQueryDto dto, PageRequest pageRequest);

   Boolean register(SysDict entity);
}