package com.demo;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 10:54
 */
public class SetAllRejectListener implements ExecutionListener {
    public void notify(DelegateExecution exec) {
        exec.setVariable("allPassed", false);
    }
}