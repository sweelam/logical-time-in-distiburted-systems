package com.ms.api;

import com.ms.time.manager.provider.TimeProviderFactory;
import com.ms.time.manager.types.TimeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/message/{appName}")
    public void sendMessage(@RequestParam Integer type, @PathVariable String appName) {
        var provider =
                TimeProviderFactory.ofType(type == 1 ? TimeType.SCALER_CLOCK : TimeType.VECTOR_CLOCK);
        provider.registerService(appName);

        final String URL = type == 1 ?
                "http://localhost:8088/api/scalar-message" : "http://localhost:8088/api/vector-message";

        restTemplate.postForObject(
                URL,
                provider.buildEvent("test message from sender", appName),
                String.class
        );
    }
}
