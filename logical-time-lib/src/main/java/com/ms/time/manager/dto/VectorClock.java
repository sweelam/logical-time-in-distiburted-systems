package com.ms.time.manager.dto;

import java.util.List;

public class VectorClock {
    private VectorClock() {}

    private List<Integer> clock;

    public List<Integer> getClock() {
        return clock;
    }

    public void setClock(List<Integer> clock) {
        this.clock = clock;
    }

    public static VectorClock getInstance() {
        return new VectorClock();
    }
}
