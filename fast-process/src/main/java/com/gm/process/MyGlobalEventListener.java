package com.gm.process;

import com.gm.process.config.ConfigTaskInfo;
import com.gm.process.constants.ProcessStatusEnum;
import com.gm.process.constants.ProcessTypeEnum;
import com.gm.process.entity.ProcessInfo;
import com.gm.process.service.ProcessConfigService;
import com.gm.process.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableActivityCancelledEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 全局监听器，监听任务事件
 *
 * @author gongmin
 * @date 2025/10/28 8:45
 */
@Slf4j
@Component
public class MyGlobalEventListener implements FlowableEventListener {
    @Resource
    private ProcessConfigService processConfigService;

    @Resource
    private ProcessService processService;

    @Override
    public void onEvent(FlowableEvent event) {
        log.debug("<onEvent> 任务发生变化，事件类型：" + event.getType().name());

        // 示例：监听任务创建事件
        if (FlowableEngineEventType.TASK_CREATED.name().equals(event.getType().name())) {
            // 获取事件关联的实体
            Object entity = ((FlowableEntityEventImpl) event).getEntity();
            if (entity instanceof TaskEntity) {
                TaskEntity taskEntity = (TaskEntity) entity;

                ConfigTaskInfo taskInfo = processConfigService.get(taskEntity.getProcessDefinitionId(), taskEntity.getTaskDefinitionKey());
                if (taskInfo == null) {
                    log.error("<onEvent> 没有找到流程 {} 中的任务 {}", taskEntity.getProcessDefinitionId(), taskEntity.getTaskDefinitionKey());
                    return;
                }

                ProcessInfo processInfo = processService.getByProcessInstanceId(taskEntity.getProcessInstanceId());
                if (processInfo == null) {
                    log.error("<onEvent> 没有在gm_process_info表中查找到{}流程的信息", taskEntity.getProcessInstanceId());
                    return;
                }

                processService.updateProcessInstanceStatus(processInfo.getRootProcessInstanceId(), taskInfo.getStatus());
            }
        } else if (FlowableEngineEventType.PROCESS_CREATED.name().equals(event.getType().name())) {
            // 流程创建
            Object entity = ((org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl) event).getEntity();
            if (entity instanceof ExecutionEntity) {
                ExecutionEntity executionEntity = (ExecutionEntity) entity;
                if (!executionEntity.getProcessInstanceId().equals(executionEntity.getRootProcessInstanceId())) {
                    // 子流程创建
                    log.debug("<onEvent> 创建子流程，子流程id为{}，根流程id为{}", executionEntity.getProcessInstanceId(),
                            executionEntity.getRootProcessInstanceId());

                    // 查找根流程信息
                    ProcessInfo rootProcessInfo = processService.getByProcessInstanceId(executionEntity.getRootProcessInstanceId());

                    // 保存子流程信息
                    ProcessInfo processInfo = new ProcessInfo() {{
                        setRootProcessInstanceId(executionEntity.getRootProcessInstanceId());
                        setProcessInstanceId(executionEntity.getProcessInstanceId());
                        setStatus(ProcessStatusEnum.PROCESS_START.getStatus());
                        setRemark(rootProcessInfo.getRemark());
                        setType(ProcessTypeEnum.SUB_PROCESS.getType());
                        setName(rootProcessInfo.getName());
                        setCreateDate(new Date());
                    }};
                    processService.save(processInfo);
                }
            }
        } else if (FlowableEngineEventType.ACTIVITY_COMPLETED.name().equals(event.getType().name())) {
            FlowableActivityEventImpl entity = (FlowableActivityEventImpl) event;

            ConfigTaskInfo taskInfo = processConfigService.get(entity.getProcessDefinitionId(), entity.getActivityId());
            if (taskInfo == null) {
                log.error("<onEvent> 没有找到流程 {} 中的节点 {}", entity.getProcessDefinitionId(), entity.getActivityId());
                return;
            }

            ProcessInfo processInfo = processService.getByProcessInstanceId(entity.getProcessInstanceId());
            if (processInfo == null) {
                log.error("<onEvent> 没有在gm_process_info表中查找到{}流程的信息", entity.getProcessInstanceId());
                return;
            }

            processService.updateProcessInstanceStatus(processInfo.getRootProcessInstanceId(), taskInfo.getStatus());
        }
    }

    @Override
    public boolean isFailOnException() {
        // 返回false，确保监听器内的异常不会影响主流程
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}