package com.gm.process.config;

import com.gm.process.MyGlobalEventListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class FlowableConfig {

    @Autowired
    private MyGlobalEventListener myGlobalEventListener;

    @Autowired
    private SpringProcessEngineConfiguration cfg;

    @PostConstruct
    public void register() {
        log.debug("<register> 开始为flowable注册全局事件监听器");
        cfg.getEventDispatcher().addEventListener(myGlobalEventListener);
    }
}
