package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.ScalarEventClockFactory;
import com.ms.time.manager.domain.VectorEventClockFactory;
import com.ms.time.manager.types.TimeType;

public class TimeProviderFactory {
    private TimeProviderFactory() {}
    private static ScalarEventProvider scalarEventProvider;
    private static VectorClockEventProvider vectorClockEventProvider;

    public synchronized static EventTimeManager ofType(TimeType timeType) {
        if (scalarEventProvider == null) {
            scalarEventProvider = new ScalarEventProvider(new ScalarEventClockFactory());
        }

        if (vectorClockEventProvider == null) {
            vectorClockEventProvider = new VectorClockEventProvider(new VectorEventClockFactory());
        }

        return switch (timeType) {
            case VECTOR_CLOCK -> vectorClockEventProvider;
            default -> scalarEventProvider;
        };
    }
}
