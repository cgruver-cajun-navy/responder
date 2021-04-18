package org.labmonkeys.cajun_navy.responder.event;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.vertx.ConsumeEvent;
import javax.inject.Inject;
import io.smallrye.common.annotation.Blocking;
import io.vertx.mutiny.core.eventbus.Message;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderQueryDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;
import org.labmonkeys.cajun_navy.responder.service.ResponderService;

@ApplicationScoped
public class EventBusSubscriber {

    //private static final Logger log = LoggerFactory.getLogger(EventBusSubscriber.class);
    
    @Inject
    ResponderService service;

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

    @ConsumeEvent("availableResponders")
    @Blocking
    public List<ResponderDTO> availableResponders(Message<ResponderQueryDTO> msg) {
        ResponderQueryDTO dto = msg.body();
        return service.availableResponders(dto);
    }

    @ConsumeEvent("responders")
    @Blocking
    public List<ResponderDTO> allResponders(Message<ResponderQueryDTO> msg){
        ResponderQueryDTO dto = msg.body();
        return service.responders(dto);
    }

    @ConsumeEvent("createResponder")
    @Blocking
    public ResponderDTO createResponder(ResponderDTO dto) {
        ResponderDTO responder = service.createResponder(dto);
        return responder;
    }

    @ConsumeEvent("createResponders")
    @Blocking
    public List<ResponderDTO> createResponders(List<ResponderDTO> dtos) {
        List<ResponderDTO> responders = service.createResponders(dtos);
        return responders;
    }

    @ConsumeEvent("updateResponderAvailable")
    @Blocking
    public ResponderDTO updateResponder(ResponderDTO dto) {
        ResponderDTO responder = service.updateResponderAvailable(dto);
        return responder;
    }

    @ConsumeEvent("updateResponderInfo")
    @Blocking
    public ResponderDTO updateResponderInfo(ResponderDTO dto) {
        ResponderDTO responder = service.updateResponderInfo(dto);
        return responder;
    }

    @ConsumeEvent("updateResponderLocation")
    @Blocking
    public ResponderDTO updateResponderLocation(ResponderDTO dto) {
        ResponderDTO responder = service.updateResponderLocation(dto);
        return responder;
    }
}
