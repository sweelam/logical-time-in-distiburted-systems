package com.ms.api;

import com.ms.time.manager.provider.TimeProviderFactory;
import com.ms.time.manager.types.TimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api")
public class SenderController {
    private final RestTemplate restTemplate;

    private static final String SCALAR_MESSAGE_URL =
            "http://localhost:8088/api/scalar-message";
    private static final String VECTOR_MESSAGE_API_URL =
            "http://localhost:8088/api/vector-message";

    public SenderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message/{appName}")
    public void sendMessage(@RequestParam Integer type, @PathVariable String appName) {
        var provider =
                TimeProviderFactory.ofType(type == 1 ? TimeType.SCALER_CLOCK : TimeType.VECTOR_CLOCK);
        provider.registerService(appName);

        final String URL = type == 1 ? SCALAR_MESSAGE_URL : VECTOR_MESSAGE_API_URL;

        restTemplate.postForObject(
                URL,
                provider.buildEvent("test message from sender", appName),
                String.class
        );
    }
}
