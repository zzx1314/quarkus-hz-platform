package org.huazhi.drones.task.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.huazhi.drones.command.entity.webscoketdto.DronesCommandWebsocketV1;
import org.huazhi.drones.command.entity.webscoketdto.DronesTaskWebScoket;
import org.huazhi.drones.command.entity.webscoketdto.action.gotow.DronesActionGoto;
import org.huazhi.drones.command.entity.webscoketdto.action.hover.DronesActionHover;
import org.huazhi.drones.command.entity.webscoketdto.action.phone.DronesActionPhoto;
import org.huazhi.drones.command.entity.webscoketdto.action.service.DronesActionService;
import org.huazhi.drones.command.entity.webscoketdto.action.takeoff.DronesActionTakeOff;
import org.huazhi.drones.config.service.DronesConfigService;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.model.service.DronesModelService;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.drones.task.repository.DronesTaskRepository;
import org.huazhi.drones.util.DateUtil;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.drones.workflow.entity.NodeEntity;
import org.huazhi.drones.workflow.entity.NodeEntityData;
import org.huazhi.drones.workflow.service.DronesWorkflowService;
import org.huazhi.drones.workflow.vo.DronesWorkflowVo;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;


import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class DronesTaskServiceImp implements DronesTaskService {
    @Inject
    DronesTaskRepository repository;

    @Inject
    DronesWorkflowService workflowService;

    @Inject
    ConnectionManager connectionManager;

    @Inject
    DronesDeviceService deviceService;

    @Inject
    DronesModelService modelService;

    @Inject
    DronesConfigService configService;

    @Inject
    DronesRouteLibraryService routeLibraryService;

    @Override
    public List<DronesTask> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<DronesTask> listEntitysByDto(DronesTaskQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesTask listOne(DronesTaskQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesTask> listPage(DronesTaskQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesTask entity) {
        entity.setIsDeleted(0);
        entity.setTaskStatus("未开始");
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesTask entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesTaskDto dto) {
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

    @Override
    public List<Long> getTaskCount() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getDronesTaskCountByDay();

        List<String> lastSevenDays = DateUtil.getLastSevenDays();

        if (!modelCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : modelCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }
        return data;
    }

    @Override
    public List<Long> getTaskCountBefore() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getDronesTaskCountByDay();

        List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

        if (!modelCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : modelCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }

        return data;
    }

    @Override
    public long count() {
        return repository.count("isDeleted = ?1", 0);
    }

    /**
     * 启动任务
     * 
     * @param id
     */
    @Override
    public void startTask(Long id) {
        DronesTask task = repository.findById(id);
        task.setTaskStatus("进行中");
        repository.updateById(task);
        // 获取任务流程图
        DronesWorkflowVo workflowGraph = workflowService.getWorkflowGraph(id);
        log.info("任务流程图：{}", JsonUtil.toJson(workflowGraph));
        // 获取任务节点
        List<NodeEntity> taskNodes = findTasknodes(workflowGraph);
        DronesCommandWebsocketV1 commandWebsocket = new DronesCommandWebsocketV1();

        List<DronesTaskWebScoket> tasks = new ArrayList<>();
        Long deviceId = taskNodes.get(0).getData().getDeviceId();
        DronesDevice device = deviceService.listById(deviceId);
        for (int i = 0; i < taskNodes.size(); i++) { 
            // 循环每一个步骤
            NodeEntity taskNode = taskNodes.get(i);
            commandWebsocket.setType("command");
            // 构建任务步骤
            DronesTaskWebScoket taskInfo = new DronesTaskWebScoket();
            taskInfo.setTaskId(i + 1 + "");
            taskInfo.setFromWp(taskNode.getData().getTaskInfo().getFromWp());
            taskInfo.setToWp(taskNode.getData().getTaskInfo().getToWp());
            taskInfo.setEvent(taskNode.getData().getTaskInfo().getEvent());
            // 寻找action
            List<Object> actions =  findAction(workflowGraph, taskNode);
            taskInfo.setActions(actions);
            tasks.add(taskInfo);
        }
        commandWebsocket.setTasks(tasks);
        log.info("发送指令信息:"+ JsonUtil.toJson(commandWebsocket));
        connectionManager.sendMessageByDeviceId(device.getDeviceId(), commandWebsocket);
    }

    private List<Object> findAction(DronesWorkflowVo workflowGraph, NodeEntity taskNode) {
        // key位任务id，vaule是所有的action
       List<Object> actions = new ArrayList<>();
       Map<String, List<String>> edgeMap = workflowGraph.getEdgeMap();
       List<String> targIds = edgeMap.get(taskNode.getId());
       // 过滤出关联的action
       List<NodeEntity> actionNodeList = new ArrayList<>();
       for (String targId : targIds) {
           NodeEntity targNode = workflowGraph.getNodeMap().get(targId);
           if (targNode == null) {
               throw new RuntimeException("nodeMap 缺少节点：" + targId);
           }
           if ("action".equals(targNode.getType())) {
               actionNodeList.add(targNode);
           }
       }
       String initEvent = taskNode.getData().getTaskInfo().getEvent();
       // 第一梯队的action
       List<NodeEntity> nextActionNodeList = new ArrayList<>();
       // 补充action的after关系
       if (actionNodeList.size() > 0) {
        for(NodeEntity actionNode : actionNodeList) {
            NodeEntityData actionNodeData = actionNode.getData();
            if (actionNodeData.getActionType().equals("GOTO")) {
                DronesActionGoto gotoAction = actionNodeData.getGotoAction();
                gotoAction.setAfter(initEvent);
                actions.add(gotoAction);
                nextActionNodeList.add(actionNode);
            } else if (actionNodeData.getActionType().equals("HOVER")) { 
                DronesActionHover hoverAction = actionNodeData.getHoverAction();
                hoverAction.setAfter(initEvent);
                actions.add(hoverAction);
                nextActionNodeList.add(actionNode);
            } else if (actionNodeData.getActionType().equals("PHOTO")) {
                DronesActionPhoto photoAction = actionNodeData.getPhotoAction();
                photoAction.setAfter(initEvent);
                actions.add(photoAction);
                nextActionNodeList.add(actionNode);
            } else if (actionNodeData.getActionType().equals("SERVICE_START")) {
                DronesActionService startService =actionNodeData.getStartService();
                startService.setType("SERVICE_START");
                startService.setAfter(initEvent);
                actions.add(startService);
                nextActionNodeList.add(actionNode);
            } else if (actionNode.getData().getActionType().equals("SERVICE_STOP")) {
                DronesActionService stopService = actionNodeData.getStopService();
                stopService.setType("SERVICE_STOP");
                stopService.setAfter(initEvent);
                actions.add(stopService);
                nextActionNodeList.add(actionNode);
            } else if (actionNode.getData().getActionType().equals("TAKE_OFF")) {
                DronesActionTakeOff takeOffAction = actionNodeData.getTakeOffAction();
                takeOffAction.setAfter(initEvent);
                actions.add(takeOffAction);
                nextActionNodeList.add(actionNode);
            }
        }
       }
       if (nextActionNodeList.size() > 0) {
        // 寻找后面串联的action
        for(NodeEntity nextActionNode : nextActionNodeList) {
            addNextAction(workflowGraph, nextActionNode, actions);
        }
       }
       return actions;
    }

    private void addNextAction(DronesWorkflowVo workflowVo, NodeEntity currentNode,  List<Object> actions) {
       // 防止循环
        Set<String> visited = new HashSet<>();
        dfsActions(workflowVo, currentNode, actions, visited);
    }

    private void dfsActions(DronesWorkflowVo workflowVo, NodeEntity currentNode,  List<Object> actions,  Set<String> visited) {
        // 如果已经遍历过该节点，避免循环引用导致死循环
        if (!visited.add(currentNode.getId())) {
            return;
        }
        // 获取 next 节点
        List<String> nextIds = workflowVo.getEdgeMap().get(currentNode.getId());
        if (nextIds == null || nextIds.isEmpty()) {
            return; // 到达终点
        }
        // 多个分叉：深度优先递归遍历每一个 next 节点
        for (String nextId : nextIds) {
            NodeEntity nextNode = workflowVo.getNodeMap().get(nextId);
            if (nextNode != null) {
                Object action = getActionInfo(nextNode, currentNode.getData().getTaskInfo().getEvent());
                actions.add(action);
                dfsActions(workflowVo, nextNode, actions, visited);
            }
        }
    }

    /**
     * 获取action信息
     */
    private Object getActionInfo(NodeEntity nextNode, String after){
        NodeEntityData nextNodeData = nextNode.getData();
        if (nextNodeData.getActionType().equals("GOTO")) {
            DronesActionGoto gotoAction = nextNodeData.getGotoAction();
            gotoAction.setAfter(after);
            return gotoAction;
        } else if (nextNodeData.getActionType().equals("HOVER")) { 
            DronesActionHover hoverAction = nextNodeData.getHoverAction();
            hoverAction.setAfter(after);
            return hoverAction;
        } else if (nextNodeData.getActionType().equals("PHOTO")) {
            DronesActionPhoto photoAction = nextNodeData.getPhotoAction();
            photoAction.setAfter(after);
        } else if (nextNodeData.getActionType().equals("SERVICE_START")) {
            DronesActionService startService = nextNodeData.getStartService();
            startService.setType("SERVICE_START");
            startService.setAfter(after);
            return startService;
        } else if (nextNodeData.getActionType().equals("SERVICE_STOP")) {
            DronesActionService stopService = nextNodeData.getStopService();
            stopService.setType("SERVICE_STOP");
            stopService.setAfter(after);
        } else if (nextNodeData.getActionType().equals("TAKE_OFF")) {
            DronesActionTakeOff takeOffAction = nextNodeData.getTakeOffAction();
            takeOffAction.setAfter(after);
            return takeOffAction;
        }
        return null;
    }


    /**
     * 寻找任务节点
     */
    private List<NodeEntity> findTasknodes(DronesWorkflowVo workflowVo) {
        List<NodeEntity> taskNodes = new ArrayList<>();

        NodeEntity current = workflowVo.getStartNode();
        while (true) {
            String currentId = current.getId();
            // 找下一节点
            List<String> nextIds = workflowVo.getEdgeMap().get(currentId);
            if (nextIds == null || nextIds.isEmpty()) {
                break;
            }
            
            for (String nextId : nextIds) {
                NodeEntity nextNode = workflowVo.getNodeMap().get(nextId);
                if (nextNode == null) {
                    throw new RuntimeException("nodeMap 缺少节点：" + nextId);
                }
                // 添加起飞节点，任务节点，降落节点
                if ("task".equals(nextNode.getType())) {
                    taskNodes.add(nextNode);
                } else {
                    continue;
                }
                current = nextNode;
            }
        }
        return taskNodes;
    }
}