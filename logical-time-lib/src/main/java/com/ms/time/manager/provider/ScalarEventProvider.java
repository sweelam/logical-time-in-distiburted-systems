package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.ScalarEventClockFactory;
import com.ms.time.manager.dto.PublishedEvent;

class ScalarEventProvider implements EventTimeManager {
    private ScalarEventClockFactory eventClockFactory;

    public ScalarEventProvider(ScalarEventClockFactory eventClockFactory) {
        this.eventClockFactory = eventClockFactory;
    }

    @Override
    public void registerService(String serviceName) {
        eventClockFactory.registerService(serviceName);
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName) {
        return new PublishedEvent(message, eventClockFactory.generateClockInstance(serviceName));
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return new PublishedEvent(message, eventClockFactory.generateClockInstance(serviceName, prevEventServiceName));
    }
}
