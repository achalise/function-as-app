package example.claims.eventdriven.models;

public record CustomerDetail(String id, String firstName, String lastName, String email, BankAccount bankAccount) {
}
