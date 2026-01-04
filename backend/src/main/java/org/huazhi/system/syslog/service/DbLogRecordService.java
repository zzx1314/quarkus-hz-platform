package org.huazhi.system.syslog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.common.service.ILogRecordService;
import org.huazhi.system.syslog.entity.SysLog;
import org.huazhi.system.syslog.entity.mapper.SysLogMapper;
import org.huazhi.util.IpUtils;

import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DbLogRecordService implements ILogRecordService{
    @Inject
    HttpServerRequest request;

    @Inject
    SysLogService sysLogService;

    @Inject
    IpUtils ipUtils;

    @Inject
    SysLogMapper sysLogMapper;

    @Override
    public void record(LogRecord logRecord) {
        String ipAddr = ipUtils.getIpAddr(request);
        logRecord.setIp(ipAddr);
        sysLogService.registerByLogRecord(logRecord);
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        List<SysLog> sysLogs = sysLogService.queryByBizNoAndType(bizNo, type);
        return sysLogs.stream().map(sysLog -> {
            return sysLogMapper.toLogRecord(sysLog);
        }).collect(Collectors.toList());
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        List<SysLog> sysLogs = sysLogService.queryByBizNoAndType(bizNo, type);
        List<LogRecord> result = new ArrayList<>();
        for (SysLog sysLog : sysLogs) {
            if (subType != null && !subType.equals(sysLog.getSubType())) {
                continue;
            }
            result.add(sysLogMapper.toLogRecord(sysLog));
        }
        return result;
    }

}
