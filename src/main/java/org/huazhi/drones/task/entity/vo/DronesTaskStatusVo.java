package org.huazhi.drones.task.entity.vo;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class DronesTaskStatusVo {
    /**
     * 成功的id
     */
    private Set<String> successIds;

    /**
     * 失败的id
     */
    private Set<String> failIds;


    /**
     * 失败的信息
     */
    private Map<String, String> errorInfoMap;
}
