package com.ms.time.manager;

public interface EventTimeManager {
    void registerService(String serviceName);

    Object buildEvent(String message, String serviceName);

    Object buildEvent(String message, String serviceName, String prevEventServiceName);
}
