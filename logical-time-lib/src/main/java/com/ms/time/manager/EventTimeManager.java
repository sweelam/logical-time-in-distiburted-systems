package com.ms.time.manager;

import com.ms.time.manager.domain.EventClockFactory;
import com.ms.time.manager.dto.PublishedEvent;
import com.ms.time.manager.provider.ScalarEventProvider;
import com.ms.time.manager.provider.VectorClockEventProvider;
import com.ms.time.manager.types.TimeType;

public interface EventTimeManager {
    void registerService(String serviceName);

    default EventTimeManager ofType(TimeType timeType) {
        return switch (timeType) {
            case VECTOR_CLOCK -> new VectorClockEventProvider(new EventClockFactory());
            default -> new ScalarEventProvider(new EventClockFactory());
        };
    }

    PublishedEvent buildEvent(String message, String serviceName);

    PublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName);
}
