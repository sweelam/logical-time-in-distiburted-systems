package com.ms.api;

import com.ms.time.manager.dto.PublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MessageReceiverController {
    Logger logger = LoggerFactory.getLogger(MessageReceiverController.class);

    @PostMapping("/message")
    public String handleMessages(@RequestBody PublishedEvent event) {
        logger.info("Received message {} with order {} ", event.message(), event.eventClock().getClock());
        return "Received message " + event.message();
    }
}
