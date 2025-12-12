package org.huazhi.drones.command.service;

import java.util.List;

import org.huazhi.drones.command.entity.DronesCommand;
import org.huazhi.drones.command.entity.dto.DronesCommandDto;
import org.huazhi.drones.command.entity.dto.DronesCommandQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesCommandService {
   List<DronesCommand> listEntitys();

   List<DronesCommand> listEntitysByDto(DronesCommandQueryDto queryDto);

   DronesCommand listOne(DronesCommandQueryDto dto);

   PageResult<DronesCommand> listPage(DronesCommandQueryDto dto, PageRequest pageRequest);

   Long register(DronesCommand entity);

   void replaceById(DronesCommand entity);

   void replaceByDto(DronesCommandDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}