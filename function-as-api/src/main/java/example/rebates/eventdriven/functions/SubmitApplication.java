package example.rebates.eventdriven.functions;

import example.rebates.eventdriven.messaging.MessageService;
import example.rebates.eventdriven.models.ApplicationResponse;
import example.rebates.eventdriven.models.ApplicationState;
import example.rebates.eventdriven.models.BankAccount;
import example.rebates.eventdriven.models.Claim;
import example.rebates.eventdriven.models.CustomerDetail;
import example.rebates.eventdriven.models.ClaimApplication;
import example.rebates.eventdriven.repository.ApplicationRepository;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Function;

public class SubmitApplication implements Function<ClaimApplication, Mono<ApplicationResponse>> {
    private final MessageService messageService;
    private final ApplicationRepository repository;

    public SubmitApplication(MessageService messageService, ApplicationRepository repository){
        this.messageService = messageService;
        this.repository = repository;
    }

    @Override
    public Mono<ApplicationResponse> apply(ClaimApplication application) {
        Claim claim = convertToClaim(application);
        return messageService.publishClaimCreatedEvent(claim)
                .flatMap(e -> repository.save(claim))
                .map(e -> new ApplicationResponse(claim.claimId(), "SUCCESS"));
    }

    private Claim convertToClaim(ClaimApplication application) {
        String correlationId = UUID.randomUUID().toString();
        return new Claim(correlationId,
                new CustomerDetail(correlationId, application.firstName(), application.lastName(),
                        application.email(),
                        new BankAccount(application.bankAccountNumber(), application.bsb(), application.bankAccountName())),
                ApplicationState.APPLICATION_STATE_NEW.name(), application.amount(), application.claimType(), OffsetDateTime.now().toLocalDateTime());
    }
}

