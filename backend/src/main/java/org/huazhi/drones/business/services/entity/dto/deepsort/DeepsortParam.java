package org.huazhi.drones.business.services.entity.dto.deepsort;


public class DeepsortParam {
    private Integer max_age;

    private DeepsortEvent event;

    public Integer getMax_age() {
        return max_age;
    }

    public void setMax_age(Integer max_age) {
        this.max_age = max_age;
    }

    public DeepsortEvent getEvent() {
        return event;
    }

    public void setEvent(DeepsortEvent event) {
        this.event = event;
    }
}
