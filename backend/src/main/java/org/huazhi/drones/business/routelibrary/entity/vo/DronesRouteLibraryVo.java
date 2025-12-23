package org.huazhi.drones.business.routelibrary.entity.vo;

import lombok.Data;

@Data
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
}