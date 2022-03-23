package com.ms.time.manager.dto;

public record ScalarPublishedEvent(String message, ScalarClock eventClock) {
}
