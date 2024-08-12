package example.rebates.eventdriven.models;

public record CustomerDetail(String id, String firstName, String lastName, String email, BankAccount bankAccount) {
}
