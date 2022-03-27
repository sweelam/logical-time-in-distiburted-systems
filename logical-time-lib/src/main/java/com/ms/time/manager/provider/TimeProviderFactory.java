package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.impl.ScalarClockFactory;
import com.ms.time.manager.domain.impl.VectorClockFactory;
import com.ms.time.manager.types.TimeType;

public class TimeProviderFactory {
    private TimeProviderFactory() {}
    private static ScalarEventProvider scalarEventProvider;
    private static VectorClockEventProvider vectorClockEventProvider;

    public static synchronized EventTimeManager ofType(TimeType timeType) {
        if (scalarEventProvider == null) {
            scalarEventProvider = new ScalarEventProvider(new ScalarClockFactory());
        }

        if (vectorClockEventProvider == null) {
            vectorClockEventProvider = new VectorClockEventProvider(new VectorClockFactory());
        }

        return switch (timeType) {
            case VECTOR_CLOCK -> vectorClockEventProvider;
            default -> scalarEventProvider;
        };
    }
}
