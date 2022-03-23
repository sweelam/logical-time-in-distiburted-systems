package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.impl.VectorEventClockFactory;
import com.ms.time.manager.dto.ScalarPublishedEvent;

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
    public ScalarPublishedEvent buildEvent(String message, String serviceName) {
        return null;
    }

    @Override
    public ScalarPublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName) {
        return null;
    }
}
