package com.gm.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gm.process.bo.ProcessInfoBo;
import com.gm.process.constants.TaskTypeEnum;
import com.gm.process.entity.ProcessInfo;
import com.gm.process.vo.ProcessInfoVo;
import com.gm.process.vo.TaskInfoVo;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 流程相关功能
 *
 * @author gongmin
 * @date 2025/10/28 10:45
 */
public interface ProcessService extends IService<ProcessInfo> {
    /**
     * 发布流程定义
     *
     * @param bpmnFileName bpmn流程定义文件名称
     */
    void deployDefinition(@NotNull String bpmnFileName);

    /**
     * 通过流程定义id创建流程实例
     *
     * @param processDefinitionId 流程定义id
     * @param processInfo         流程详情信息
     * @param variables           流程参数
     * @return 返回流程实例id
     */
    String createInstance(@NotNull String processDefinitionId, ProcessInfoBo processInfo, Map<String, Object> variables);

    /**
     * 获取用户流程信息
     *
     * @param user     用户
     * @param taskType 任务类型
     * @return 流程列表
     */
    List<ProcessInfoVo> getProcessInfo(@NotNull String user, @NotNull TaskTypeEnum taskType);

    /**
     * 批量获取流程实例信息
     *
     * @param processInstanceIds 流程实例id列表
     * @return 流程实例信息列表
     */
    List<ProcessInfo> getByProcessInstanceIds(Collection<String> processInstanceIds);

    /**
     * 获取指定流程实例的子流程实例id列表
     *
     * @param rootProcessInstanceId 根流程实例id
     * @return 子流程实例id列表
     */
    List<String> getSubProcessInstanceIds(@NotNull String rootProcessInstanceId);

    /**
     * 获取流程实例信息
     *
     * @param processInstanceId 流程实例id
     * @return 流程实例信息
     */
    ProcessInfo getByProcessInstanceId(@NotNull String processInstanceId);

    /**
     * 更新流程实例状态
     *
     * @param processInstanceId 流程实例id
     * @param status            状态
     */
    void updateProcessInstanceStatus(@NotNull String processInstanceId, int status);

    /**
     * 获取流程的执行记录
     *
     * @param processInstanceId          流程实例id
     * @param includeSubProcessInstances 需要需要查询子流程执行记录
     * @return 执行记录列表
     */
    List<TaskInfoVo> getProcessExecutionHistory(@NotNull String processInstanceId, boolean includeSubProcessInstances);
}
