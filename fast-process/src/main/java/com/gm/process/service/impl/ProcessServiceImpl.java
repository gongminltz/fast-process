package com.gm.process.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gm.process.bo.ProcessInfoBo;
import com.gm.process.bo.TaskInfoBo;
import com.gm.process.constants.ProcessStatusEnum;
import com.gm.process.constants.TaskTypeEnum;
import com.gm.process.entity.ProcessInfo;
import com.gm.process.entity.TaskInfo;
import com.gm.process.mapper.ProcessInfoMapper;
import com.gm.process.service.ProcessService;
import com.gm.process.service.TaskService;
import com.gm.process.vo.ProcessInfoVo;
import com.gm.process.vo.TaskInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProcessServiceImpl
 *
 * @author gongmin
 * @date 2025/10/28 10:50
 */
@Slf4j
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessInfoMapper, ProcessInfo> implements ProcessService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Resource
    private ProcessInfoMapper processInfoMapper;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    public void deployDefinition(String bpmnFileName) {
        log.debug("<deployProcessDefinition> 发布流程定义 " + bpmnFileName);

        // 发布流程定义
        repositoryService.createDeployment()
                .addClasspathResource(bpmnFileName)
                .deploy();
    }

    @Transactional(rollbackFor = Exception.class)
    public String createInstance(@NotNull String processDefinitionId, final ProcessInfoBo processInfo,
                                 Map<String, Object> variables) {
        log.debug("<createInstance> 基于流程定义id {} 创建流程实例，流程信息: {}，变量: {}",
                processDefinitionId, JSONObject.toJSONString(processInfo), JSONObject.toJSONString(variables));

        // 创建流程实例
        final ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);

        // 保存流程实例信息
        ProcessInfo processInfo1 = new ProcessInfo() {{
            setCreateDate(new Date());
            setCreateUser(processInfo.getCreateUser());
            setProcessInstanceId(processInstance.getProcessInstanceId());
            setRootProcessInstanceId(processInstance.getProcessInstanceId());
            setName(processInfo.getName());
            setType(processInfo.getType().getType());
            setRemark(processInfo.getRemark());
            // 0表示实例刚刚创建成功
            setStatus(ProcessStatusEnum.PROCESS_START.getStatus());
        }};

        processInfoMapper.insert(processInfo1);

        return processInstance.getProcessInstanceId();
    }

    public List<ProcessInfoVo> getProcessInfo(@NotNull String user, @NotNull TaskTypeEnum taskType) {
        log.debug("<getProcessInfo> 获取用户{}的任务列表，任务类型为{}", user, taskType);

        // 获取用户当前所有待处理的任务
        List<TaskInfoBo> taskInfoBos = taskService.getTasks(user, taskType);
        if (CollectionUtils.isEmpty(taskInfoBos)) {
            return null;
        }

        // 获取所有待处理的流程id
        List<String> pendingProcessInstanceIds =
                taskInfoBos.stream().map(TaskInfoBo::getProcessInstanceId).collect(Collectors.toList());
        List<ProcessInfo> processInfos = getByProcessInstanceIds(pendingProcessInstanceIds);
        if (CollectionUtils.isEmpty(processInfos)) {
            log.debug("<getProcessInfo> processInfos为空");
            return null;
        }

        List<ProcessInfoVo> pendingProcessInfoVos = new ArrayList<>(processInfos.size());
        TaskInfoBo taskInfoBo;

        for (ProcessInfo processInfo : processInfos) {
            ProcessInfoVo pendingProcessInfoVo = new ProcessInfoVo();
            BeanUtils.copyProperties(processInfo, pendingProcessInfoVo);

            // 查找当前流程实例下的待办任务
            taskInfoBo = taskInfoBos.stream()
                    .filter(tempTaskInfoBo -> processInfo.getProcessInstanceId().equals(tempTaskInfoBo.getProcessInstanceId()))
                    .findFirst().get();

            pendingProcessInfoVo.setTaskId(taskInfoBo.getTaskId());
            pendingProcessInfoVo.setTaskKey(taskInfoBo.getTaskKey());
            pendingProcessInfoVo.setProcessDefinitionId(taskInfoBo.getProcessDefinitionId());

            pendingProcessInfoVos.add(pendingProcessInfoVo);
        }

        return pendingProcessInfoVos;
    }

    public List<ProcessInfo> getByProcessInstanceIds(Collection<String> processInstanceIds) {
        LambdaQueryWrapper<ProcessInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProcessInfo::getProcessInstanceId, processInstanceIds);
        return list(queryWrapper);
    }

    @Override
    public List<String> getSubProcessInstanceIds(@NotNull String rootProcessInstanceId) {
        log.debug("<getSubProcessInstanceIds> 获取根流程实例id为{}的子流程实例id列表", rootProcessInstanceId);

        LambdaQueryWrapper<ProcessInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProcessInfo::getProcessInstanceId);
        queryWrapper.eq(ProcessInfo::getRootProcessInstanceId, rootProcessInstanceId)
                .ne(ProcessInfo::getProcessInstanceId, rootProcessInstanceId);

        List<ProcessInfo> processInfos = list(queryWrapper);
        return processInfos.stream()
                .map(ProcessInfo::getProcessInstanceId)
                .collect(Collectors.toList());
    }

    @Override
    public ProcessInfo getByProcessInstanceId(@NotNull String processInstanceId) {
        log.debug("<getByProcessInstanceId> 通过流程实例id获取流程实例信息，流程实例id为" + processInstanceId);

        LambdaQueryWrapper<ProcessInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessInfo::getProcessInstanceId, processInstanceId);
        return getOne(queryWrapper);
    }

    @Override
    public void updateProcessInstanceStatus(@NotNull String processInstanceId, int status) {
        log.info("<updateProcessInstanceStatus> 更新流程实例 {} 的状态为 {}", processInstanceId, status);

        LambdaUpdateWrapper<ProcessInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessInfo::getProcessInstanceId, processInstanceId)
                .set(ProcessInfo::getStatus, status);
        update(updateWrapper);
    }

    @Override
    public List<TaskInfoVo> getProcessExecutionHistory(@NotNull String processInstanceId,
                                                       boolean includeSubProcessInstances) {
        log.debug("<getProcessExecutionHistory> 获取流程 {} 的执行记录", processInstanceId);

        List<String> processInstanceIds = new ArrayList<String>() {{
            add(processInstanceId);
        }};

        if (includeSubProcessInstances) {
            // 取子流程实例 ID 列表
            List<String> subProcInstIds = getSubProcessInstanceIds(processInstanceId);
            processInstanceIds.addAll(subProcInstIds);
        }


        // 从流程引擎中获取流程流转记录
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceIdIn(processInstanceIds)
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();

        // 获取所有的任务id
        List<String> taskIds = taskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());

        // 获取任务附件信息
        List<TaskInfo> taskInfos = taskService.getTaskAttachmentInfo(taskIds);
        if (CollectionUtils.isEmpty(taskInfos)) {
            log.warn("<getProcessExecutionHistory> 查找任务附件信息失败，taskIds = {}", JSONObject.toJSONString(taskIds));
            taskInfos = Collections.EMPTY_LIST;
        }

        List<TaskInfoVo> taskInfoVos = new ArrayList<>(taskInstances.size());
        TaskInfoVo taskInfoVo;
        TaskInfo taskInfo;

        for (HistoricTaskInstance taskInstance : taskInstances) {
            taskInfoVo = new TaskInfoVo() {{
                setTaskId(taskInstance.getId());
                setStartDate(taskInstance.getCreateTime());
                setEndDate(taskInstance.getEndTime());
            }};

            taskInfo = taskInfos.stream().filter(tmpTaskInfo -> tmpTaskInfo.getTaskId().equals(taskInstance.getId()))
                    .findFirst().orElse(null);
            if (taskInfo == null) {
                log.warn("<getProcessExecutionHistory> 没有找到任务{}的附加信息", taskInstance.getId());
                continue;
            }
            if (StringUtils.isEmpty(taskInfo.getOperator())) {
                log.debug("<getProcessExecutionHistory> 任务{}没有操作人员，应该是并行任务自动结束，不列入到执行记录中", taskInstance.getId());
                continue;
            }

            taskInfoVo.setOperator(taskInfo.getOperator());
            taskInfoVo.setOperateType(taskInfo.getOperateType());
            taskInfoVo.setOperateName(taskInfo.getOperateName());
            taskInfoVo.setAttachmentInfo(taskInfo.getAttachmentInfo());

            taskInfoVos.add(taskInfoVo);
        }

        return taskInfoVos;
    }
}
