package org.huazhi.drones.routelibrary.entity.dto;

import java.util.List;

import org.huazhi.drones.routeitem.entity.DronesRouteItem;

import lombok.Data;

@Data
public class DronesRouteLibraryDto {
    private Long id;

    private String routeData;

    private String routeStatus;

    private Long modelId;

    /**
     * 起点坐标
     */
    private String startCoordinates;

    /**
     * 终点坐标
     */
    private String endCoordinates;

    /**
     * 描述
     */
    private String routeDescription;

    /**
     * 航点信息
     */
    private List<DronesRouteItem> routeItems;

}