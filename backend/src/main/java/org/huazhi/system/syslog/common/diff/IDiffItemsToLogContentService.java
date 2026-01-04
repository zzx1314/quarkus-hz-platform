package  org.huazhi.system.syslog.common.diff;

import de.danielbechler.diff.node.DiffNode;


public interface IDiffItemsToLogContentService {

    String toLogContent(DiffNode diffNode, final Object o1, final Object o2);
}
