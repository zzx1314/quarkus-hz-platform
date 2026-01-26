package org.huazhi.drones.business.command.entity.dto;


public class DronesCommandDto {
    private Long id;

    private String status;

    private String commandParams;

    private String returnValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(String commandParams) {
        this.commandParams = commandParams;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

}