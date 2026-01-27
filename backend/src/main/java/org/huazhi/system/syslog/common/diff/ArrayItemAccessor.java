package org.huazhi.system.syslog.common.diff;


import de.danielbechler.diff.access.TypeAwareAccessor;
import de.danielbechler.diff.identity.EqualsIdentityStrategy;
import de.danielbechler.diff.identity.IdentityStrategy;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.ElementSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 数组/集合元素访问器
 *
 * 用于在对象比较（diff）过程中访问和操作集合中的特定元素
 * 通过身份策略（IdentityStrategy）来匹配和识别目标元素
 *
 * 实现了TypeAwareAccessor接口（该接口已继承Accessor），
 * 提供对集合元素的类型感知访问能力
 *
 * @author system
 */
public class ArrayItemAccessor implements TypeAwareAccessor {

    private final Object referenceItem;
    private final IdentityStrategy identityStrategy;

    public ArrayItemAccessor(final Object referenceItem) {
        this(referenceItem, EqualsIdentityStrategy.getInstance());
    }

    public ArrayItemAccessor(final Object referenceItem,
                             final IdentityStrategy identityStrategy) {
        this.referenceItem = referenceItem;
        this.identityStrategy = identityStrategy;
    }

    @Override
    public Class<?> getType() {
        return referenceItem != null ? referenceItem.getClass() : null;
    }

    @Override
    public String toString() {
        return "collection item " + getElementSelector();
    }

    @Override
    public ElementSelector getElementSelector() {
        final CollectionItemElementSelector selector = new CollectionItemElementSelector(referenceItem);
        return identityStrategy == null ? selector : selector.copyWithIdentityStrategy(identityStrategy);
    }

    @Override
    public Object get(final Object target) {
        final Collection<?> targetCollection = objectAsCollection(target);
        if (targetCollection == null) {
            return null;
        }
        for (final Object item : targetCollection) {
            if (item != null && identityStrategy.equals(item, referenceItem)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void set(final Object target, final Object value) {
        final Collection<Object> targetCollection = objectAsCollection(target);
        if (targetCollection == null) {
            return;
        }
        final Object previous = get(target);
        if (previous != null) {
            unset(target);
        }
        targetCollection.add(value);
    }

    private static Collection<Object> objectAsCollection(final Object object) {
        if (object == null) {
            return null;
        } else if (object.getClass().isArray()) {
            return new ArrayList<>(Arrays.asList((Object[]) object));
        }
        throw new IllegalArgumentException(object.getClass().toString());
    }

    @Override
    public void unset(final Object target) {
        final Collection<?> targetCollection = objectAsCollection(target);
        if (targetCollection == null) {
            return;
        }
        final Iterator<?> iterator = targetCollection.iterator();
        while (iterator.hasNext()) {
            final Object item = iterator.next();
            if (item != null && identityStrategy.equals(item, referenceItem)) {
                iterator.remove();
                break;
            }
        }
    }
}
