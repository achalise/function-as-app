package example.rebates.eventdriven.messaging;

import example.rebates.eventdriven.events.ApplicationCreated;
import example.rebates.eventdriven.models.Claim;
import example.rebates.eventdriven.models.SendMessageResult;
import reactor.core.publisher.Mono;

public interface MessageService {
    Mono<SendMessageResult> publishClaimCreatedEvent(Claim event);
}

