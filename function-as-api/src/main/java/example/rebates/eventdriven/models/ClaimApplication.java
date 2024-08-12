package example.rebates.eventdriven.models;

public record ClaimApplication(String firstName, String lastName, String email, long amount,
                               String claimType, String bankAccountName, String bankAccountNumber,
                               String bsb) {
}

