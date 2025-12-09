package com.gm.process.handler;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监听子任务错误事件
 *
 * @author gongmin
 * @date 2025/11/4 15:54
 */
@Slf4j
@Service
public class RejectListener implements ExecutionListener {
    @Resource
    private TaskService taskService;

    @Override
    public void notify(DelegateExecution execution) {
        // 获取当前流程实例
        String processInstanceId = execution.getProcessInstanceId();
        log.debug("<notify> 流程实例{}中的并行网关中的一个分支({})异常结束，其他分支任务将被删除", processInstanceId, execution.getId());

        // 获取当前流程实例下的待执行任务列表
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();

        for (Task task : tasks) {
            if (!task.getExecutionId().equals(execution.getId())) {
                // 需要终止其他分支的执行实例
                log.debug("<notify> 任务(id = {}, name = {}，执行人 = {})被删除", task.getId(), task.getName(), task.getAssignee());
                taskService.deleteTask(task.getId());
            }
        }
    }
}
