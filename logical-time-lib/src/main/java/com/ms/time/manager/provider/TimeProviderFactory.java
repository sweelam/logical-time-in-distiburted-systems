package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.ScalarEventClockFactory;
import com.ms.time.manager.domain.VectorEventClockFactory;
import com.ms.time.manager.types.TimeType;

public class TimeProviderFactory {
    private TimeProviderFactory() {}

    public static EventTimeManager ofType(TimeType timeType) {
        return switch (timeType) {
            case VECTOR_CLOCK -> new VectorClockEventProvider(new VectorEventClockFactory());
            default -> new ScalarEventProvider(new ScalarEventClockFactory());
        };
    }
}
