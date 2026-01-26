package org.huazhi.drones.business.routelibrary.entity.dto;

import java.util.List;

import org.huazhi.drones.business.routelibrary.routeitem.entity.DronesRouteItem;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteData() {
        return routeData;
    }

    public void setRouteData(String routeData) {
        this.routeData = routeData;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public List<DronesRouteItem> getRouteItems() {
        return routeItems;
    }

    public void setRouteItems(List<DronesRouteItem> routeItems) {
        this.routeItems = routeItems;
    }

    

}