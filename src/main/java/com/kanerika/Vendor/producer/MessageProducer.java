package com.kanerika.Vendor.producer;

import com.kanerika.Vendor.model.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
//public class MessageProducer {
//
//    @Autowired
//    private KafkaTemplate<String, User> kafkaTemplate;
//    private static final String TOPIC = "test_topic";
//
//    public void sendMessage(User user) {
//        kafkaTemplate.send(TOPIC, user);
//    }
//}
//
//@Component
//public class MessageProducer {
//
//    private final KafkaTemplate<String, User> kafkaTemplate;
//    private static final String TOPIC = "my_topic";
//
//    public MessageProducer(KafkaTemplate<String, User> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final KafkaTemplate<String, User> kafkaTemplate;
    private static final String TOPIC = "my_topic";

    public MessageProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(User user) throws KafkaSendException {
        if (user == null) {
            logger.error("Attempted to send null user");
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            logger.debug("Attempting to send user {} to topic {}", user, TOPIC);

            CompletableFuture<SendResult<String, User>> future = kafkaTemplate.send(TOPIC, user);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send message to topic {}: {}", TOPIC, ex.getMessage());
                    // Additional error handling logic here
                } else {
                    logger.info("Successfully sent message to topic {} [partition={}, offset={}]",
                            TOPIC,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            logger.error("Kafka communication error for user {}: {}", user, e.getMessage(), e);
            throw new KafkaSendException("Failed to send message to Kafka", e);
        }
    }

    public static class KafkaSendException extends Exception {
        public KafkaSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}