package com.demo;

import com.gm.process.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

import java.util.List;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 10:50
 */
@Slf4j
public class YgCompleteListener implements TaskListener {
    private static RuntimeService runtimeService = null;

    public void notify(DelegateTask task) {
        int operateType = (int) task.getVariable("operateType");
        log.debug("<notify> operateType = " + operateType);

        boolean approved = operateType == 0;
        task.setVariable("ygResult", approved);
        if (!approved) {
            // 立即触发中断信号
            if (runtimeService == null) {
                runtimeService = ApplicationUtils.get().getBean(RuntimeService.class);
            }

//            List<Execution> sigExecs = runtimeService.createExecutionQuery()
//                    .signalEventSubscriptionName("rejectSignal")
//                    .processInstanceId(task.getProcessInstanceId())
//                    .list();
//            log.info("<ok>");

            runtimeService.signalEventReceived("rejectSignal");
        }
    }
}
