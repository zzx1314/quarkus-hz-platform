package org.huazhi.drones.business.task.entity.vo;

import java.util.Map;
import java.util.Set;


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


    public Set<String> getSuccessIds() {
        return successIds;
    }


    public void setSuccessIds(Set<String> successIds) {
        this.successIds = successIds;
    }


    public Set<String> getFailIds() {
        return failIds;
    }


    public void setFailIds(Set<String> failIds) {
        this.failIds = failIds;
    }


    public Map<String, String> getErrorInfoMap() {
        return errorInfoMap;
    }


    public void setErrorInfoMap(Map<String, String> errorInfoMap) {
        this.errorInfoMap = errorInfoMap;
    }

    
}
