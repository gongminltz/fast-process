package com.demo;

import com.gm.process.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 10:52
 */
@Slf4j
public class HqCompleteListener implements TaskListener {
    private static RuntimeService runtimeService = null;

    public void notify(DelegateTask task) {
        int operateType = (int) task.getVariable("operateType");
        log.debug("<notify> operateType = " + operateType);

        boolean approved = operateType == 0;
        task.setVariable("hqResult", approved);
        if (!approved) {
            if (runtimeService == null) {
                runtimeService = ApplicationUtils.get().getBean(RuntimeService.class);
            }

            runtimeService.signalEventReceived("rejectSignal");
        }
    }
}
