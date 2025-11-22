package org.huazhi.drones.services.service;

import java.util.List;

import org.huazhi.drones.services.entity.DronesServices;
import org.huazhi.drones.services.entity.dto.DronesServicesDto;
import org.huazhi.drones.services.entity.dto.DronesServicesQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesServicesService {
   List<DronesServices> listEntitys();

   List<DronesServices> listEntitysByDto(DronesServicesQueryDto sysOrgDto);

   DronesServices listOne(DronesServicesQueryDto dto);

   PageResult<DronesServices> listPage(DronesServicesQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesServices entity);

   void replaceById(DronesServices entity);

   void replaceByDto(DronesServicesDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}