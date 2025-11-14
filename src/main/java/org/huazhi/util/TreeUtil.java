package org.huazhi.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.system.sysmenu.entity.SysMenu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@UtilityClass
public class TreeUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param treeNodes: 传入的树节点列表
	 * @param root:
	 * @description 两层循环实现建树
	 * @author: zzx
	 * @date 2020-02-19 15:14
	 * @return: java.util.List<T>
	 **/
	public <T extends TreeNode> List<T> buildByLoop(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId() == treeNode.getId()) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 构建菜单树
	 */
	public static ArrayNode listToTree(ArrayNode arr) {
        ArrayNode r = mapper.createArrayNode();
        Map<String, ObjectNode> hash = new HashMap<>();

        // 将数组转为Object的形式，key为数组中的id
        for (JsonNode node : arr) {
            if (node.isObject()) {
                ObjectNode obj = (ObjectNode) node;
                hash.put(obj.get("id").asText(), obj);
            }
        }
        // 遍历结果集
        for (JsonNode node : arr) {
            if (node.isObject()) {
                ObjectNode aVal = (ObjectNode) node;
                JsonNode parentIdNode = aVal.get("parentId");

                if (parentIdNode != null && !parentIdNode.isNull()) {
                    ObjectNode hashVP = hash.get(parentIdNode.asText());

                    // 如果记录的pid存在，则说明它有父节点
                    if (hashVP != null) {
                        JsonNode childrenNode = hashVP.get("children");
                        ArrayNode children;
                        if (childrenNode != null && childrenNode.isArray()) {
                            children = (ArrayNode) childrenNode;
                        } else {
                            children = mapper.createArrayNode();
                            hashVP.set("children", children);
                        }
                        children.add(aVal);
                    } else {
                        r.add(aVal);
                    }
                } else {
                    r.add(aVal);
                }
            }
        }
        return r;
    }

	/**
	 * @description 使用递归方法建树
	 * @author: zzx
	 * @date 2020-02-19 15:17
	 * @param treeNodes:
	 * @param root:
	 * @return: java.util.List<T>
	 **/
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * @description 递归查找子节点
	 * @author: zzx
	 * @date 2020-02-19 15:18
	 * @param treeNode:
	 * @param treeNodes:
	 * @return: T
	 **/
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId() == it.getParentId()) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * @description 通过sysMenu创建树形节点
	 * @author: zzx
	 * @date 2020-02-19 15:18
	 * @param menus:
	 * @param root:
	 **/
	public List<MenuTree> buildTree(List<SysMenu> menus, int root) {
		List<MenuTree> trees = new ArrayList<>();
		MenuTree node;
		for (SysMenu menu : menus) {
			node = new MenuTree();
			node.setId(menu.getId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getName());
			node.setPath(menu.getPathUrl());
			trees.add(node);
		}
		return TreeUtil.buildByLoop(trees, root);
	}

}
