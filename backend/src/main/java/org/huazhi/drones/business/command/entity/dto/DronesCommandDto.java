package org.huazhi.drones.business.command.entity.dto;

import lombok.Data;

@Data
public class DronesCommandDto {
    private Long id;

    private String status;

    private String commandParams;

    private String returnValue;

}