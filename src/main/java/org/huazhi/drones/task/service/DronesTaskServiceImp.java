package org.huazhi.drones.task.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.huazhi.drones.command.entity.dto.DronesCommandWebsocket;
import org.huazhi.drones.command.entity.dto.DronesStep;
import org.huazhi.drones.config.entity.DronesConfig;
import org.huazhi.drones.config.entity.dto.DronesConfigQueryDto;
import org.huazhi.drones.config.service.DronesConfigService;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.model.service.DronesModelService;
import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.drones.task.repository.DronesTaskRepository;
import org.huazhi.drones.util.DateUtil;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.drones.workflow.entity.DeviceNodeEntity;
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

        List<DronesStep> stepArray = new ArrayList<>();
        for (int i = 0; i < taskNodes.size(); i++) { 
            // 循环每一个步骤
            NodeEntity taskNode = taskNodes.get(i);
            DeviceNodeEntity data = taskNode.getData();
            long deviceId =  data.getDeviceId();
            DronesDevice deviceInfo = deviceService.listOne(new DronesDeviceQueryDto().setId(deviceId));
            commandWebsocket.setDeviceId(deviceInfo.getDeviceId());
            commandWebsocket.setType("command");
            // 构建步骤
            DronesStep step = new DronesStep();
            step.setTaskNumber(i + 1 + "");
            // 构建目标功能
            List<Long> configIds = data.getConfigIds();
            if (configIds != null && !configIds.isEmpty()) {
                Map<String, String> configMap = new HashMap<>();
                for(int j = 0; j < configIds.size(); j++) {
                    Long configId = configIds.get(j);
                    DronesConfig config = configService.listOne(new DronesConfigQueryDto().setId(configId));
                    configMap.put(config.getConfigName(), config.getConfigValue());
                }
                step.setTaskTarget(configMap);
            }
            // 构建路径
            Long routeId =  data.getRouteId();
            List<String> pathArray = data.getPath();
            if (pathArray != null && !pathArray.isEmpty()) {
                // 有航点
                List<List<Double>> pathList = new ArrayList<>();
                for (int j = 0; j < pathArray.size(); j++){
                    List<Double> point = new ArrayList<>();
                    String pointString = pathArray.get(j);
                    point = Arrays.asList(pointString.split(",")).stream().map(Double::parseDouble).collect(Collectors.toList());
                    point.add(0.0);
                    pathList.add(point);
                }
                step.setRoutePath(pathList);
            } else{
                DronesRouteLibrary route = routeLibraryService.listOne(new DronesRouteLibraryQueryDto().setId(routeId));
                List<List<Double>> pathList = getPathList(route.getRouteData());
                step.setRoutePath(pathList);
            }
            stepArray.add(step);
        }
        commandWebsocket.setStepArray(stepArray);
        log.info("发送指令信息:"+ JsonUtil.wrapJsonValue(commandWebsocket));
        connectionManager.sendMessageByDeviceId(commandWebsocket.getDeviceId(), commandWebsocket);
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
            // 添加设备节点
            if ("device".equals(nextNode.getType())) {
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