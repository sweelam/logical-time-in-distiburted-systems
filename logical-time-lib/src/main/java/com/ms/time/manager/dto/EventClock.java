package com.ms.time.manager.dto;

public class EventClock {
    private EventClock() {}

    private long clock;
    private static EventClock eventClock;

    public static EventClock getInstance() {
        return eventClock != null ? eventClock : new EventClock();
    }

    public long getClock() {
        return clock;
    }

    public void setClock(long clock) {
        this.clock = clock;
    }
}
