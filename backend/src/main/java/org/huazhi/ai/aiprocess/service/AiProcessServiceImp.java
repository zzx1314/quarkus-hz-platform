package org.huazhi.ai.aiprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.huazhi.ai.aiprocess.entity.AiProcess;
import org.huazhi.ai.aiprocess.entity.EdgeEntity;
import org.huazhi.ai.aiprocess.entity.NodeEntity;
import org.huazhi.ai.aiprocess.entity.dto.AiProcessDto;
import org.huazhi.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.huazhi.ai.aiprocess.entity.vo.AiProcessNet;
import org.huazhi.ai.aiprocess.repository.AiProcessRepository;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiProcessServiceImp implements AiProcessService {
    @Inject
    AiProcessRepository repository;
    @Override
    public List<AiProcess> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiProcess> listEntitysByDto(AiProcessQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiProcess listOne(AiProcessQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiProcess> listPage(AiProcessQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiProcess entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiProcess entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiProcessDto dto) {
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
    public AiProcessNet getAiProcessNet(Long appId) {
        AiProcessNet aiProcessNet = new AiProcessNet();
        AiProcessQueryDto queryDto = new AiProcessQueryDto().setAppId(appId);
		AiProcess aiProcess = repository.selectOne(queryDto);
		String nodes = aiProcess.getNodes();
		List<NodeEntity> nodeEntityList = JsonUtil.fromJsonToList(nodes, NodeEntity.class);
		String edges = aiProcess.getEdges();
		List<EdgeEntity> edgeEntityList = JsonUtil.fromJsonToList(edges, EdgeEntity.class);

		NodeEntity startNode = nodeEntityList.stream()
				.filter(n -> "start".equals(n.getType()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("未找到开始节点"));
		aiProcessNet.setStartNode(startNode);

		// 建立从 sourceId -> List<targetId> 的映射
		Map<String, List<String>> edgeMap = new HashMap<>();
		for (EdgeEntity edge : edgeEntityList) {
			edgeMap.computeIfAbsent(edge.getSource(), k -> new ArrayList<>())
					.add(edge.getTarget());
		}
		aiProcessNet.setEdgeMap(edgeMap);

		// 构建 id -> NodeEntity 映射
		Map<String, NodeEntity> nodeMap = nodeEntityList.stream()
				.collect(Collectors.toMap(NodeEntity::getId, Function.identity()));
		aiProcessNet.setNodeMap(nodeMap);

		return aiProcessNet;
    }

}