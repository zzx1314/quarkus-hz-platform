package org.huazhi.system.syslog.common.service.impl;

import org.huazhi.system.syslog.common.beans.Operator;
import org.huazhi.system.syslog.common.service.IOperatorGetService;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author muzhantong
 * create on 2020/4/29 5:45 下午
 */
@ApplicationScoped
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        // return Optional.ofNullable(UserUtils.getUser())
        //                .map(a -> new Operator(a.getName(), a.getLogin()))
        //                .orElseThrow(()->new IllegalArgumentException("user is null"));
        Operator operator = new Operator();
        operator.setOperatorId("111");
        return operator;
    }
}
