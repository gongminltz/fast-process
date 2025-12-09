package com.gm.process.constants;

import lombok.Getter;

/**
 * 操作类型
 *
 * @author gongmin
 * @date 2025/11/10 14:47
 */
@Getter
public enum OperateTypeEnum {
    /**
     * 大于等于0的操作类型是用户执行的操作类型（如同意、打回、不同意等）
     * 小于0的操作类型是程序自动处理的操作类型
     */
    TERMINATE(-1);

    private final int type;

    OperateTypeEnum(int type) {
        this.type = type;
    }
}
