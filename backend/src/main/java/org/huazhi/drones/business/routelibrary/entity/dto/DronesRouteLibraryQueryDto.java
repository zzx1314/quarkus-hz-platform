package org.huazhi.drones.business.routelibrary.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class DronesRouteLibraryQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    /**
     * 航线名称
     */
    @QueryParam("routeName")
    private String routeName;

    /**
     * 路线类型
     */
    @QueryParam("routeType")
    private String routeType;

    /**
     * 路线状态
     */
    @QueryParam("routeStatus")
    private String routeStatus;
}