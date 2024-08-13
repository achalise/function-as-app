package example.claims.eventdriven.models;

public record ClaimApplication(String firstName, String lastName, String email, long amount,
                               String claimType, String bankAccountName, String bankAccountNumber,
                               String bsb) {
}

