package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.impl.VectorEventClockFactory;
import com.ms.time.manager.dto.VectorPublishedEvent;

class VectorClockEventProvider implements EventTimeManager {
    private VectorEventClockFactory eventClockFactory;

    public VectorClockEventProvider(VectorEventClockFactory eventClockFactory) {
        this.eventClockFactory = eventClockFactory;
    }

    @Override
    public void registerService(String serviceName) {
        eventClockFactory.registerService(serviceName);
    }

    @Override
    public VectorPublishedEvent buildEvent(String message, String serviceName) {
        return new VectorPublishedEvent(message, eventClockFactory.generateClockInstance(serviceName));
    }

    @Override
    public VectorPublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return null;
    }
}
