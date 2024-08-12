package example.eventdrivenservices.config

import example.eventdrivenservices.claimsStream
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.streams.StreamsBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@EnableKafkaStreams
@Profile("kstreams")
class KafkaStreamsConfig {
    @Value(value = "\${spring.cloud.stream.kafka.binder.brokers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun kAdmin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun kStream(builder: StreamsBuilder): String {
        claimsStream(builder)
        return ""
    }
}