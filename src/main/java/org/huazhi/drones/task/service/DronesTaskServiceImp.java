package org.huazhi.drones.task.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.drones.command.entity.dto.DroneTaskPlan;
import org.huazhi.drones.command.entity.dto.DronesAction;
import org.huazhi.drones.command.entity.dto.DronesActionData;
import org.huazhi.drones.command.entity.dto.DronesCommandWebsocket;
import org.huazhi.drones.config.service.DronesConfigService;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.model.service.DronesModelService;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.drones.task.repository.DronesTaskRepository;
import org.huazhi.drones.util.DateUtil;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.drones.workflow.entity.DeviceNodeAction;
import org.huazhi.drones.workflow.entity.DeviceNodeActionParallelGroup;
import org.huazhi.drones.workflow.entity.DeviceNodeLand;
import org.huazhi.drones.workflow.entity.DeviceNodeTakeoff;
import org.huazhi.drones.workflow.entity.NodeEntity;
import org.huazhi.drones.workflow.service.DronesWorkflowService;
import org.huazhi.drones.workflow.vo.DronesWorkflowVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.common.Sort;
import io.quarkus.vertx.runtime.jackson.JsonUtil;
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
        log.info("任务流程图：{}", JsonUtil.wrapJsonValue(workflowGraph));
        // 获取任务节点
        List<NodeEntity> taskNodes = findTasknodes(workflowGraph);
        DronesCommandWebsocket commandWebsocket = new DronesCommandWebsocket();

        List<DroneTaskPlan> planArray = new ArrayList<>();
        for (int i = 0; i < taskNodes.size(); i++) { 
            // 循环每一个步骤
            NodeEntity taskNode = taskNodes.get(i);
            commandWebsocket.setDeviceId(taskNode.getDeviceId());
            commandWebsocket.setType("command");
            // 构建任务步骤
            DroneTaskPlan taskPlan = new DroneTaskPlan();
            taskPlan.setTaskId(i + 1 + "");
            // 判断节点类型
            if ("takeoff".equals(taskNode.getType())) {
                // 起飞节点
                DeviceNodeTakeoff takeoffInfo = taskNode.getTakeoff();
                // 动作数组
                List<DronesAction> actionArray = new ArrayList<>();
                // 动作
                DronesAction actionTakeoff = new DronesAction();
                actionTakeoff.setActionId(i + 1 + "-1");
                actionTakeoff.setActionType("hzRos");
                DronesActionData actionData = new DronesActionData();
                actionData.setAction("takeoff");
                actionData.setHeight(takeoffInfo.getHeight());
                actionData.setSpeed(takeoffInfo.getSpeed());
                actionTakeoff.setActionData(actionData);
                actionTakeoff.setTimeout(takeoffInfo.getTimeout());
                actionArray.add(actionTakeoff);
                // 添加动作
                taskPlan.setActionArray(actionArray);
            } else if ("land".equals(taskNode.getType())) {
                // 降落节点
                DeviceNodeLand landInfo = taskNode.getLand();
                // 动作数组
                List<DronesAction> actionArray = new ArrayList<>();
                // 动作
                DronesAction actionTakeoLand = new DronesAction();
                actionTakeoLand.setActionId(i + 1 + "-1");
                actionTakeoLand.setActionType("hzRos");
                DronesActionData actionData = new DronesActionData();
                actionData.setAction("land");
                actionTakeoLand.setActionData(actionData);
                actionTakeoLand.setTimeout(landInfo.getTimeout());
                actionArray.add(actionTakeoLand);
                // 添加动作
                taskPlan.setActionArray(actionArray);
            } else if ("task".equals(taskNode.getType())) {
                // 设备节点--执行任务
                DeviceNodeAction taskInfo = taskNode.getAction();
                // 顺序执行的动作
                List<String> sequenceList = taskInfo.getSequence();
                // 并行动作
                DeviceNodeActionParallelGroup parallelGroup = taskInfo.getParallelGroup();
                // 动作数组
                List<DronesAction> actionArray = new ArrayList<>();
                // 动作
                for (int j = 0; j < sequenceList.size(); j++) { 
                    String sequence = sequenceList.get(j);
                    if ("parallel".equals(sequence)) {
                        // 并行动作
                        List<String> parallelAction = parallelGroup.getList();
                        for (int k = 0; k < parallelAction.size(); k++) { 
                            String parallel = parallelAction.get(k);
                            if ("moveBase".equals(parallel)) {
                                // 移动航线
                                getActionDataMoveBase(i, taskInfo, actionArray, j, k);
                            } else if ("videoCapture".equals(parallel)) {
                                // 视频抓拍
                                 getActionDataVideoCapture(i, taskInfo, actionArray, j, k);
                            } else if ("takePhoto".equals(sequence)) {
                                // 拍照
                                getActionDataTakePhoto(i, taskInfo, actionArray, j, k);
                            } else if ("targetRecognition".equals(sequence)) {
                                // 目标识别
                                getActionDataTarge(i, taskInfo, actionArray, j, k);
                            }
                        }
                    }
                    if ("moveBase".equals(sequence)) {
                        // 移动航线
                        getActionDataMoveBase(i, taskInfo, actionArray, j, 0);
                    } else if ("videoCapture".equals(sequence)) {
                        // 视频抓拍
                        getActionDataVideoCapture(i, taskInfo, actionArray, j, 0);
                    } else if ("takePhoto".equals(sequence)) {
                        // 拍照
                        getActionDataTakePhoto(i, taskInfo, actionArray, j, 0);
                    } else if ("targetRecognition".equals(sequence)) {
                        // 目标识别
                        getActionDataTarge(i, taskInfo, actionArray, j, 0);
                    }
                }
                taskPlan.setActionArray(actionArray);
            }
            planArray.add(taskPlan);
        }
        commandWebsocket.setTaskplanArray(planArray);
        log.info("发送指令信息:"+ JsonUtil.wrapJsonValue(commandWebsocket));
        connectionManager.sendMessageByDeviceId(commandWebsocket.getDeviceId(), commandWebsocket);
    }

    private void getActionDataTarge(int i, DeviceNodeAction taskInfo, List<DronesAction> actionArray, int j, int k) {
        DronesAction action = taskInfo.getTargetRecognition();
        if (k == 0) {
            action.setActionId(i + 1 + "-" + (j + 1));
        } else {
            action.setActionId(i + 1 + "-" + (j + 1) + "-" + (k + 1));
        }
        action.setActionType("targetRecognition");
        DronesActionData actionData = new DronesActionData();
        actionData.setAction("targetRecognition");
        action.setActionData(actionData);
        actionArray.add(action);
    }

    private void getActionDataTakePhoto(int i, DeviceNodeAction taskInfo, List<DronesAction> actionArray, int j, int k) {
        DronesAction action = taskInfo.getTakePhoto();
        if (k == 0) {
            action.setActionId(i + 1 + "-" + (j + 1));
        } else {
            action.setActionId(i + 1 + "-" + (j + 1) + "-" + (k + 1));
        }
        action.setActionType("takePhoto");
        DronesActionData actionData = new DronesActionData();
        actionData.setAction("takePhoto");
        action.setActionData(actionData);
        actionArray.add(action);
    }

    private void getActionDataVideoCapture(int i, DeviceNodeAction taskInfo, List<DronesAction> actionArray, int j, int k) {
        DronesAction action = taskInfo.getVideoCapture();
        if (k == 0) {
            action.setActionId(i + 1 + "-" + (j + 1));
        } else {
            action.setActionId(i + 1 + "-" + (j + 1) + "-" + (k + 1));
        }
        action.setActionType("videoCapture");
        DronesActionData actionData = new DronesActionData();
        actionData.setAction("videoCapture");
        action.setActionData(actionData);
        actionArray.add(action);
    }

    private void getActionDataMoveBase(int i, DeviceNodeAction taskInfo, List<DronesAction> actionArray, int j, int k) {
        DronesAction action = taskInfo.getMoveBase();
        if (k == 0) {
            action.setActionId(i + 1 + "-" + (j + 1));
        } else {
            action.setActionId(i + 1 + "-" + (j + 1) + "-" + (k + 1));
        }
        action.setActionType("hzRos");
        DronesActionData actionData = new DronesActionData();
        actionData.setAction("moveBase");
        actionData.setPath(getPathList(action.getActionData().getPathString()));
        action.setActionData(actionData);
        actionArray.add(action);
    }

    /**
     * 根据工作流定义查找任务中的设备节点
     * 
     * @param workflowVo 工作流定义对象，包含开始节点、边映射和节点映射
     * @return 设备类型的任务节点列表
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
            // 默认只走第一条线（如有分支可扩展）
            String nextId = nextIds.get(0);

            NodeEntity nextNode = workflowVo.getNodeMap().get(nextId);
            if (nextNode == null) {
                throw new RuntimeException("nodeMap 缺少节点：" + nextId);
            }
            // 添加起飞节点，任务节点，降落节点
            if ("task".equals(nextNode.getType()) || "takeoff".equals(nextNode.getType()) || "land".equals(nextNode.getType())) {
                taskNodes.add(nextNode);
            }
            current = nextNode;
        }
        return taskNodes;
    }

    private List<List<Double>> getPathList(String routeData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<List<Double>> result = mapper.readValue(routeData, mapper.getTypeFactory()
                    .constructCollectionType(List.class, mapper.getTypeFactory()
                            .constructCollectionType(List.class, Double.class)));
            return result;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}