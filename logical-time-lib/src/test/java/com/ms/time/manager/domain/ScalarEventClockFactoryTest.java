package com.ms.time.manager.domain;

import com.ms.time.manager.exception.LogicalTimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalarEventClockFactoryTest {
    private static ScalarEventClockFactory scalarEventClockFactory;
    private static final String SERVICE_NAME = "B-Service";
    private static final String PREV_SERVICE_NAME = "A-Service";

    @BeforeAll
    static void init() {
        scalarEventClockFactory = new ScalarEventClockFactory();
        scalarEventClockFactory.registerService(PREV_SERVICE_NAME);
        scalarEventClockFactory.registerService(SERVICE_NAME);
    }

    @Test
    void getEventClockInstanceShouldThrowExceptionIfNotRegistered() throws LogicalTimeException {
        scalarEventClockFactory.deRegisterService(SERVICE_NAME);
        Assertions.assertThrows(LogicalTimeException.class,
                () -> scalarEventClockFactory.generateClockInstance(SERVICE_NAME)
        );
        scalarEventClockFactory.registerService(SERVICE_NAME);
    }

    @Test
    void getEventClockInstance() {
        var clock =
                scalarEventClockFactory.generateClockInstance(SERVICE_NAME);

        assertNotNull(clock);
        assertEquals(1, clock.getClock());
    }

    @Test
    void testGetEventClockInstanceWithPrevEvent() {
        var clock =
                scalarEventClockFactory.generateClockInstance(SERVICE_NAME, PREV_SERVICE_NAME);

        assertNotNull(clock);
        assertTrue(scalarEventClockFactory.getCurrentClock(PREV_SERVICE_NAME).getClock() <
                scalarEventClockFactory.getCurrentClock(SERVICE_NAME).getClock());
    }

    @Test
    void testGetEventClockInstanceWithPrevEventShouldThrowExceptionIfNotRegistered() {
        scalarEventClockFactory.deRegisterService(SERVICE_NAME);
        Assertions.assertThrows(LogicalTimeException.class,
                () -> scalarEventClockFactory.generateClockInstance(SERVICE_NAME, PREV_SERVICE_NAME)
        );
    }
}