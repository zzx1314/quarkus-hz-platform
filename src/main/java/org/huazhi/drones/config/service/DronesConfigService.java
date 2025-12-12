package org.huazhi.drones.config.service;

import java.util.List;

import org.huazhi.drones.common.SelectOption;
import org.huazhi.drones.config.entity.DronesConfig;
import org.huazhi.drones.config.entity.dto.DronesConfigDto;
import org.huazhi.drones.config.entity.dto.DronesConfigQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesConfigService extends BaseService<DronesConfig, DronesConfigDto, DronesConfigQueryDto> {
    Long register(DronesConfig entity);
}