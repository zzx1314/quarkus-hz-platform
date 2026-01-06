package org.huazhi.drones.business.command.entity.webscoketdto.route;

import lombok.Data;

@Data
public class DronesRoute {
    /**
     * 航点id
     */
    private long wpId;

    /**
     * 经度
     */
    private Double lat;

    /**
     * 纬度
     */
    private Double lon;

    /**
     * 高度
     */
    private Double alt;
}
