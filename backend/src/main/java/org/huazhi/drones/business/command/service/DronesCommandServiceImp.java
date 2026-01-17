package org.huazhi.drones.business.command.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.huazhi.drones.business.command.entity.DronesCommand;
import org.huazhi.drones.business.command.entity.dto.DronesCommandDto;
import org.huazhi.drones.business.command.entity.dto.DronesCommandParam;
import org.huazhi.drones.business.command.entity.dto.DronesCommandQueryDto;
import org.huazhi.drones.business.command.entity.dto.DronesCommonCommand;
import org.huazhi.drones.business.command.entity.webscoketdto.DronesCommandWebsocket;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesActionParam;
import org.huazhi.drones.business.command.entity.webscoketdto.task.DronesTaskWebScoket;
import org.huazhi.drones.business.command.repository.DronesCommandRepository;
import org.huazhi.drones.business.device.entity.DronesDevice;
import org.huazhi.drones.business.device.service.DronesDeviceService;
import org.huazhi.drones.business.services.entity.DronesServices;
import org.huazhi.drones.business.services.service.DronesServicesService;
import org.huazhi.drones.business.workflow.service.DronesWorkflowService;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.util.IdUtil;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class DronesCommandServiceImp implements DronesCommandService {
    @Inject
    DronesCommandRepository repository;

    @Inject
    ConnectionManager connectionManager;

    @Inject
    DronesServicesService servicesService;

    @Inject
    DronesDeviceService deviceService;

    @Inject
    DronesWorkflowService workflowService;

    @Override
    public List<DronesCommand> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<DronesCommand> listEntitysByDto(DronesCommandQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesCommand listOne(DronesCommandQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesCommand> listPage(DronesCommandQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    @Transactional
    public Long register(DronesCommand entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return entity.getId();
    }

    @Override
    public void replaceById(DronesCommand entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesCommandDto dto) {
        repository.updateByDto(dto);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void removeByIds(List<Long> ids) {
        repository.deleteByIds(ids);
    }

    /**
     * 下发服务指令
     */
    @Override
    public Boolean serverCommand(DronesCommandParam param) {
        DronesDevice dronesDevice = deviceService.listById(param.getDeviceId());
        DronesCommandWebsocket commandWebsocket = new DronesCommandWebsocket();
        commandWebsocket.setDeviceId(dronesDevice.getDeviceId());
        commandWebsocket.setType("toros");
        // 任务信息
        List<DronesTaskWebScoket> tasks = new ArrayList<>();

        DronesTaskWebScoket taskInfo = new DronesTaskWebScoket();
        taskInfo.setTaskId("node_"+ IdUtil.simpleUUID());
        String initId = "init_" + IdUtil.simpleUUID();
        taskInfo.setEvent(initId);
        // 补充action
        List<DronesAction> actions = new ArrayList<>();
        for (String type : param.getType()) { 
            DronesAction action = new DronesAction();
            action.setActionId("node_"+ IdUtil.simpleUUID());
            action.setAfter(initId);
            action.setTimeoutSec(200);
            if ("yolo".equals(type)) {
                // yolo信息
                if ("start".equals(param.getParam())) {
                    action.setType("SERVICE_START_YOLO");
                } else if ("stop".equals(param.getParam())) {
                    action.setType("SERVICE_STOP_YOLO");
                }
                this.getYoloAction(action);
            }  
            if ("rtsp".equals(type)) {
                // rtsp信息
                if ("start".equals(param.getParam())) {
                    action.setType("SERVICE_START_RTSP");
                } else if ("stop".equals(param.getParam())) {
                    action.setType("SERVICE_STOP_RTSP");
                }
                this.getRtspAction(action);
            }
            actions.add(action);
        }
        taskInfo.setActions(actions);

        tasks.add(taskInfo);

        commandWebsocket.setTasks(tasks);
        log.info("下发服务命令：{}", JsonUtil.toJson(commandWebsocket));
        // 任务id为空，临时命令不需要修改任务信息
        connectionManager.sendMessageByDeviceId(commandWebsocket.getDeviceId(),
         commandWebsocket, null, "SERVER");
        return true;
    }

    /**
     * 补充yolo参数
     */
    private void getYoloAction(DronesAction action) {
        Set<String> types = new HashSet<>();
        types.add("YOLO");
        List<DronesServices> services =  servicesService.listByTypes(types);
        DronesActionParam param = JsonUtil.fromJson(services.getFirst().getParams(), DronesActionParam.class);
        action.setParams(param);
    }


    /**
     * 补充rtsp参数
     */
    private void getRtspAction(DronesAction action) {
        Set<String> types = new HashSet<>();
        types.add("RTSP");
        List<DronesServices> services =  servicesService.listByTypes(types);
        DronesActionParam param = JsonUtil.fromJson(services.getFirst().getParams(), DronesActionParam.class);
        action.setParams(param);
    }

    /**
     * 下发普通指令
     *  type: track目标跟踪，deepsort_start，deepsort_stop
     */
    @Override
    public Boolean commonCommand(DronesCommonCommand param) {
        log.info("下发普通指令" + JsonUtil.toJson(param));
        DronesCommand command = new DronesCommand();
        command.setCommandType("server");
        command.setCommandParams(JsonUtil.toJson(param));
        this.register(command);
        param.setCommandId(command.getId());

       /*  DronesWorkflow dronesWorkflow = workflowService.getWorkflow(param.getTaskId());
        JsonNode jsonNode = JsonUtil.toJsonObject(dronesWorkflow.getCommandJsonString());
        param.setDeviceId(jsonNode.path("deviceId").asText()); */
        connectionManager.sendCommonCommand(param.getDeviceId(), param);
        return true;
    }

    @Override
    public DronesCommand listById(Long id) {
        DronesCommand command = repository.findById(id);
        return command;
    }

    
}