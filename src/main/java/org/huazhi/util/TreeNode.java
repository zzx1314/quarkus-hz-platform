package org.huazhi.util;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode {

	protected int id;

	protected int parentId;

	protected List<TreeNode> children;

	public void add(TreeNode node) {
		children.add(node);
	}

}
