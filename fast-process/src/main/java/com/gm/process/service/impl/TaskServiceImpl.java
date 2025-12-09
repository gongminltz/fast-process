package com.gm.process.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gm.process.bo.TaskInfoBo;
import com.gm.process.config.ConfigTaskInfo;
import com.gm.process.constants.TaskCompleteRequestParam;
import com.gm.process.constants.TaskTypeEnum;
import com.gm.process.entity.TaskInfo;
import com.gm.process.mapper.TaskInfoMapper;
import com.gm.process.service.ProcessConfigService;
import com.gm.process.service.TaskService;
import com.gm.process.vo.TaskOperateInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * TaskServiceImpl
 *
 * @author gongmin
 * @date 2025/10/29 11:08
 */
@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskService {
    @Resource
    private org.flowable.engine.TaskService taskService;

    @Resource
    private ProcessConfigService processConfigService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    public List<TaskInfoBo> getTasks(@NotNull String user, @NotNull TaskTypeEnum taskType) {
        switch (taskType) {
            case ALL: {
                log.debug("<getTasks> 获取用户{}的所有任务", user);
                return getAllTasks(user);
            }
            case PENDING: {
                log.debug("<getTasks> 获取用户{}的待处理任务", user);
                return getPendingTasks(user);
            }
            case FINISHED: {
                log.debug("<getTasks> 获取用户{}的已完成任务", user);
                return getFinishedTasks(user);
            }
            default: {
                log.debug("<getTasks> 目前不支持 {} 该任务类型的处理", taskType.getType());
                return null;
            }
        }
    }

    @Override
    public List<TaskInfoBo> getPendingTasks(@NotNull String user) {
        log.debug("<getPendingTasks> 获取用户{}的待处理任务", user);

        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(user)
                .active()
                .orderByTaskCreateTime().asc()
                .list();

        List<TaskInfoBo> taskInfoBos = new ArrayList<>(tasks.size());

        // 遍历任务列表并输出任务信息
        for (final Task task : tasks) {
            taskInfoBos.add(new TaskInfoBo() {{
                setTaskId(task.getId());
                setTaskKey(task.getTaskDefinitionKey());
                setProcessInstanceId(task.getProcessInstanceId());
                setProcessDefinitionId(task.getProcessDefinitionId());
            }});
        }

        return taskInfoBos;
    }

    @Override
    public List<TaskInfoBo> getFinishedTasks(@NotNull String user) {
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(user)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        List<TaskInfoBo> taskInfoBos = new ArrayList<>(taskList.size());
        TaskInfoBo taskInfoBo;
        for (HistoricTaskInstance task : taskList) {
            taskInfoBo = new TaskInfoBo() {{
                setTaskId(task.getId());
                setTaskKey(task.getTaskDefinitionKey());
                setProcessInstanceId(task.getProcessInstanceId());
                setProcessDefinitionId(task.getProcessDefinitionId());
            }};
            taskInfoBos.add(taskInfoBo);
        }

        return taskInfoBos;
    }

    @Override
    public List<TaskInfoBo> getAllTasks(@NotNull String user) {
        // 获取待处理任务
        List<TaskInfoBo> taskInfoBos = getPendingTasks(user);

        // 获取已完成任务
        taskInfoBos.addAll(getFinishedTasks(user));

        return taskInfoBos;
    }

    @Override
    public List<TaskOperateInfoVo> getTaskOperates(@NotNull String processDefinitionId, @NotNull String taskKey) {
        log.debug("<getTaskOperates> 获取指定任务操作信息, 流程定义id为 {}, taskKey = {}", processDefinitionId, taskKey);

        ConfigTaskInfo configTaskInfo = processConfigService.get(processDefinitionId, taskKey);
        if (configTaskInfo == null) {
            log.warn("<getTaskOperates> 没有在配置文件中的流程定义 {} 下找到任务 {} 的配置信息", processDefinitionId, taskKey);
            return null;
        }

        if (StringUtils.isEmpty(configTaskInfo.getOperateNames())) {
            log.warn("<getTaskOperates> 在配置文件中的流程定义 {} 下的任务 {} 的配置信息没有配置operateNames参数", processDefinitionId, taskKey);
            return null;
        }

        String[] operateNames = configTaskInfo.getOperateNames().split(",");
        List<TaskOperateInfoVo> taskOperateInfoVos = new ArrayList<>(operateNames.length);
        TaskOperateInfoVo taskOperateInfoVo;
        for (int i = 0; i < operateNames.length; i++) {
            taskOperateInfoVo = new TaskOperateInfoVo();
            taskOperateInfoVo.setOperateName(operateNames[i]);
            taskOperateInfoVo.setOperateType(i);
            taskOperateInfoVos.add(taskOperateInfoVo);
        }

        return taskOperateInfoVos;
    }

    @Override
    public List<TaskInfo> getTaskAttachmentInfo(@NotNull Collection<String> taskIds) {
        log.debug("<getTaskAttachmentInfo> 批量获取任务附加信息, taskIds = {}", JSONObject.toJSONString(taskIds));

        LambdaQueryWrapper<TaskInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TaskInfo::getTaskId, taskIds);

        return list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean completeTask(@NotNull String taskId, String operator, Map<String, Object> variables, String attachmentInfo) {
        log.debug("<completeTask> 任务 {} 完成提交，提交人 {}, 提交参数 {}, 附加信息 {}",
                taskId, operator, JSONObject.toJSONString(variables), attachmentInfo);

        // 判断是否有操作类型
        if (variables == null || StringUtils.isEmpty(variables.get(TaskCompleteRequestParam.OPERATE_TYPE))) {
            log.error("<completeTask> 没有找到参数 {}", TaskCompleteRequestParam.OPERATE_TYPE);
            return false;
        }

        // 完成任务
        taskService.complete(taskId, variables);

        // 保存任务附加信息
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(taskId);
        taskInfo.setAttachmentInfo(attachmentInfo);
        taskInfo.setCompleteDate(new Date());
        taskInfo.setOperator(operator);
        taskInfo.setOperateType((Integer) variables.get(TaskCompleteRequestParam.OPERATE_TYPE));
        taskInfo.setOperateName((String) variables.get(TaskCompleteRequestParam.OPERATE_NAME));
        taskInfo.setVariables(JSONObject.toJSONString(variables));
        save(taskInfo);

        return true;
    }

    @Override
    public Task getTaskInfo(@NotNull String taskId) {
        log.debug("<getTaskInfo> 获取任务id为{}的任务信息", taskId);

        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
    }
}
