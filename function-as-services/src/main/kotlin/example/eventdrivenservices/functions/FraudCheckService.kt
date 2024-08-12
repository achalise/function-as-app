package example.eventdrivenservices.functions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import example.eventdrivenservices.models.ApplicationState
import example.eventdrivenservices.models.Claim
import example.eventdrivenservices.repository.ApplicationRepository

private val objectMapper = ObjectMapper().registerModule(kotlinModule()).registerModule(JavaTimeModule())
fun fraudCheckFn(repository: ApplicationRepository): (application: String)-> String {
    return {
        val event = objectMapper.readValue(it, Claim::class.java)
        val eventOut = event.copy(status = ApplicationState.APPLICATION_STATE_FRAUD_CHECK_SUCCESS.name)
        println("Processed claim ${event.claimId}-${event.customer.email} with amount ${event.amount} from status ${event.status} to ${eventOut.status}")
        repository.save(eventOut)
        objectMapper.writeValueAsString(eventOut)

    }
}