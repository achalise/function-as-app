package example.eventdrivenservices.functions

import example.eventdrivenservices.repository.ApplicationRepository

fun submitApplicationFn(repository: ApplicationRepository): (application: String) -> String  {
    return { rebate: String ->
        println("Received message $rebate")
        rebate
    }
}