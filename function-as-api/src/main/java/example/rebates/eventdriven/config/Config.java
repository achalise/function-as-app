package example.rebates.eventdriven.config;

import example.rebates.eventdriven.functions.SubmitApplication;
import example.rebates.eventdriven.messaging.KafkaMessageService;
import example.rebates.eventdriven.messaging.MessageService;
import example.rebates.eventdriven.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Supplier;

@Configuration
class AppConfig {
    @Value("${application.claim.topic}")
    private String claimTopic;

    @Bean
    public Object topic1() {
        return TopicBuilder.name(claimTopic)
                .build();
    }

    @Bean
    public MessageService messageService(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaMessageService(kafkaTemplate, claimTopic);
    }

    @Bean
    public SubmitApplication submitApplication(MessageService messageService, ApplicationRepository repository) {
        return new SubmitApplication(messageService, repository);
    }
}
