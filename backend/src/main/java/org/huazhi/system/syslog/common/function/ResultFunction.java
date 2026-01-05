package org.huazhi.system.syslog.common.function;

import org.huazhi.system.syslog.common.service.IParseFunction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResultFunction implements IParseFunction {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public String functionName() {
        return "RESULT";
    }

    @Override
    public String apply(Object value) {
        if (value == null) {
            return "";
        }

        try {
            // 将对象转成 JsonNode
            JsonNode jsonNode = MAPPER.valueToTree(value);
            if (jsonNode.has("code") && 10200 == jsonNode.get("code").asInt()) {
                return "成功";
            } else {
                String msg = jsonNode.has("msg") ? jsonNode.get("msg").asText() : "未知原因";
                return "失败，失败原因：" + msg;
            }
        } catch (Exception e) {
            // 转换失败，也返回空
            return "";
        }
    }
}
