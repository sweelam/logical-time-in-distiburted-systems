package com.ms.api;

import com.ms.time.manager.provider.TimeProviderFactory;
import com.ms.time.manager.types.TimeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api")
public class SenderController {
    private final String serviceName;
    private final RestTemplate restTemplate;

    public SenderController(@Value("${spring.application.name}") String serviceName, RestTemplate restTemplate) {
        this.serviceName = serviceName;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message")
    public void sendMessage() {
        var provider = TimeProviderFactory.ofType(TimeType.SCALER_CLOCK);
        provider.registerService(serviceName);

        restTemplate.postForObject(
                "http://localhost:8088/api/message",
                provider.buildEvent("test message from sender", serviceName),
                String.class
        );
    }
}
