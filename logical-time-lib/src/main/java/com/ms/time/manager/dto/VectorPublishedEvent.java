package com.ms.time.manager.dto;

public record VectorPublishedEvent(String message, VectorClock eventClock) {
}
