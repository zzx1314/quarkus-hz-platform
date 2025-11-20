package org.huazhi.drones.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.routelibrary.entity.mapper.DronesRouteLibraryMapper;
import org.huazhi.drones.routelibrary.entity.vo.DronesRouteLibraryVo;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.service.DronesTaskService;
import org.huazhi.drones.workflow.entity.DronesWorkflow;
import org.huazhi.drones.workflow.entity.EdgeEntity;
import org.huazhi.drones.workflow.entity.NodeEntity;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowDto;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowQueryDto;
import org.huazhi.drones.workflow.repository.DronesWorkflowRepository;
import org.huazhi.drones.workflow.vo.DronesWorkflowVo;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesWorkflowServiceImp implements DronesWorkflowService {
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
    public Boolean register(DronesWorkflow entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        repository.persist(entity);
        // 修改任务的id
        DronesTask task = new DronesTask();
        task.setId(entity.getTaskId());
        task.setWorkflowId(entity.getId());
        taskService.replaceById(task);
        return true;
    }

    @Override
    public void replaceById(DronesWorkflow entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesWorkflowQueryDto queryDto,DronesWorkflowDto dto) {
        repository.updateByQeryDto(queryDto ,dto);
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
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto().setTaskId(taskId);
        DronesWorkflow dronesWorkflow = repository.selectOne(queryDto);
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
        }
        return dronesWorkflowVo;
    }

    @Override
    public DronesWorkflow getWorkflow(Long taskId) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto().setTaskId(taskId);
        return repository.selectOne(queryDto);
    }

    /**
     * 获取任务下的所有航线
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
                    Long routeId = nodeEntity.getRouteId();
                    String deviceIdString = nodeEntity.getDeviceId();
                    DronesDevice deviceInfo = deviceService.listOne(new DronesDeviceQueryDto().setDeviceId(deviceIdString));
                    DronesRouteLibrary route = routeLibraryService.listOne(new DronesRouteLibraryQueryDto().setId(routeId));
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

}