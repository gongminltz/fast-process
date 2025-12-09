package com.gm.process.constants;

import lombok.Getter;

/**
 * 任务类型
 */
@Getter
public enum TaskTypeEnum {
    /**
     * 所有任务
     */
    ALL(0),

    /**
     * 待处理任务
     */
    PENDING(1),

    /**
     * 已完成的任务
     */
    FINISHED(2);

    private final int type;

    TaskTypeEnum(int type) {
        this.type = type;
    }
}
