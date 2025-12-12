package org.huazhi.drones.task.service;

import lombok.Data;

@Data
public class ServiceConfig {
    String serviceType;
    String completeEventFrom;

    ServiceConfig(String serviceType, String completeEventFrom) {
        this.serviceType = serviceType;
        this.completeEventFrom = completeEventFrom;
    }
}
