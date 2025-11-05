package org.hzai.drones.routelibrary.service;

import java.util.List;

import org.hzai.ai.common.SelectOption;
import org.hzai.drones.routelibrary.entity.DronesRouteLibrary;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesRouteLibraryService {
   List<DronesRouteLibrary> listEntitys();

   List<DronesRouteLibrary> listEntitysByDto(DronesRouteLibraryQueryDto sysOrgDto);

   DronesRouteLibrary listOne(DronesRouteLibraryQueryDto dto);

   PageResult<DronesRouteLibrary> listPage(DronesRouteLibraryQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesRouteLibrary entity);

   void replaceById(DronesRouteLibrary entity);

   void replaceByDto(DronesRouteLibraryDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   Object getRoute(Long modelId);

   List<SelectOption> getSelectOptions();

   List<Long> getRouteCount();

   List<Long> getRouteCountBefore();

   long count();
}