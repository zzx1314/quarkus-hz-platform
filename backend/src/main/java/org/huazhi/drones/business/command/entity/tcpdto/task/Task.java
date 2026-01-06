package org.huazhi.drones.business.command.entity.tcpdto.task;

import org.huazhi.drones.business.command.entity.tcpdto.enumdata.TaskType;

import lombok.Data;

@Data
public class Task {

    /** 任务类型 */
    private TaskType taskType;

    /** 业务任务 ID（文件ID / 任务ID） */
    private String taskId;

    /**
     * 任务元数据
     * - 文件任务：文件名 / 大小 / hash
     * - 消息任务：编码 / 压缩
     *
     * 这里允许灵活扩展
     */
    private Object taskMeta;

}

