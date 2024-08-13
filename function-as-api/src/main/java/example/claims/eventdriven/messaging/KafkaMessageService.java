package example.claims.eventdriven.messaging;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import example.claims.eventdriven.models.Claim;
import example.claims.eventdriven.models.SendMessageResult;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaMessageService implements MessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String claimTopic;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMessageService(KafkaTemplate<String, String> kafkaTemplate, String claimTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.claimTopic = claimTopic;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mono<SendMessageResult> publishClaimCreatedEvent(Claim event) {
        Mono<String> jsonString;
        try {
            jsonString = Mono.just(objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            logger.error("Error when processing {}", e.getMessage());
            jsonString = Mono.error(e);
        }
        return jsonString.filter(e -> !e.isEmpty()).flatMap(value ->
                Mono.fromFuture(kafkaTemplate.send(claimTopic, value))
                        .map(sendResult -> new SendMessageResult("SUCCESS"))
                        .doOnSuccess(sendResult -> logger.info("Successfully published event " + event)))
                .doOnError(e -> logger.error("Error when processing {}", e.getMessage()))
                .switchIfEmpty(Mono.just( new SendMessageResult("ERROR")));

    }
}