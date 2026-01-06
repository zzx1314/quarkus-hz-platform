package org.huazhi.drones.business.command.entity.tcpdto.task;


import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Task {

    /** 任务类型 */
    private String taskType;

    /**
     * 任务元数据
     * - 文件任务：文件名 / 大小 / hash
     * - 消息任务：编码 / 压缩
     *
     * 这里允许灵活扩展
     */
    private JsonNode taskMeta;


    /** 任务状态 */
    private String status;

}

