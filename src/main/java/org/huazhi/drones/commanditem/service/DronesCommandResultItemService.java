package org.huazhi.drones.commanditem.service;

import java.util.List;

import org.huazhi.drones.commanditem.entity.DronesCommandResultItem;
import org.huazhi.drones.commanditem.entity.dto.DronesCommandResultItemDto;
import org.huazhi.drones.commanditem.entity.dto.DronesCommandResultItemQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesCommandResultItemService {
   List<DronesCommandResultItem> listEntitys();

   List<DronesCommandResultItem> listEntitysByDto(DronesCommandResultItemQueryDto queryDto);

   DronesCommandResultItem listOne(DronesCommandResultItemQueryDto dto);

   PageResult<DronesCommandResultItem> listPage(DronesCommandResultItemQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesCommandResultItem entity);

   void replaceById(DronesCommandResultItem entity);

   void replaceByDto(DronesCommandResultItemDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}