package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.EventClockFactory;
import com.ms.time.manager.dto.PublishedEvent;

public class ScalarEventProvider implements EventTimeManager {
    private EventClockFactory eventClockFactory;

    public ScalarEventProvider(EventClockFactory eventClockFactory) {
        this.eventClockFactory = eventClockFactory;
    }

    @Override
    public void registerService(String serviceName) {
        eventClockFactory.registerService(serviceName);
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName) {
        return new PublishedEvent(message, eventClockFactory.getEventClockInstance(serviceName));
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return new PublishedEvent(message, eventClockFactory.getEventClockInstance(serviceName, prevEventServiceName));
    }
}
