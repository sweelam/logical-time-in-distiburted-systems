package com.ms.time.manager.dto;

public class ScalarClock {
    private ScalarClock() {}

    private int clock;

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public static ScalarClock getInstance() {
        return new ScalarClock();
    }
}
