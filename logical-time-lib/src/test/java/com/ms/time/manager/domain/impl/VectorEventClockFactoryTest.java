package com.ms.time.manager.domain.impl;

import com.ms.time.manager.dto.VectorClock;
import com.ms.time.manager.exception.LogicalTimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorEventClockFactoryTest {
    private static VectorClockFactory vectorClockFactory;
    private static final String SERVICE_NAME = "Service-A";
    private static final String SERVICE_NAME_B = "Service-B";
    private static final String SERVICE_NAME_C = "Service-C";


    @BeforeAll
    static void init() {
        vectorClockFactory = new VectorClockFactory();
    }

    @AfterEach
    void clean() {
        vectorClockFactory.deRegisterService(SERVICE_NAME);
        vectorClockFactory.deRegisterService(SERVICE_NAME_B);
        vectorClockFactory.deRegisterService(SERVICE_NAME_C);
    }

    @Test
    void registerService() {
        vectorClockFactory.registerService(SERVICE_NAME);
        var registeredServices = vectorClockFactory.getRegisteredService();

        assertNotNull(registeredServices);
        assertTrue(registeredServices.size() > 0);

        assertNotNull(registeredServices.get(SERVICE_NAME));
        assertEquals(0, (Integer) registeredServices.get(SERVICE_NAME).get("process-number"));
        assertEquals(1, ((VectorClock) registeredServices.get(SERVICE_NAME).get("clock")).getClock().size());

        vectorClockFactory.registerService(SERVICE_NAME_B);
        registeredServices = vectorClockFactory.getRegisteredService();
        assertEquals(1, (Integer) registeredServices.get(SERVICE_NAME_B).get("process-number"));
        assertEquals(2, ((VectorClock) registeredServices.get(SERVICE_NAME_B).get("clock")).getClock().size());
    }


    @Test
    void generateClockInstanceShouldThrowExceptionIfNotRehistered() throws LogicalTimeException {
        assertThrows(LogicalTimeException.class,
                () -> vectorClockFactory.generateClockInstance(SERVICE_NAME));
    }


    @Test
    void generateClockInstanceShouldIncreaseVectorProcess() {
        vectorClockFactory.registerService(SERVICE_NAME);
        var vectorClock = vectorClockFactory.generateClockInstance(SERVICE_NAME);

        assertNotNull(vectorClock);
        assertEquals(1, vectorClock.getClock().get(0));


        vectorClock = vectorClockFactory.generateClockInstance(SERVICE_NAME);
        assertNotNull(vectorClock);
        assertEquals(2, vectorClock.getClock().get(0));

        vectorClockFactory.registerService(SERVICE_NAME_B);
        vectorClock = vectorClockFactory.generateClockInstance(SERVICE_NAME_B);

        assertNotNull(vectorClock);
        assertEquals(1, vectorClock.getClock().get(1));
    }



    @Test
    void generateClockInstanceShouldUpdateAfterMaximizing() {
        vectorClockFactory.registerService(SERVICE_NAME);
        vectorClockFactory.registerService(SERVICE_NAME_B);

        var bVectorClock = vectorClockFactory.getCurrentClock(SERVICE_NAME_B);
        var sourceVectorClock = vectorClockFactory.getCurrentClock(SERVICE_NAME);

        vectorClockFactory.generateClockInstance(SERVICE_NAME_B);
        vectorClockFactory.generateClockInstance(SERVICE_NAME_B);
        vectorClockFactory.generateClockInstance(SERVICE_NAME_B);

        assertEquals(2, bVectorClock.getClock().size());
        assertEquals(3, bVectorClock.getClock().get(1));

        vectorClockFactory.generateClockInstance(SERVICE_NAME, SERVICE_NAME_B);
        assertEquals(1, sourceVectorClock.getClock().get(0));
    }


    @Test
    void deRegisterShouldShrinkVectorSize() {
        vectorClockFactory.registerService(SERVICE_NAME);
        vectorClockFactory.registerService(SERVICE_NAME_B);
        vectorClockFactory.registerService(SERVICE_NAME_C);

        var registeredService = vectorClockFactory.getRegisteredService();

        var aVectorClock = vectorClockFactory.getCurrentClock(SERVICE_NAME);
        var bVectorClock = vectorClockFactory.getCurrentClock(SERVICE_NAME_B);
        var cVectorClock = vectorClockFactory.getCurrentClock(SERVICE_NAME_C);

        assertEquals(3, aVectorClock.getClock().size());
        assertEquals(3, bVectorClock.getClock().size());

        vectorClockFactory.deRegisterService(SERVICE_NAME_B);
        assertEquals(2, aVectorClock.getClock().size());

        assertEquals(2, cVectorClock.getClock().size());
    }
}