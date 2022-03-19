package com.ms;

import com.ms.time.manager.domain.EventClockFactory;
import com.ms.time.manager.provider.ScalarEventProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MessageSender implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(MessageSender.class, args);
    }

    @Value("${spring.application.name}")
    private String serviceName;


    @Autowired
    RestTemplate restTemplate;

    @Override
    public void run(ApplicationArguments args) {
        var provider = new ScalarEventProvider(new EventClockFactory());
        provider.registerService(serviceName);

        restTemplate.postForObject(
                "http://localhost:8088/api/message",
                provider.buildEvent("test message from sender", serviceName),
                String.class
        );
    }
}
