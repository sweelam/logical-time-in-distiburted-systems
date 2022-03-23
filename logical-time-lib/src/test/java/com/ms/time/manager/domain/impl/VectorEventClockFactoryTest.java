package com.ms.time.manager.domain.impl;

import com.ms.time.manager.dto.VectorClock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorEventClockFactoryTest {
    private static VectorEventClockFactory vectorEventClockFactory;
    private static final String SERVICE_NAME = "Service-A";
    private static final String SERVICE_NAME_B = "Service-B";


    @BeforeAll
    static void init() {
        vectorEventClockFactory = new VectorEventClockFactory();
    }

    @Test
    void registerService() {
        vectorEventClockFactory.registerService(SERVICE_NAME);
        var registeredServices = vectorEventClockFactory.getRegisteredService();

        assertNotNull(registeredServices);
        assertTrue(registeredServices.size() > 0);

        assertNotNull(registeredServices.get(SERVICE_NAME));
        assertEquals(0, (Integer) registeredServices.get(SERVICE_NAME).get("process-number"));
        assertEquals(1, ((VectorClock) registeredServices.get(SERVICE_NAME).get("clock")).getClock().size());

        vectorEventClockFactory.registerService(SERVICE_NAME_B);
        registeredServices = vectorEventClockFactory.getRegisteredService();
        assertEquals(1, (Integer) registeredServices.get(SERVICE_NAME_B).get("process-number"));
        assertEquals(2, ((VectorClock) registeredServices.get(SERVICE_NAME_B).get("clock")).getClock().size());
    }
}