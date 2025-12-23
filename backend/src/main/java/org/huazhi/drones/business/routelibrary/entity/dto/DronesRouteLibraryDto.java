package org.huazhi.drones.business.routelibrary.entity.dto;

import java.util.List;

import org.huazhi.drones.business.routelibrary.routeitem.entity.DronesRouteItem;

import lombok.Data;

@Data
public class DronesRouteLibraryDto {
    private Long id;

    private String routeName;

    private String routeData;

    private String routeStatus;

    private Long modelId;

    /**
     * 描述
     */
    private String routeDescription;

    /**
     * 航点信息
     */
    private List<DronesRouteItem> routeItems;

}