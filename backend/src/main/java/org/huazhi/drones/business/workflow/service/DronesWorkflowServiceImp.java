package org.huazhi.drones.business.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.huazhi.drones.business.device.entity.DronesDevice;
import org.huazhi.drones.business.device.service.DronesDeviceService;
import org.huazhi.drones.business.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.business.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.business.routelibrary.entity.mapper.DronesRouteLibraryMapper;
import org.huazhi.drones.business.routelibrary.entity.vo.DronesRouteLibraryVo;
import org.huazhi.drones.business.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.business.task.entity.DronesTask;
import org.huazhi.drones.business.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.business.task.service.DronesTaskService;
import org.huazhi.drones.business.workflow.entity.DronesWorkflow;
import org.huazhi.drones.business.workflow.entity.EdgeEntity;
import org.huazhi.drones.business.workflow.entity.NodeEntity;
import org.huazhi.drones.business.workflow.entity.dto.DronesWorkflowDto;
import org.huazhi.drones.business.workflow.entity.dto.DronesWorkflowQueryDto;
import org.huazhi.drones.business.workflow.repository.DronesWorkflowRepository;
import org.huazhi.drones.business.workflow.vo.DronesWorkflowVo;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesWorkflowServiceImp implements DronesWorkflowService {
    private static final Logger log = Logger.getLogger(DronesWorkflowServiceImp.class);
    @Inject
    DronesWorkflowRepository repository;

    @Inject
    DronesTaskService taskService;

    @Inject
    DronesRouteLibraryService routeLibraryService;

    @Inject
    DronesDeviceService deviceService;

    @Inject
    DronesRouteLibraryMapper mapper;

    @Override
    public List<DronesWorkflow> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<DronesWorkflow> listEntitysByDto(DronesWorkflowQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesWorkflow listOne(DronesWorkflowQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesWorkflow> listPage(DronesWorkflowQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Long register(DronesWorkflow entity) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto();
        queryDto.setUuid(entity.getUuid());
        DronesWorkflow oneFlow = this.listOne(queryDto);
        if (oneFlow != null) {
            // 修改
            String commandJsonString = taskService.getCommandJsonString(entity);
            entity.setId(oneFlow.getId());
            entity.setCommandJsonString(commandJsonString);
            log.infof("修改json：%s", commandJsonString);
            this.replaceById(entity);
            // 修改任务的状态是未开始
            DronesTaskDto taskQueryDto = new DronesTaskDto();
            taskQueryDto.setId(entity.getTaskId());
            taskQueryDto.setTaskStatus("未开始");
            taskService.replaceByDto(taskQueryDto);
            return oneFlow.getId();
        } else {
            // 新增
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            String commandJsonString = taskService.getCommandJsonString(entity);
            entity.setCommandJsonString(commandJsonString);
            repository.persist(entity);

            // 修改任务表
            DronesTask task = new DronesTask();
            task.setId(entity.getTaskId());
            task.setWorkflowId(entity.getId());
            task.setWorkflowUuid(entity.getUuid());
            taskService.replaceById(task);
            return entity.getId();
        }

    }

    @Override
    public void replaceById(DronesWorkflow entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesWorkflowQueryDto queryDto, DronesWorkflowDto dto) {
        repository.updateByQeryDto(queryDto, dto);
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
    public DronesWorkflowVo getWorkflowGraph(Long taskId) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto();
        DronesWorkflow dronesWorkflow = repository.selectOne(queryDto);
        return getWorkflowGraphItem(dronesWorkflow);
    }

    @Override
    public DronesWorkflowVo getWorkflowGraph(DronesWorkflow dronesWorkflow) {
         return getWorkflowGraphItem(dronesWorkflow);
    }

    private DronesWorkflowVo getWorkflowGraphItem (DronesWorkflow dronesWorkflow){
        DronesWorkflowVo dronesWorkflowVo = new DronesWorkflowVo();
        if (null != dronesWorkflow) {
            String nodes = dronesWorkflow.getNodes();
            List<NodeEntity> nodeEntityList = JsonUtil.fromJsonToList(nodes, NodeEntity.class);
            String edges = dronesWorkflow.getEdges();
            List<EdgeEntity> edgeEntityList = JsonUtil.fromJsonToList(edges, EdgeEntity.class);

            NodeEntity startNode = nodeEntityList.stream()
                    .filter(n -> "start".equals(n.getType()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("未找到开始节点"));
            dronesWorkflowVo.setStartNode(startNode);

            // 建立从 sourceId -> List<targetId> 的映射
            Map<String, List<String>> edgeMap = new HashMap<>();
            for (EdgeEntity edge : edgeEntityList) {
                edgeMap.computeIfAbsent(edge.getSource(), k -> new ArrayList<>())
                        .add(edge.getTarget());
            }
            dronesWorkflowVo.setEdgeMap(edgeMap);

            // 构建 id -> NodeEntity 映射
            Map<String, NodeEntity> nodeMap = nodeEntityList.stream()
                    .collect(Collectors.toMap(NodeEntity::getId, Function.identity()));
            dronesWorkflowVo.setNodeMap(nodeMap);

            // 建立 targetId -> List<sourceId> 的映射
            Map<String, List<String>> reverseEdgeMap = new HashMap<>();

            for (EdgeEntity edge : edgeEntityList) {
                reverseEdgeMap
                        .computeIfAbsent(edge.getTarget(), k -> new ArrayList<>())
                        .add(edge.getSource());
            }
            dronesWorkflowVo.setReverseEdgeMap(reverseEdgeMap);

        }
        return dronesWorkflowVo;
    }

    @Override
    public DronesWorkflow getWorkflow(Long taskId) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto();
        queryDto.setTaskId(taskId);
        return repository.selectOne(queryDto);
    }

    /**
     * 获取任务下的所有航线
     * 
     * @param taskId
     * @return
     */
    @Override
    public List<DronesRouteLibraryVo> getRouteByTaskId(Long taskId) {
        List<DronesRouteLibraryVo> result = new ArrayList<>();
        DronesWorkflow dronesWorkflow = getWorkflow(taskId);
        // 从地图中找到设备节点，然后从设备节点找到具体的航线数据
        if (null != dronesWorkflow) {
            String nodes = dronesWorkflow.getNodes();
            List<NodeEntity> nodeEntityList = JsonUtil.fromJsonToList(nodes, NodeEntity.class);
            for (NodeEntity nodeEntity : nodeEntityList) {
                if ("task".equals(nodeEntity.getType())) {
                    Long routeId = nodeEntity.getData().getRouteId();
                    DronesDevice deviceInfo = deviceService.listById(nodeEntity.getData().getDeviceId());

                    DronesRouteLibraryQueryDto routeQueryDto = new DronesRouteLibraryQueryDto();
                    routeQueryDto.setId(routeId);
                    DronesRouteLibrary route = routeLibraryService.listOne(routeQueryDto);
                    if (null != route) {
                        DronesRouteLibraryVo routeVo = new DronesRouteLibraryVo();
                        mapper.toVo(route, routeVo);
                        routeVo.setDeviceId(deviceInfo.getId());
                        result.add(routeVo);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getCommandJsonString(Long taskId) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto();
        queryDto.setTaskId(taskId);
        DronesWorkflow dronesWorkflow = repository.selectOne(queryDto);
        if (dronesWorkflow != null) {
            return dronesWorkflow.getCommandJsonString();
        }
        return null;
    }

}