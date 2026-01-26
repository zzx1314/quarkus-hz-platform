package org.huazhi.drones.business.command.entity.webscoketdto.route;


public class DronesRoute {
    /**
     * 航点id
     */
    private long wpId;

    /**
     * 经度
     */
    private Double lat;

    /**
     * 纬度
     */
    private Double lon;

    /**
     * 高度
     */
    private Double alt;

    public long getWpId() {
        return wpId;
    }

    public void setWpId(long wpId) {
        this.wpId = wpId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getAlt() {
        return alt;
    }

    public void setAlt(Double alt) {
        this.alt = alt;
    }
}
