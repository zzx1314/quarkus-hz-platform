package org.hzai.drones.routelibrary.entity.dto;

import lombok.Data;

@Data
public class DronesRouteLibraryDto {
    private Long id;

    private String routeData;

    private String routeStatus;

    /**
     * 起点坐标
     */
    private String startCoordinates;

    /**
     * 终点坐标
     */
    private String endCoordinates;

}