package com.kanerika.Vendor.controller;

import com.kanerika.Vendor.model.User;
import com.kanerika.Vendor.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/kafka")
//public class HelloKafkaController {
//    @Autowired
//    private MessageProducer producerService;
//
//    public HelloKafkaController(MessageProducer producerService) {
//        this.producerService = producerService;
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(@RequestBody User user) {
//        producerService.sendMessage(user);
//        return ResponseEntity.ok("Message sent to Kafka: " + user);
//    }
//}

import com.kanerika.Vendor.producer.MessageProducer.KafkaSendException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class HelloKafkaController {

    private final MessageProducer producerService;

    public HelloKafkaController(MessageProducer producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody User user) {
        try {
            producerService.sendMessage(user);
            return ResponseEntity.ok("Message sent to Kafka: " + user);
        } catch (KafkaSendException e) {
            return ResponseEntity.status(500)
                    .body("Failed to send message to Kafka: " + e.getMessage());
        }
    }
}