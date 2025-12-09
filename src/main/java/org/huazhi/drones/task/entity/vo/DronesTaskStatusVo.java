package org.huazhi.drones.task.entity.vo;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DronesTaskStatusVo {
    /**
     * 成功的id
     */
    private List<String> successIds;

    /**
     * 失败的id
     */
    private List<String> failIds;


    /**
     * 失败的信息
     */
    private Map<String, String> errorInfoMap;
}
