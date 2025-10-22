package org.hzai.drones.model.service;

import java.util.List;

import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelDto;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesModelService {
   List<DronesModel> listEntitys();

   List<DronesModel> listEntitysByDto(DronesModelQueryDto sysOrgDto);

   DronesModel listOne(DronesModelQueryDto dto);

   PageResult<DronesModel> listPage(DronesModelQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesModel entity);

   void replaceById(DronesModel entity);

   void replaceByDto(DronesModelDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}