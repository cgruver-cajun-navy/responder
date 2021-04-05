package org.labmonkeys.cajun_navy.responder.event;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ResponderDtoDeserilizer extends ObjectMapperDeserializer<ResponderDTO> {

    public ResponderDtoDeserilizer() {
        super(ResponderDTO.class);
    }
    
}
