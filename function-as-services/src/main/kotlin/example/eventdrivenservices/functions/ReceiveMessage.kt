package example.eventdrivenservices.functions

import example.eventdrivenservices.repository.ApplicationRepository
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.GenericMessage

fun receiveMessageFn(repository: ApplicationRepository, sourceChannel: MessageChannel): (rebate: String) -> String  {
    return { application: String ->
       println("Received message $application")
       sourceChannel.send(GenericMessage<String>(application))
       "SUCCESSFUL"
    }
}