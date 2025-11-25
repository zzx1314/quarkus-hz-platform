package org.huazhi.drones.command.entity.webscoketdto.action;


import org.huazhi.drones.command.entity.webscoketdto.DronesRoute;

import lombok.Data;

@Data
public class DronesActionParam {
    /**
     * 悬停时间
     */
    private Integer timeSec;
    /**
     * 目标高度
     */
    private String targetAlt;

    private DronesActionParamEvent event;

    /**
     * 目标航点
     */
    private DronesRoute targetWp;

    /**
     * 循环次数
     */
    private Integer num;

    /**
     * 循环间隔
     */
    private Integer intervalSec;


    /**
     * service -type
     */
    private String type;

    /**
     * 失败原因
     */
    private String reason;
}
