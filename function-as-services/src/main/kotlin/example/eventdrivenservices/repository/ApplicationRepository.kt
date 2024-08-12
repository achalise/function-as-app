package example.eventdrivenservices.repository

import example.eventdrivenservices.models.Claim
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : MongoRepository<Claim?, String?>