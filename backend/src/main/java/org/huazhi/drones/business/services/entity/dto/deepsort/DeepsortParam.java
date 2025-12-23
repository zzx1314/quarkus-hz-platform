package org.huazhi.drones.business.services.entity.dto.deepsort;

import lombok.Data;

@Data
public class DeepsortParam {
    private Integer max_age;

    private DeepsortEvent event;
}
