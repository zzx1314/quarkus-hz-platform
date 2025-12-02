package org.huazhi.drones.task.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.huazhi.drones.command.entity.webscoketdto.DronesCommandWebsocketV1;
import org.huazhi.drones.command.entity.webscoketdto.DronesRoute;
import org.huazhi.drones.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.command.entity.webscoketdto.task.DronesTaskWebScoket;
import org.huazhi.drones.config.service.DronesConfigService;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.model.service.DronesModelService;
import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.services.entity.DronesServices;
import org.huazhi.drones.services.entity.vo.DronesServicesVo;
import org.huazhi.drones.services.service.DronesServicesService;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Inject
    DronesServicesService servicesService;

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
     * @param id 任务ID
     */
    @Override
    public void startTask(Long id) {
        // 更新任务状态
        DronesTask task = repository.findById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在：" + id);
        }
        task.setTaskStatus("进行中");
        repository.updateById(task);
        
        // 获取任务流程图
        DronesWorkflowVo workflowGraph = workflowService.getWorkflowGraph(id);
        log.info("任务流程图：{}", JsonUtil.toJson(workflowGraph));
        
        // 获取任务节点
        List<NodeEntity> taskNodes = findTasknodes(workflowGraph);
        if (taskNodes.isEmpty()) {
            throw new RuntimeException("任务节点为空，无法启动任务");
        }
        
        // 初始化命令对象
        DronesCommandWebsocketV1 commandWebsocket = new DronesCommandWebsocketV1();
        commandWebsocket.setType("command");
        
        // 获取首个任务节点的设备和航线信息
        NodeEntity firstTaskNode = taskNodes.get(0);
        Long deviceId = firstTaskNode.getData().getDeviceId();
        long routeId = firstTaskNode.getData().getRouteId();
        
        // 补充航线路径
        commandWebsocket.setRoute(getRoutesById(routeId));
        
        // 获取设备信息（用于后续发送消息）
        DronesDevice device = deviceService.listById(deviceId);
        
        // 构建任务步骤列表
        List<DronesTaskWebScoket> tasks = new ArrayList<>();
        List<DronesAction> errorNodes = new ArrayList<>();
        Set<String> serviceNames = new HashSet<>();
        
        for (int i = 0; i < taskNodes.size(); i++) {
            NodeEntity taskNode = taskNodes.get(i);
            
            // 构建任务步骤
            DronesTaskWebScoket taskInfo = new DronesTaskWebScoket();
            taskInfo.setTaskId(String.valueOf(i + 1));
            taskInfo.setFromWp(taskNode.getData().getTaskInfo().getFromWp());
            taskInfo.setToWp(taskNode.getData().getTaskInfo().getToWp());
            taskInfo.setEvent(taskNode.getData().getTaskInfo().getEvent());
            
            // 寻找action
            List<DronesAction> actions = findAction(workflowGraph, taskNode, errorNodes, serviceNames);
            taskInfo.setActions(actions);
            tasks.add(taskInfo);
        }
        
        // 批量获取服务列表（优化N+1查询）
        List<DronesServicesVo> services = getServicesByNames(serviceNames);
        
        // 组装命令对象
        commandWebsocket.setServices(services);
        commandWebsocket.setTasks(tasks);
        commandWebsocket.setOnErrorActions(errorNodes);
        
        log.info("发送指令信息：{}", JsonUtil.toJson(commandWebsocket));
        connectionManager.sendMessageByDeviceId(device.getDeviceId(), commandWebsocket);
    }

    /**
     * 根据服务名称集合批量获取服务信息
     * @param serviceNames 服务名称集合
     * @return 服务VO列表
     */
    private List<DronesServicesVo> getServicesByNames(Set<String> serviceNames) {
        if (serviceNames == null || serviceNames.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 批量查询所有服务
        List<DronesServices> servicesList = servicesService.listByTypes(serviceNames);
        
        // 转换为VO对象
        List<DronesServicesVo> services = new ArrayList<>();
        for (DronesServices service : servicesList) {
            DronesServicesVo vo = new DronesServicesVo();
            vo.setType(service.getType());
            vo.setParams(JsonUtil.toJsonObject(service.getParams()));
            services.add(vo);
        }
        return services;
    }

    /**
     * 根据航线ID获取航线路径点
     * 
     * @param routeId 航线ID
     * @return 航线路径点列表
     */
    private List<DronesRoute> getRoutesById(long routeId) {
        DronesRouteLibrary route =  routeLibraryService.listEntitysById(routeId);
        String routeData = route.getRouteData();
        List<DronesRoute> routes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<List<Double>> result = mapper.readValue(routeData, new TypeReference<List<List<Double>>>() {});
            for (List<Double> point : result) {
                DronesRoute routeDataOne = new DronesRoute();
                routeDataOne.setLat(point.get(0));
                routeDataOne.setLon(point.get(1));
                routeDataOne.setAlt(0.0);
                routes.add(routeDataOne);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return routes;
    }

    /**
     * 获取动作节点
     * 
     */
    private List<DronesAction> findAction(DronesWorkflowVo workflowGraph, NodeEntity taskNode,
            List<DronesAction> errorNodes, Set<String> serviceName) {
        // key位任务id，vaule是所有的action
        List<DronesAction> actions = new ArrayList<>();
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
            for (NodeEntity actionNode : actionNodeList) {
                NodeEntityData actionNodeData = actionNode.getData();

                DronesAction action = actionNodeData.getAction();
                action.setActionId(actionNode.getId());
                action.setAfter(initEvent);
                actions.add(action);
                nextActionNodeList.add(actionNode);

                if (action.getType().contains("SERVICE")) {
                    serviceName.add(action.getParams().getType());
                }
            }
        }
        if (nextActionNodeList.size() > 0) {
            // 寻找后面串联的action
            for (NodeEntity nextActionNode : nextActionNodeList) {
                addNextAction(workflowGraph, nextActionNode, actions, errorNodes, serviceName);
            }
        }
        return actions;
    }

    private void addNextAction(DronesWorkflowVo workflowVo, NodeEntity currentNode, List<DronesAction> actions, List<DronesAction> errorNodes, Set<String> serviceName) {
        // 防止循环
        Set<String> visited = new HashSet<>();
        dfsActions(workflowVo, currentNode, actions, visited, errorNodes, serviceName);
    }

    /**
     * 深度优先遍历获取后续action节点
     */
    private void dfsActions(DronesWorkflowVo workflowVo, NodeEntity currentNode, List<DronesAction> actions,
            Set<String> visited, List<DronesAction> errorActions, Set<String> serviceName) {
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
            if (nextNode != null && "action".equals(nextNode.getType())) {
                DronesAction action = getActionInfo(nextNode, getAfter(currentNode));
                String type = action.getType();
                if (type.contains("SERVICE")) {
                    serviceName.add(action.getParams().getType());
                }
                actions.add(action);
                dfsActions(workflowVo, nextNode, actions, visited, errorActions, serviceName);
            } else if (nextNode != null && "error".equals(nextNode.getType())) {
                DronesAction errorAction = nextNode.getData().getAction();
                errorAction.setAfter(getErrorAfter(currentNode));
                errorActions.add(errorAction);
            }
        }
    }

    /**
     * 获取action的after
     */
    private String getAfter(NodeEntity currentNode) {
        NodeEntityData data = currentNode.getData();
        return data.getAction().getParams().getEvent().getOnComplete();
    }


    /**
     * 获取error的after
     */
    private String getErrorAfter(NodeEntity currentNode) {
        NodeEntityData data = currentNode.getData();
        return data.getAction().getParams().getEvent().getOnFail();
    }

    /**
     * 获取action信息
     */
    private DronesAction getActionInfo(NodeEntity nextNode, String after) {
        DronesAction action = nextNode.getData().getAction();
        action.setActionId(nextNode.getId());
        action.setAfter(after);
        return action;
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
                if ("task".equals(nextNode.getType())) {
                    taskNodes.add(nextNode);
                    current = nextNode;
                    break;
                } else if ("end".equals(nextNode.getType())) {
                    current = nextNode;
                    break;
                } 
            }
        }
        return taskNodes;
    }

}
