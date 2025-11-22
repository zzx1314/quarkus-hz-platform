package org.huazhi.drones.command.entity.webscoketdto.action.phone;

import lombok.Data;

@Data
public class DronesActionPhoto {
    private String actionId;
    private String type = "PHOTO";
    private DronesActionPhotoParam param;
    private String after;
    private Integer timeoutSec;
}
