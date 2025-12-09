package com.gm.process.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配置文件中任务信息格式
 *
 * @author gongmin
 * @date 2025/10/29 14:02
 */
@Getter
@Setter
public class ConfigTaskInfo {
    /**
     * bpmn文件中task的id
     */
    private String key;

    /**
     * 当流程进行到这个任务时流程的状态
     */
    private Integer status;

    /**
     * 当流程进行到这个任务时流程的状态名称
     */
    private String statusName;

    /**
     * 有多个操作按钮时以逗号隔开
     */
    private String operateNames;

    /**
     * 配置哪些操作类型需要删除其他并行任务
     */
    private List<Integer> deleteMultiInstanceOperateTypes;
}
