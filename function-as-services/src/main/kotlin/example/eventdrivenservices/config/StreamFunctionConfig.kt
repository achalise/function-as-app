package example.eventdrivenservices.config

import example.eventdrivenservices.functions.checkEligibilityFn
import example.eventdrivenservices.functions.fraudCheckFn
import example.eventdrivenservices.functions.processPaymentFn
import example.eventdrivenservices.repository.ApplicationRepository
import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@Profile("CloudFunctionStream")
class StreamFunctionConfig {
    @Value(value = "\${spring.cloud.stream.kafka.binder.brokers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun checkEligibility(repository: ApplicationRepository): (String) -> String {
        return checkEligibilityFn(repository)
    }

    @Bean
    fun fraudCheck(repository: ApplicationRepository): (String) -> String {
        return fraudCheckFn(repository)
    }

    @Bean
    fun processPayment(repository: ApplicationRepository): (String) -> String {
        return processPaymentFn(repository)
    }
}