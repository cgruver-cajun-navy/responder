package org.labmonkeys.cajun_navy.responder.event;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import io.vertx.mutiny.core.eventbus.EventBus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ResponderEventSubscriber {

    //private final static Logger log = LoggerFactory.getLogger(ResponderEventSubscriber.class);
    
    @Inject EventBus bus;

    @Incoming("responder-location-update")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<CompletionStage<Void>> updateResponderLocation(Message<ResponderDTO> message) {
        return CompletableFuture.supplyAsync(() -> {
            bus.<ResponderDTO>request("updateResponderLocation", message.getPayload()).onItem().transform(msg -> msg.body());
            return message.ack();
        });
    }

    @Incoming("responder-info-update")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<CompletionStage<Void>> updateResponderInfo (Message<ResponderDTO> message) {
        return CompletableFuture.supplyAsync(() -> {
            bus.<ResponderDTO>request("updateResponderInfo", message.getPayload()).onItem().transform(msg -> msg.body());
            return message.ack();
        });
    }

    @Incoming("responder-availability-update")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<CompletionStage<Void>> updateResponderAvailable (Message<ResponderDTO> message) {
        return CompletableFuture.supplyAsync(() -> {
            bus.<ResponderDTO>request("updateResponderAvailable", message.getPayload()).onItem().transform(msg -> msg.body());
            return message.ack();
        });
    }
}
