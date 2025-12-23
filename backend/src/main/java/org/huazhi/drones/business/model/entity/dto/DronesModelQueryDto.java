package org.huazhi.drones.business.model.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class DronesModelQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    /**
     * 模型名称
     */
    @QueryParam("modelName")
    private String modelName;

    /**
     * 模型类型
     */
    @QueryParam("modelType")
    private String modelType;
}