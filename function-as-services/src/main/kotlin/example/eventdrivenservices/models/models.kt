package example.eventdrivenservices.models

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

data class ClaimApplication(
    var firstName: String,
    var lastName: String,
    var email: String,
    var amount: Long,
    var rebateType: String
)

data class ApplicationCreated(
    var correlationId: String, var createdTime: Date,
    var claimApplication: ClaimApplication,
    var status: String = "APPLICATION_NEW"
)

data class EligibilityCheckPassed(
    var correlationId: String, var createdTime: Date,
    var claimApplication: ClaimApplication,
    var status: String = "APPLICATION_NEW"
)

fun ApplicationCreated.toEligibilityCheckEvent(): EligibilityCheckPassed {
    val eligibilityCheckEvent = EligibilityCheckPassed(correlationId, Date(), claimApplication, "ELIGIBILITY_CHECK_PASSED")
    return eligibilityCheckEvent
}

enum class ApplicationState {
     APPLICATION_STATE_ELIGIBILITY_CHECK_SUCCESS,
     APPLICATION_STATE_FRAUD_CHECK_SUCCESS,
    APPLICATION_STATE_PAYMENT_SUCCESSFUL
}

@Document
data class Claim(
    val claimId: String, val customer: CustomerDetail, val status: String,
    val amount: Long, val claimType: String, val dateCreated: LocalDateTime
)

@JvmRecord
data class BankAccount(val accountNumber: String, val bsb: String, val accountName: String)

@JvmRecord
data class CustomerDetail(val id: String, val firstName: String, val lastName: String, val email: String, val bankAccount: BankAccount)