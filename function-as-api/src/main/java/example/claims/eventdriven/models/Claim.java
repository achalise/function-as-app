package example.claims.eventdriven.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public record Claim(String claimId, CustomerDetail customer, String status, Long amount, String claimType, LocalDateTime dateCreated) {
}
