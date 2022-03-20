package com.ms.time.manager.provider;

import com.ms.time.manager.EventTimeManager;
import com.ms.time.manager.domain.ScalarEventClockFactory;
import com.ms.time.manager.types.TimeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScalarEventProviderTest {
    private static ScalarEventClockFactory eventClockFactory;
    private static EventTimeManager scalarEventProvider;
    final static String PREV_EVENT = "message-sender-service";
    final static String SERVICE_SENDER = "message-sender-receiver";

    @BeforeAll
    public static void init() {
        eventClockFactory = new ScalarEventClockFactory();
        scalarEventProvider = TimeProviderFactory.ofType(TimeType.SCALER_CLOCK);
    }

    @Test
    void registerService() {
        scalarEventProvider.registerService(SERVICE_SENDER);
        var result = eventClockFactory.generateClockInstance(SERVICE_SENDER);
        Assertions.assertNotNull(result, "object is not created");
    }

    @Test
    void buildEvent() {
        scalarEventProvider.registerService(SERVICE_SENDER);
        var result =
                scalarEventProvider.buildEvent("Whatsapp message to abc", SERVICE_SENDER);
        Assertions.assertNotNull(result, "object is not created");
    }


    @Test
    void buildEventFromOldEvent() {
        scalarEventProvider.registerService(PREV_EVENT);
        scalarEventProvider.registerService(SERVICE_SENDER);

        scalarEventProvider.buildEvent(" Old event", PREV_EVENT);

        var result =
                scalarEventProvider.buildEvent("Whatsapp message to abc", SERVICE_SENDER, PREV_EVENT);

        Assertions.assertNotNull(result, "object is not created");
            assertTrue(eventClockFactory.getCurrentClock(PREV_EVENT).getClock() < eventClockFactory.getCurrentClock(SERVICE_SENDER).getClock());
    }
}