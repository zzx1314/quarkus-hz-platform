package org.hzai.drones.task.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import org.hzai.drones.command.entity.dto.DronesCommandWebsocket;
import org.hzai.drones.command.entity.dto.LocationInfo;
import org.hzai.drones.config.entity.DronesConfig;
import org.hzai.drones.config.entity.dto.DronesConfigQueryDto;
import org.hzai.drones.config.service.DronesConfigService;
import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.service.DronesDeviceService;
import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.drones.model.service.DronesModelService;
import org.hzai.drones.routelibrary.entity.DronesRouteLibrary;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.hzai.drones.routelibrary.service.DronesRouteLibraryService;
import org.hzai.drones.task.entity.DronesTask;
import org.hzai.drones.task.entity.dto.DronesTaskQueryDto;
import org.hzai.drones.task.entity.dto.DronesTaskDto;
import org.hzai.drones.task.repository.DronesTaskRepository;
import org.hzai.drones.util.DateUtil;
import org.hzai.drones.websocket.service.ConnectionManager;
import org.hzai.drones.workflow.entity.NodeEntity;
import org.hzai.drones.workflow.service.DronesWorkflowService;
import org.hzai.drones.workflow.vo.DronesWorkflowVo;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.common.Sort;
import io.quarkus.vertx.runtime.jackson.JsonUtil;
import io.vertx.core.json.JsonObject;
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
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
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
        return repository.count();
    }

    /**
     * 启动任务
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
        /*
         * 流程的节点类型
         * 1. 设备
         * 2. 配置
         * 3. 模型
         * 4. 航线
         * 
         * 找到设备为每个设备（配置，模型，航线）
         */
        // 1、获取设备节点
         List<NodeEntity> deviceList = workflowGraph.getNodeMap().values().stream()
                .filter(node -> "设备".equals(node.getType()))
                .toList();
        if (deviceList != null && deviceList.size() >0) {
            for (NodeEntity devicEntity : deviceList) { 
                // 2、组建向设备发送的信息
                DronesCommandWebsocket commandWebsocket = new DronesCommandWebsocket();
                JsonObject data = devicEntity.getData();
                Long deviceId =  data.getLong("deviceId");
                DronesDevice deviceInfo = deviceService.listOne(new DronesDeviceQueryDto().setId(deviceId));
                commandWebsocket.setDeviceId(deviceInfo.getDeviceId());
                commandWebsocket.setType("command");
                // 获取配置
                Long configId =  data.getLong("configIds");
                List<DronesConfig> configs = configService.listEntitysByDto(new DronesConfigQueryDto().setId(configId));
                Map<String, String> configMap = new HashMap<>();
                for (DronesConfig config : configs) {
                    configMap.put(config.getConfigName(), config.getConfigValue());
                }
                commandWebsocket.setConfig(configMap);
                // 获取模型
                Long modelId =  data.getLong("modelId");
                DronesModel model = modelService.listOne(new DronesModelQueryDto().setId(modelId));
                Map<String, String> modelMap = new HashMap<>();
                modelMap.put("name", model.getModelName());
                commandWebsocket.setModel(modelMap);
                // 获取航线
                Long routeId =  data.getLong("routeId");
                DronesRouteLibrary route = routeLibraryService.listOne(new DronesRouteLibraryQueryDto().setId(routeId));
                List<LocationInfo> rouInfos = new ArrayList<>();
                for (List<Double> path : getPathList(route.getRouteData())) {
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setLongitude(path.get(0).toString());
                    locationInfo.setLatitude(path.get(1).toString());
                    rouInfos.add(locationInfo);
                }
                commandWebsocket.setRoute(rouInfos);

                connectionManager.sendMessageByDeviceId(deviceInfo.getDeviceId(), commandWebsocket);
            }
        }
    }

    private  List<List<Double>> getPathList(String routeData) {
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