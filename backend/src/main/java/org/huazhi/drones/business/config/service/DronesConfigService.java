package org.huazhi.drones.business.config.service;

import java.util.List;

import org.huazhi.drones.business.config.entity.DronesConfig;
import org.huazhi.drones.business.config.entity.dto.DronesConfigDto;
import org.huazhi.drones.business.config.entity.dto.DronesConfigQueryDto;
import org.huazhi.drones.common.SelectOption;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesConfigService {
   List<DronesConfig> listEntitys();

   List<DronesConfig> listEntitysByDto(DronesConfigQueryDto sysOrgDto);

   DronesConfig listOne(DronesConfigQueryDto dto);

   PageResult<DronesConfig> listPage(DronesConfigQueryDto dto, PageRequest pageRequest);

   Long register(DronesConfig entity);

   void replaceById(DronesConfig entity);

   void replaceByDto(DronesConfigDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   List<SelectOption> getSelectOptions();
}