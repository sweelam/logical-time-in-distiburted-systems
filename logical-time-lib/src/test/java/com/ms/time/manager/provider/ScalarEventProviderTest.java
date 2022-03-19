package com.ms.time.manager.provider;

import com.ms.time.manager.domain.EventClockFactory;
import com.ms.time.manager.types.TimeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScalarEventProviderTest {
    private static EventClockFactory eventClockFactory;
    private static ScalarEventProvider scalarEventProvider;
    final static String SENDER = "message-sender-service";
    final static String RECEIVER = "message-sender-receiver";

    @BeforeAll
    public static void init() {
        eventClockFactory = new EventClockFactory();
        scalarEventProvider = new ScalarEventProvider(eventClockFactory);
    }

    @Test
    void registerService() {
        scalarEventProvider.registerService(SENDER);
        var result = eventClockFactory.getEventClockInstance(SENDER, "");
        Assertions.assertNotNull(result, "object is not created");
    }

    @Test
    void ofType() {
        assertTrue(scalarEventProvider.ofType(TimeType.SCALER_CLOCK) instanceof ScalarEventProvider);
    }

    @Test
    void buildEvent() {
        scalarEventProvider.registerService(SENDER);
        var result =
                scalarEventProvider.buildEvent("Whatsapp message to abc", SENDER, "");
        Assertions.assertNotNull(result, "object is not created");
    }



    @Test
    void buildEventFromOldEvent() {
        scalarEventProvider.registerService(SENDER);
        scalarEventProvider.registerService(RECEIVER);

        var result =
                scalarEventProvider.buildEvent("Whatsapp message to abc", SENDER, RECEIVER);

        Assertions.assertNotNull(result, "object is not created");
        assertTrue(eventClockFactory.getCurrentClock(SENDER).getClock() > eventClockFactory.getCurrentClock(RECEIVER).getClock());
    }
}