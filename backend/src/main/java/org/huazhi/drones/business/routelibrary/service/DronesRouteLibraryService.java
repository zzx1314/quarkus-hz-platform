package org.huazhi.drones.business.routelibrary.service;

import java.util.List;

import org.huazhi.drones.business.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.business.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.huazhi.drones.business.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.common.SelectOption;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;

public interface DronesRouteLibraryService {
   List<DronesRouteLibrary> listEntitys();

   List<DronesRouteLibrary> listEntitysByDto(DronesRouteLibraryQueryDto sysOrgDto);

   DronesRouteLibrary listOne(DronesRouteLibraryQueryDto dto);

   PageResult<DronesRouteLibrary> listPage(DronesRouteLibraryQueryDto dto, PageRequest pageRequest);

   Long register(DronesRouteLibrary entity);

   void replaceById(DronesRouteLibrary entity);

   void replaceByDto(DronesRouteLibraryDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   R<Object> getRoute(Long modelId);

   List<SelectOption> getSelectOptions();

   List<SelectOption> getRouteOption(Long id);

   List<Long> getRouteCount();

   List<Long> getRouteCountBefore();

   long count();

   void saveRouteData(DronesRouteLibraryDto data);

   R<Object> startOrRoute(Long id, String status);

   DronesRouteLibrary listEntitysById(long routeId);
}