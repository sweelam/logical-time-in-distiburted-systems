package com.ms.time.manager.domain;

public interface EventFactoryBuilder {
    void registerService(String serviceName);

    void deRegisterService(String serviceName);

    Object getCurrentClock(String serviceName);

    Object generateClockInstance(String serviceName);

    Object generateClockInstance(String serviceName, String prevEventServiceName);
}
