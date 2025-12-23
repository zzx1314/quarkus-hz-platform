package org.huazhi.drones.business.device.service;

import java.util.List;
import java.util.Map;

import org.huazhi.drones.business.device.entity.DronesDevice;
import org.huazhi.drones.business.device.entity.dto.DronesDeviceDto;
import org.huazhi.drones.business.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.common.SelectOption;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesDeviceService {
   List<DronesDevice> listEntitys();

   List<DronesDevice> listEntitysByDto(DronesDeviceQueryDto sysOrgDto);

   DronesDevice listOne(DronesDeviceQueryDto dto);

   DronesDevice listById(Long id);

   PageResult<DronesDevice> listPage(DronesDeviceQueryDto dto, PageRequest pageRequest);

   Long register(DronesDevice entity);

   void replaceById(DronesDevice entity);

   void replaceByDto(DronesDeviceDto dto);

   void replaceByQuery(DronesDeviceDto dto, DronesDeviceQueryDto queryDto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   void registerByDto(DronesDeviceDto deviceDto);

   List<SelectOption> getSelectOptions();

   List<Long> getDeviceCount();

   List<Long> getDeviceCountBefore();

   long count();

   List<Map<String, Object>> statisticsDeviceTypeNumber();

}