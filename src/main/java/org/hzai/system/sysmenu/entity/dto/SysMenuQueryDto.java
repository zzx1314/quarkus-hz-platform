package org.hzai.system.sysmenu.entity.dto;

import lombok.Data;

import java.util.List;

import jakarta.ws.rs.QueryParam;

@Data
public class SysMenuQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("ids")
    private List<Integer> ids;

    @QueryParam("type")
    private Integer type;

    @QueryParam("parentId")
    private Integer parentId;

    @QueryParam("roleCode")
    private String roleCode;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}