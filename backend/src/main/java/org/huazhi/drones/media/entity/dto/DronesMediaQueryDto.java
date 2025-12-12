package org.huazhi.drones.media.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class DronesMediaQueryDto {
    @QueryParam("id")
    private Long id;

    /**
     * 媒体文件名称
     */
    @QueryParam("mediaName")
    private String mediaName;

    /**
     * 媒体文件类型
     */
    @QueryParam("mediaType")
    private String mediaType;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}