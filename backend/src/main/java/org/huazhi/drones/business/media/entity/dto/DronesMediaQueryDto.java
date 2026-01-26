package org.huazhi.drones.business.media.entity.dto;

import jakarta.ws.rs.QueryParam;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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

    
}