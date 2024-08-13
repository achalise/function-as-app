package example.claims.eventdriven.messaging;

import example.claims.eventdriven.models.Claim;
import example.claims.eventdriven.models.SendMessageResult;
import reactor.core.publisher.Mono;

public interface MessageService {
    Mono<SendMessageResult> publishClaimCreatedEvent(Claim event);
}

