package example.eventdrivenservices.config

import example.eventdrivenservices.functions.checkEligibilityFn
import example.eventdrivenservices.functions.fraudCheckFn
import example.eventdrivenservices.functions.processPaymentFn
import example.eventdrivenservices.functions.submitApplicationFn
import example.eventdrivenservices.repository.ApplicationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("FunctionChain")
class FunctionConfig {

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

    @Bean
    fun submitApplication(repository: ApplicationRepository): (String) -> String {
        return submitApplicationFn(repository)
    }

}