package org.huazhi.system.sysmenu.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class SysMenuQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("ids")
    private List<Long> ids;

    @QueryParam("type")
    private Integer type;

    @QueryParam("parentId")
    private Long parentId;

    @QueryParam("roleCode")
    private String roleCode;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}