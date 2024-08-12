package example.eventdrivenservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.TimeWindows
import java.time.Duration


private val objectMapper = ObjectMapper().registerModule(kotlinModule())

fun claimsStream(streamsBuilder: StreamsBuilder) {
    val windowSize: Duration = Duration.ofSeconds(30)
    val tumblingWindow = TimeWindows.ofSizeWithNoGrace(windowSize)

    val stream = streamsBuilder
        .stream("rebate_applications", Consumed.with(Serdes.String(), Serdes.String()))
        .peek { _, v ->
            println("received claim $v")
        }
}
