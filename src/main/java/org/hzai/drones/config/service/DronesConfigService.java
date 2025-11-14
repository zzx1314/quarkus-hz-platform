package org.hzai.drones.config.service;

import java.util.List;

import org.hzai.drones.common.SelectOption;
import org.hzai.drones.config.entity.DronesConfig;
import org.hzai.drones.config.entity.dto.DronesConfigDto;
import org.hzai.drones.config.entity.dto.DronesConfigQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesConfigService {
   List<DronesConfig> listEntitys();

   List<DronesConfig> listEntitysByDto(DronesConfigQueryDto sysOrgDto);

   DronesConfig listOne(DronesConfigQueryDto dto);

   PageResult<DronesConfig> listPage(DronesConfigQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesConfig entity);

   void replaceById(DronesConfig entity);

   void replaceByDto(DronesConfigDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   List<SelectOption> getSelectOptions();
}