package org.huazhi.drones.command.entity.webscoketdto.action.phone;

import lombok.Data;

@Data
public class DronesActionPhotoParam {
    private Integer num;

    private Integer intervalSec;

    private DronesActionPhotoParamEvent event;
}
