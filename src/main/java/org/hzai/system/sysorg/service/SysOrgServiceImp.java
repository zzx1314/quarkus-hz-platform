package org.hzai.system.sysorg.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.hzai.system.sysorg.entity.SysOrg;
import org.hzai.system.sysorg.entity.dto.SysOrgQueryDto;
import org.hzai.system.sysorg.entity.dto.SysOrgTreeDto;
import org.hzai.system.sysorg.entity.mapper.SysOrgMapper;
import org.hzai.system.sysorg.entity.vo.SysOrgVo;
import org.hzai.system.sysorg.repository.SysOrgRepository;
import org.hzai.util.R;
import org.hzai.util.TreeUtil;

import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SysOrgServiceImp implements SysOrgService {
    @Inject
    SysOrgRepository sysOrgRepository;

	@Inject
    SysOrgMapper sysOrgMapper;
    @Override
    public List<SysOrg> listOrgs() {
        return sysOrgRepository.list("isDeleted = ?1", Sort.by("sort asc"),  0);
    }

    @Override
    public List<SysOrg> listOrgsByDto(SysOrgQueryDto sysOrgDto) {
        return sysOrgRepository.selectOrgList(sysOrgDto);
    }

    @Override
    public List<SysOrgTreeDto> listOrgTrees(SysOrgQueryDto dto) {
        List<SysOrg> selectOrgList = sysOrgRepository.listAll(Sort.by("sort asc"));
        List<SysOrg> resultOrg = new ArrayList<>();
        if (!selectOrgList.isEmpty()) {
            // 通过id进行分组
			Map<Integer, List<SysOrg>> collect = selectOrgList.stream().collect(Collectors.groupingBy(SysOrg::getId));
            // 通过parentid进行分组
			Map<Integer, List<SysOrg>> groupByParentId = selectOrgList.stream()
				.collect(Collectors.groupingBy(SysOrg::getParentId));

            if (StringUtil.isNullOrEmpty(dto.getName()) || StringUtil.isNullOrEmpty(dto.getType())) {
                // 查询到的组织节点
				selectOrgList = sysOrgRepository.selectOrgList(dto);
                if (selectOrgList != null && !selectOrgList.isEmpty()) {
                    selectOrgList.forEach(one -> {
						// 组成父节点的
						this.getParentOrgs(one, resultOrg, collect);
						// 组成子节点的
						this.getSonOrgs(one, resultOrg, groupByParentId);
					});
                }
            } else {
                resultOrg.addAll(selectOrgList);
            }
            // 去除重复的元素
			Map<Integer, SysOrg> resultMap = new HashMap<>();
			if (resultOrg != null && resultOrg.size() > 0) {
				resultOrg.stream().forEach(one -> {
					resultMap.put(one.getId(), one);
				});
			}
			return getOrgTree(new ArrayList<>(resultMap.values()));
        }
        return null;
    }

    private List<SysOrgTreeDto> getOrgTree(List<SysOrg> orgs) {
		List<SysOrgTreeDto> treeList = orgs.stream().filter(org -> !org.getId().equals(org.getParentId())).map(org -> {
			SysOrgTreeDto node = new SysOrgTreeDto();
			node.setId(org.getId());
			node.setParentId(org.getParentId());
			node.setName(org.getName());
			node.setCreateTime(org.getCreateTime());
			node.setOrgDuty(org.getOrgDuty());
			node.setSort(org.getSort());
			node.setDesrc(org.getDesrc());
			node.setType(org.getType());
			node.setParentName(org.getParentName());
			return node;
		}).collect(Collectors.toList());
		return TreeUtil.buildByLoop(treeList, 0);
	}

    private void getParentOrgs(SysOrg oneOrg, List<SysOrg> resultOrg, Map<Integer, List<SysOrg>> sumOrg) {
		if (oneOrg.getParentId() != 0) {
			resultOrg.add(oneOrg);
			this.getParentOrgs(sumOrg.get(oneOrg.getParentId()).get(0), resultOrg, sumOrg);
		}
		else {
			resultOrg.add(oneOrg);
		}
	}

    private void getSonOrgs(SysOrg oneOrg, List<SysOrg> resultOrg, Map<Integer, List<SysOrg>> sumParentOrg) {
		if (sumParentOrg.get(oneOrg.getId()) != null && !sumParentOrg.get(oneOrg.getId()).isEmpty()) {
			List<SysOrg> sonOrgs = sumParentOrg.get(oneOrg.getId());
			resultOrg.addAll(sonOrgs);
			// 查询子节点下的节点
			sonOrgs.forEach(oneSon -> {
				this.getSonOrgs(oneSon, resultOrg, sumParentOrg);
			});
		}
		else {
			resultOrg.add(oneOrg);
		}
	}

    @Override
    public R<Object> registerOrg(SysOrgTreeDto sysOrg) {
        // 顶部门判断只能有一个
		if ("top".equals(sysOrg.getType())) {
			SysOrgQueryDto queryDto = new SysOrgQueryDto();
			queryDto.setType("top");
			List<SysOrg> list = sysOrgRepository.selectOrgList(queryDto);
			if (list.size() > 0) {
				return R.failed("顶部门只能添加一个!");
			}
		}
		/**
		 * 1、判断添加的上级是单位，当前添加的只能是单位和部门 2、判断上级是部门，只能创建部门
		 */
		SysOrg parentOrg = sysOrgRepository.findById(sysOrg.getParentId());
		if (null != parentOrg) {
			if ("company".equals(parentOrg.getType())) {
				// 上级是单位
				if ("company".equals(sysOrg.getType()) || "common".equals(sysOrg.getType())) {
				}
				else {
					return R.failed("上级是单位只能添加单位或者是部门！");
				}
			}
			else if ("common".equals(parentOrg.getType())) {
				// 上级是部门
				if ("common".equals(sysOrg.getType())) {
				}
				else {
					return R.failed("上级是部门只能添加部门！");
				}
			}
		}
		// 校验部门名称是否已存在
		int parentId = sysOrg.getParentId();
		if (this.checkOrgName(sysOrg.getName(), parentId)) {
			// 代表存在相同的
			SysOrg entity = sysOrgMapper.toEntity(sysOrg);
			sysOrgRepository.persist(entity);
			return R.ok();
		}
		return R.failed("本层级下已存在:" + sysOrg.getName() + "部门");
    }

	public Boolean checkOrgName(String orgName, Integer parentId) {
		List<SysOrg> sonOrg = sysOrgRepository.list("parentId = ?1 and name = ?2", parentId, orgName);
		if (sonOrg != null && sonOrg.size() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<SysOrgVo> listAllOrgVo(SysOrgQueryDto queryDto) {
		List<SysOrg> listOrg = sysOrgRepository.selectOrgList(queryDto);
		List<SysOrg> resultOrg = new ArrayList<>();
		if (listOrg != null && listOrg.size() > 0) {
			// 通过id进行分组
			Map<Integer, List<SysOrg>> collect = listOrg.stream().collect(Collectors.groupingBy(SysOrg::getId));
			// 通过parentid进行分组
			Map<Integer, List<SysOrg>> groupByParentId = listOrg.stream()
				.collect(Collectors.groupingBy(SysOrg::getParentId));
			if (StringUtil.isNullOrEmpty(queryDto.getName()) || StringUtil.isNullOrEmpty(queryDto.getType())) {
				listOrg =  sysOrgRepository.selectOrgList(queryDto);
				// 寻找父级节点
				if ((StringUtil.isNullOrEmpty(queryDto.getName()) || StringUtil.isNullOrEmpty(queryDto.getType()))
						&& !listOrg.isEmpty()) {
					listOrg.forEach(one -> {
						// 组成父节点的
						this.getParentOrgs(one, resultOrg, collect);
						// 组成子节点的
						this.getSonOrgs(one, resultOrg, groupByParentId);
					});
				}
				else {
					resultOrg.addAll(listOrg);
				}
			} else {
				resultOrg.addAll(listOrg);
			}
			// 去除重复的元素
			Map<Integer, SysOrg> resultMap = new HashMap<>();

			if (!resultOrg.isEmpty()) {
				resultOrg.stream().forEach(one -> {
					resultMap.put(one.getId(), one);
				});
			}
			List<SysOrg> result = new ArrayList<>(resultMap.values());
			result.sort(Comparator.comparingInt(SysOrg::getSort));
			return result.stream().map(one -> {
				SysOrgVo sysOrgVo = sysOrgMapper.toVo(one);
				sysOrgVo.setLabel(one.getName());
				sysOrgVo.setValue(one.getId());
				sysOrgVo.setParentName(
						collect.get(one.getParentId()) != null ? collect.get(one.getParentId()).get(0).getName() : "");
				return sysOrgVo;
			}).collect(Collectors.toList());

		}
		return null;
	}

	@Override
	public R<Object> updateOrg(SysOrgTreeDto sysOrg) {
		// 顶部门判断只能有一个
		if ("top".equals(sysOrg.getType())) {
			SysOrgQueryDto queryDto = new SysOrgQueryDto();
			queryDto.setType("top");
			queryDto.setNotId(sysOrg.getId());
			List<SysOrg> list = sysOrgRepository.selectOrgList(queryDto);
			if (list.size() > 0) {
				return R.failed("顶部门只能添加一个!");
			}
		}
		/**
		 * 1、判断添加的上级是单位，当前添加的只能是单位和部门 2、判断上级是部门，只能创建部门
		 */
		SysOrg parentOrg = sysOrgRepository.findById(sysOrg.getParentId());
		if (null != parentOrg) {
			if ("company".equals(parentOrg.getType())) {
				// 上级是单位
				if ("company".equals(sysOrg.getType()) || "common".equals(sysOrg.getType())) {
				}
				else {
					return R.failed("上级是单位只能添加单位或者是部门！");
				}
			}
			else if ("common".equals(parentOrg.getType())) {
				// 上级是部门
				if ("common".equals(sysOrg.getType())) {
				}
				else {
					return R.failed("上级是部门只能添加部门！");
				}
			}
		}
		// 修改部门前校验部门名称是否在子层级下存在
		return this.updateOrgAndTypeById(sysOrg);
	}

	@Transactional
	public R<Object> updateOrgAndTypeById(SysOrgTreeDto orgDTO) {
		R<Object> r = this.checkOrgBeforeUpdate(orgDTO);
		if (-1 == r.getCode()) {
			return R.failed(r.getMsg());
		}
		Optional<SysOrg> byIdOptional = sysOrgRepository.findByIdOptional(orgDTO.getId());
		if (byIdOptional.isPresent()) {
            SysOrg org = byIdOptional.get();
            sysOrgMapper.updateEntityFromDto(orgDTO, org);
            return R.ok();
        } else {
            return R.failed();
        }
	}


	public R<Object> checkOrgBeforeUpdate(SysOrgTreeDto sysOrg) {
		R<Object> r = R.getInstance();
		String name = sysOrgRepository.findById(sysOrg.getId()).getName();
		// 如果名称不同需要进行校验
		if (!sysOrg.getName().equals(name)) {
			Boolean checkResult = this.checkOrgName(sysOrg.getName(), sysOrg.getParentId());
			if (!checkResult) {
				r.setCode(-1);
				r.setMsg("当前层级下已存在：" + sysOrg.getName());
				return r;
			}
		}
		r.setCode(0);
		return r;
	}

}
