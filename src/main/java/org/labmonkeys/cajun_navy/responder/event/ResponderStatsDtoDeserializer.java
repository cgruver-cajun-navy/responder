package org.labmonkeys.cajun_navy.responder.event;

import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ResponderStatsDtoDeserializer extends ObjectMapperDeserializer<ResponderStatsDTO>{
    public ResponderStatsDtoDeserializer() {
        super(ResponderStatsDTO.class);
    }
}
