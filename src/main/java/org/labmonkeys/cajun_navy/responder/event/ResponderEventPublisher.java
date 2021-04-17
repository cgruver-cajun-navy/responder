package org.labmonkeys.cajun_navy.responder.event;

import java.time.OffsetDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadata;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ResponderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventBusSubscriber.class);
    
    private final UnicastProcessor<ResponderDTO> responderCreateProcessor = UnicastProcessor.create();
    private final UnicastProcessor<ResponderDTO> responderUpdateProcessor = UnicastProcessor.create();
    private final UnicastProcessor<ResponderDTO> responderLocationProcessor = UnicastProcessor.create();
    
    public void createResponder(ResponderDTO dto) {
        responderCreateProcessor.onNext(dto);
    }

    public void createResponders(List<ResponderDTO> dtos) {
        for (ResponderDTO responderDTO : dtos) {
            responderCreateProcessor.onNext(responderDTO);
        }
    }

    public void updateResponder(ResponderDTO dto) {
        responderUpdateProcessor.onNext(dto);
    }

    public void updateResponderLocation(ResponderDTO dto) {
        responderLocationProcessor.onNext(dto);
    }

    @Outgoing("responder-created")
    public Multi<org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO>> responderCreate() {
        return responderCreateProcessor.onItem().transform(this::sendResponderCreate);
    }

    @Outgoing("responder-updated")
    public Multi<org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO>> responderUpdate() {
        return responderUpdateProcessor.onItem().transform(this::sendResponderUpdate);
    }

    @Outgoing("responder-location-updated")
    public Multi<org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO>> responderLocation() {
        return responderLocationProcessor.onItem().transform(this::sendResponderLocation);
    }

    private org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO> sendResponderCreate(ResponderDTO responder) {
        log.debug("IncidentReportedEvent: " + responder);
        return KafkaRecord.of(responder.getResponderId(), responder)
                .addMetadata(OutgoingCloudEventMetadata.builder().withType("ResponderCreatedEvent")
                        .withTimestamp(OffsetDateTime.now().toZonedDateTime()).build());
    }

    private org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO> sendResponderUpdate(ResponderDTO responder) {
        log.debug("IncidentReportedEvent: " + responder);
        return KafkaRecord.of(responder.getResponderId(), responder)
                .addMetadata(OutgoingCloudEventMetadata.builder().withType("ResponderUpdatedEvent")
                        .withTimestamp(OffsetDateTime.now().toZonedDateTime()).build());
    }

    private org.eclipse.microprofile.reactive.messaging.Message<ResponderDTO> sendResponderLocation(ResponderDTO responder) {
        log.debug("IncidentReportedEvent: " + responder);
        return KafkaRecord.of(responder.getResponderId(), responder)
                .addMetadata(OutgoingCloudEventMetadata.builder().withType("ResponderLocationEvent")
                        .withTimestamp(OffsetDateTime.now().toZonedDateTime()).build());
    }
}
