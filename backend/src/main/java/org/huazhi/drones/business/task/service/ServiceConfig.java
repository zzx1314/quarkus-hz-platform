package org.huazhi.drones.business.task.service;


public class ServiceConfig {
    String serviceType;
    String completeEventFrom;

    ServiceConfig(String serviceType, String completeEventFrom) {
        this.serviceType = serviceType;
        this.completeEventFrom = completeEventFrom;
    }
}
