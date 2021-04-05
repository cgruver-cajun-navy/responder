package org.labmonkeys.cajun_navy.responder.event;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.vertx.ConsumeEvent;
import javax.inject.Inject;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.Message;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;
import org.labmonkeys.cajun_navy.responder.service.ResponderService;

@ApplicationScoped
public class EventBusSubscriber {

    //private static final Logger log = LoggerFactory.getLogger(EventBusSubscriber.class);
    
    @Inject
    ResponderService service;

    @Inject
    EventPublisher publisher;

    @ConsumeEvent("stats")
    @Blocking
    public ResponderStatsDTO stats(Message<Object> msg) {
        return service.getResponderStats();
    }

    @ConsumeEvent("responderById")
    @Blocking
    public ResponderDTO responderById(String responderId) {
        return service.getResponder(responderId);
    }

    @ConsumeEvent("responderByName")
    @Blocking
    public List<ResponderDTO> responderByName(String name) {
        return service.getRespondersByName(name);
    }

    @ConsumeEvent("avalableResponders")
    @Blocking
    public List<ResponderDTO> avalableResponders(Message<JsonObject> msg) {
        JsonObject payload = msg.body();
        Integer limit = payload.getInteger("limit");
        Integer offset = payload.getInteger("offset");
        return service.availableResponders(limit, offset);
    }

    @ConsumeEvent("allResponders")
    @Blocking
    public List<ResponderDTO> allResponders(Message<JsonObject> msg){
        JsonObject payload = msg.body();
        Integer limit = payload.getInteger("limit");
        Integer offset = payload.getInteger("offset");
        return service.allResponders(limit, offset);
    }

    @ConsumeEvent("createResponder")
    @Blocking
    public ResponderDTO createResponder(ResponderDTO dto) {
        ResponderDTO responder = service.createResponder(dto);
        publisher.createResponder(responder);
        return responder;
    }

    @ConsumeEvent("createResponders")
    @Blocking
    public List<ResponderDTO> createResponders(List<ResponderDTO> dtos) {
        List<ResponderDTO> responders = service.createResponders(dtos);
        publisher.createResponders(responders);
        return responders;
    }

    @ConsumeEvent("updateResponder")
    @Blocking
    public ResponderDTO updateResponder(ResponderDTO dto) {
        ResponderDTO responder = service.updateResponder(dto);
        publisher.updateResponder(responder);
        return responder;
    }

    @ConsumeEvent("updateResponderLocation")
    @Blocking
    public ResponderDTO updateResponderLocation(ResponderDTO dto) {
        ResponderDTO responder = service.updateResponderLocation(dto);
        publisher.updateResponderLocation(responder);
        return responder;
    }

    // @ConsumeEvent("reset")
    // @Blocking

    // @ConsumeEvent("clear")
    // @Blocking

    

    
}
