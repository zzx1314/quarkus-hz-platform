package org.huazhi.drones.routeitem.service;

import java.util.List;

import org.huazhi.drones.routeitem.entity.DronesRouteItem;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemDto;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesRouteItemService {
   List<DronesRouteItem> listEntitys();

   List<DronesRouteItem> listEntitysByDto(DronesRouteItemQueryDto queryDto);

   DronesRouteItem listOne(DronesRouteItemQueryDto dto);

   DronesRouteItem listById(Long id);

   PageResult<DronesRouteItem> listPage(DronesRouteItemQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesRouteItem entity);

   void replaceById(DronesRouteItem entity);

   void replaceByDto(DronesRouteItemDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   void removeByRouteLibraryId(Long id);
}