package com.ms;

import com.ms.time.manager.dto.PublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MessageReceiver {
    Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    public static void main(String[] args) {
        SpringApplication.run(MessageReceiver.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    @RequestMapping("api")
    class TestController {

        @PostMapping("/message")
        public String handleMessages(@RequestBody PublishedEvent event) {
            logger.info("Received message {} with order {} ", event.message(), event.eventClock().getClock());
            return "Received message " + event.message();
        }
    }
}
