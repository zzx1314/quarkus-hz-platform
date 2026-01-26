package org.huazhi.drones.business.routelibrary.entity.vo;


public class DronesRouteLibraryVo {

    private Long id;

    /**
     * 航线名称
     */
    private String routeName;

    /**
     * 路线类型
     */
    private String routeType;

    /**
     * 路线状态
     */
    private String routeStatus;

    /**
     * 关联模型的id
     */
    private long modelId;

    /**
     * 路线数据
     */
    private String routeData;

    /**
     * 设备id
     */
    private Long deviceId;

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

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getRouteData() {
        return routeData;
    }

    public void setRouteData(String routeData) {
        this.routeData = routeData;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    
}