package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.impl.ScalarClockFactory;
import com.ms.time.manager.dto.ScalarPublishedEvent;

class ScalarEventProvider implements EventTimeManager {
    private ScalarClockFactory eventClockFactory;

    public ScalarEventProvider(ScalarClockFactory eventClockFactory) {
        this.eventClockFactory = eventClockFactory;
    }

    @Override
    public void registerService(String serviceName) {
        eventClockFactory.registerService(serviceName);
    }

    @Override
    public ScalarPublishedEvent buildEvent(String message, String serviceName) {
        return new ScalarPublishedEvent(message, eventClockFactory.generateClockInstance(serviceName));
    }

    @Override
    public ScalarPublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return new ScalarPublishedEvent(message, eventClockFactory.generateClockInstance(serviceName, prevEventServiceName));
    }
}
