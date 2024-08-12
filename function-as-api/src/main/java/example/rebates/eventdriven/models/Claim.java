package example.rebates.eventdriven.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document
public record Claim(String claimId, CustomerDetail customer, String status, Long amount, String claimType, LocalDateTime dateCreated) {
}
