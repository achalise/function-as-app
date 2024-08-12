package example.rebates.eventdriven.events;

import example.rebates.eventdriven.models.ClaimApplication;

import java.util.Date;

public record ApplicationCreated(String correlationId, Date createdTime, ClaimApplication rebateApplication) {
}