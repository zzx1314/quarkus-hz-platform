package org.huazhi.drones.services.entity.dto.deepsort;

import lombok.Data;

@Data
public class DeepsortEvent {
    private String start;

    private String stop;

    private String onTracked;

    private String onLost;
}
