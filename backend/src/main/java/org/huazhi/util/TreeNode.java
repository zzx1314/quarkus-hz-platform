package org.huazhi.util;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode {

	protected Long id;

	protected Long parentId;

	protected List<TreeNode> children;

	public void add(TreeNode node) {
		children.add(node);
	}

}
