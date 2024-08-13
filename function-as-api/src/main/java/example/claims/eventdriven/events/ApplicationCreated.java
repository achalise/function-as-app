package example.claims.eventdriven.events;

import example.claims.eventdriven.models.ClaimApplication;

import java.util.Date;

public record ApplicationCreated(String correlationId, Date createdTime, ClaimApplication rebateApplication) {
}