package com.ms.time.manager.dto;

public class EventClock {
    private EventClock() {}

    private long clock;

    public static EventClock getInstance() {
        return new EventClock();
    }

    public long getClock() {
        return clock;
    }

    public void setClock(long clock) {
        this.clock = clock;
    }
}
