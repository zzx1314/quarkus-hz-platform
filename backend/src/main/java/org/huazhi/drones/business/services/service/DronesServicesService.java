package org.huazhi.drones.business.services.service;

import java.util.List;
import java.util.Set;

import org.huazhi.drones.business.services.entity.DronesServices;
import org.huazhi.drones.business.services.entity.dto.DronesServicesDto;
import org.huazhi.drones.business.services.entity.dto.DronesServicesQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesServicesService {
   List<DronesServices> listEntitys();

   List<DronesServices> listEntitysByDto(DronesServicesQueryDto sysOrgDto);

   DronesServices listOne(DronesServicesQueryDto dto);

   PageResult<DronesServices> listPage(DronesServicesQueryDto dto, PageRequest pageRequest);

   Long register(DronesServices entity);

   void replaceById(DronesServices entity);

   void replaceByDto(DronesServicesDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   /**
    * 根据类型列表批量查询服务
    * 
    * @param types 服务类型集合
    * @return 服务列表
    */
   List<DronesServices> listByTypes(Set<String> types);
}
