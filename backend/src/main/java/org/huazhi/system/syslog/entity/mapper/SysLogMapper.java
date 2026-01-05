package org.huazhi.system.syslog.entity.mapper;


import java.util.Map;

import org.huazhi.system.syslog.common.beans.CodeVariableType;
import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.entity.SysLog;
import org.huazhi.system.syslog.entity.dto.SysLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysLogMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    SysLog toEntity(SysLogDto dto);

    @Mapping(target = "id", expression = "java(convertId(logRecord.getId()))")
    @Mapping(target = "codeVariable", expression = "java(mapCodeVariable(logRecord.getCodeVariable()))")
    SysLog toEntityByLogRecord(LogRecord logRecord);

    @Mapping(target = "codeVariable", expression = "java(unmapCodeVariable(entity.getCodeVariable()))")
    @Mapping(target = "id", expression = "java(entity.getId())")
    LogRecord toLogRecord(SysLog entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SysLogDto dto, @MappingTarget SysLog entity);


    // Serializable -> Long
    default Long convertId(java.io.Serializable id) {
        if (id == null) return null;
        if (id instanceof Number) return ((Number) id).longValue();
        try {
            return Long.parseLong(id.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert Serializable id to Long: " + id, e);
        }
    }

    // Map -> JSON String
    default String mapCodeVariable(Map<CodeVariableType, Object> codeVariable) {
        if (codeVariable == null || codeVariable.isEmpty()) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(codeVariable);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize codeVariable to JSON", e);
        }
    }

    // JSON String -> Map<CodeVariableType,Object>
    default Map<CodeVariableType, Object> unmapCodeVariable(String codeVariableJson) {
        if (codeVariableJson == null || codeVariableJson.isEmpty()) return null;
        try {
            // 注意 MapKey 类型要转换成 CodeVariableType
            @SuppressWarnings("unchecked")
            Map<String, Object> map = OBJECT_MAPPER.readValue(codeVariableJson, Map.class);
            Map<CodeVariableType, Object> result = new java.util.HashMap<>();
            map.forEach((k, v) -> result.put(CodeVariableType.valueOf(k), v));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize codeVariable JSON: " + codeVariableJson, e);
        }
    }
}
