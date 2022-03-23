package com.ms.time.manager;

import com.ms.time.manager.dto.ScalarPublishedEvent;

public interface EventTimeManager {
    void registerService(String serviceName);

    ScalarPublishedEvent buildEvent(String message, String serviceName);

    ScalarPublishedEvent buildEvent(String message, String serviceName, String prevEventServiceName);
}
