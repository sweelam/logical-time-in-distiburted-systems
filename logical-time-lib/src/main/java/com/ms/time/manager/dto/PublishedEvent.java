package com.ms.time.manager.dto;

public record PublishedEvent (String message, EventClock eventClock) {
}
