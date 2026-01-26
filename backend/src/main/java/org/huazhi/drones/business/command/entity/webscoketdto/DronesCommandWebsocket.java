package org.huazhi.drones.business.command.entity.webscoketdto;

import java.util.List;

import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.route.DronesRoute;
import org.huazhi.drones.business.command.entity.webscoketdto.task.DronesTaskWebScoket;
import org.huazhi.drones.business.services.entity.vo.DronesServicesVo;


public class DronesCommandWebsocket {

    /*
    * 设备id
    */
    private String deviceId;
    /**
     * 类型
     */
    private String type;
    /**
     * 指令id
     */
    private String missionId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public List<DronesRoute> getRoute() {
        return route;
    }

    public void setRoute(List<DronesRoute> route) {
        this.route = route;
    }

    public List<DronesServicesVo> getServices() {
        return services;
    }

    public void setServices(List<DronesServicesVo> services) {
        this.services = services;
    }

    public List<DronesAction> getOnErrorActions() {
        return onErrorActions;
    }

    public void setOnErrorActions(List<DronesAction> onErrorActions) {
        this.onErrorActions = onErrorActions;
    }

    public List<DronesTaskWebScoket> getTasks() {
        return tasks;
    }

    public void setTasks(List<DronesTaskWebScoket> tasks) {
        this.tasks = tasks;
    }

    /**
     * 路线
     */
    private List<DronesRoute> route;

    /**
     * 服务列表
     */
    private List<DronesServicesVo> services;

    /**
     * 错误动作列表
     */
    private List<DronesAction> onErrorActions;

    /**
     * 任务列表
     */
    private List<DronesTaskWebScoket> tasks;
}
