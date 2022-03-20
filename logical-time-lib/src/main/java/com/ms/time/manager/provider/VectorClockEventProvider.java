package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.VectorEventClockFactory;
import com.ms.time.manager.dto.PublishedEvent;

class VectorClockEventProvider implements EventTimeManager {
    private VectorEventClockFactory eventClockFactory;

    public VectorClockEventProvider(VectorEventClockFactory eventClockFactory) {
        this.eventClockFactory = eventClockFactory;
    }

    @Override
    public void registerService(String serviceName) {
        return;
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName) {
        return null;
    }

    @Override
    public PublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return null;
    }
}
