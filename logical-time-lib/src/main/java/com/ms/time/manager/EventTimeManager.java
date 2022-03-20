package com.ms.time.manager;

import com.ms.time.manager.dto.PublishedEvent;

public interface EventTimeManager {
    void registerService(String serviceName);

    PublishedEvent buildEvent(String message, String serviceName);

    PublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName);
}
