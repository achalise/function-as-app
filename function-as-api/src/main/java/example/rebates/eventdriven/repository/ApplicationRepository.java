package example.rebates.eventdriven.repository;

import example.rebates.eventdriven.models.Claim;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<Claim, String> {
}
