package org.huazhi.drones.business.media.entity.dto;


public class DronesMediaDto {
    private Long id;

    /**
     * 媒体文件名称
     */
    private String mediaName;

    /**
     * 媒体文件类型
     */
    private String mediaType;

    /**
     * 媒体文件路径
     */
    private String mediaPath;

    /**
     * 媒体文件大小（字节）
     */
    private Long mediaSize;

    /**
     * 媒体文件描述
     */
    private String remarks;

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

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public Long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(Long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
}