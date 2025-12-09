package com.gm.process.service;

import com.gm.process.bo.TaskInfoBo;
import com.gm.process.constants.TaskTypeEnum;
import com.gm.process.entity.TaskInfo;
import com.gm.process.vo.TaskOperateInfoVo;
import org.flowable.task.api.Task;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 任务
 *
 * @author gongmin
 * @date 2025/10/29 11:02
 */
public interface TaskService {
    /**
     * 获取用户的任务列表
     *
     * @param user     用户
     * @param taskType 任务类型
     * @return 任务列表
     */
    List<TaskInfoBo> getTasks(@NotNull String user, @NotNull TaskTypeEnum taskType);

    /**
     * 获取用户的待处理任务列表
     *
     * @param user 用户
     * @return 任务列表
     */
    List<TaskInfoBo> getPendingTasks(@NotNull String user);

    /**
     * 获取用户的已完成任务列表
     *
     * @param user 用户
     * @return 任务列表
     */
    List<TaskInfoBo> getFinishedTasks(@NotNull String user);

    /**
     * 获取用户的所有任务列表
     *
     * @param user 用户
     * @return 任务列表
     */
    List<TaskInfoBo> getAllTasks(@NotNull String user);

    /**
     * 获取任务节点的操作列表
     *
     * @param processDefinitionId 流程定义id
     * @param taskKey             bpmn文件中task的id
     * @return 任务的操作列表
     */
    List<TaskOperateInfoVo> getTaskOperates(@NotNull String processDefinitionId, @NotNull String taskKey);

    /**
     * 批量获取任务附加信息
     *
     * @param taskIds 任务id列表
     * @return 对应的任务附加信息
     */
    List<TaskInfo> getTaskAttachmentInfo(@NotNull Collection<String> taskIds);

    /**
     * 完成任务
     *
     * @param taskId         任务id
     * @param operator       操作者
     * @param variables      变量
     * @param attachmentInfo 任务生成的附加信息，如请假流程中请假天数、请假时间段、请假原因，审批中审批意见等
     * @return 成功返回true，否则返回false
     */
    boolean completeTask(@NotNull String taskId, String operator, Map<String, Object> variables, String attachmentInfo);

    /**
     * 根据任务id获取任务信息
     *
     * @param taskId 任务id
     * @return 任务信息o
     */
    Task getTaskInfo(@NotNull String taskId);
}
