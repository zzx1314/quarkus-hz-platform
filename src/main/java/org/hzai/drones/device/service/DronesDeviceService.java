package org.hzai.drones.device.service;

import java.util.List;

import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesDeviceService {
   List<DronesDevice> listEntitys();

   List<DronesDevice> listEntitysByDto(DronesDeviceQueryDto sysOrgDto);

   DronesDevice listOne(DronesDeviceQueryDto dto);

   PageResult<DronesDevice> listPage(DronesDeviceQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesDevice entity);

   void replaceById(DronesDevice entity);

   void replaceByDto(DronesDeviceDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}