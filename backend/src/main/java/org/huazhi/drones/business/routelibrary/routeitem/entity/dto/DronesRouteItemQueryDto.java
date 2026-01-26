package org.huazhi.drones.business.routelibrary.routeitem.entity.dto;

import jakarta.ws.rs.QueryParam;


public class DronesRouteItemQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("routeLibraryId")
    private Long routeLibraryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getRouteLibraryId() {
        return routeLibraryId;
    }

    public void setRouteLibraryId(Long routeLibraryId) {
        this.routeLibraryId = routeLibraryId;
    }

    

}