package org.labmonkeys.cajun_navy.responder.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;
import org.labmonkeys.cajun_navy.responder.event.ResponderEventPublisher;
import org.labmonkeys.cajun_navy.responder.mapper.ResponderMapper;
import org.labmonkeys.cajun_navy.responder.model.Responder;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ResponderService {
    //private static final Logger log = LoggerFactory.getLogger(ResponderService.class);

    @Inject ResponderMapper mapper;

    @Inject
    ResponderEventPublisher publisher;
    
    @Transactional
    public ResponderStatsDTO getResponderStats() {
        Long active = Responder.countActiveResponders();
        Long total = Responder.countAllResponders();
        return new ResponderStatsDTO(active, total);
    }

    @Transactional
    public ResponderDTO getResponder(String responderId) {
        return mapper.responderEntityToDto(Responder.findByResponderId(responderId));
    }

    @Transactional
    public List<ResponderDTO> getRespondersByName(String name) {
        return mapper.responderEntitiesToDtos(Responder.findByName(name));
    }

    @Transactional
    public List<ResponderDTO> availableResponders() {
        return mapper.responderEntitiesToDtos(Responder.findByAvailable(0,0));
    }

    @Transactional
    public List<ResponderDTO> availableResponders(int limit, int offset) {
        return mapper.responderEntitiesToDtos(Responder.findByAvailable(limit,offset));
    }

    @Transactional
    public List<ResponderDTO> allResponders() {
        return mapper.responderEntitiesToDtos(Responder.findAllResponders(0,0));
    }

    @Transactional
    public List<ResponderDTO> allResponders(int limit, int offset) {
        return mapper.responderEntitiesToDtos(Responder.findAllResponders(limit,offset));
    }

    @Transactional
    public List<ResponderDTO> personResponders() {
        return mapper.responderEntitiesToDtos(Responder.findPersons(0, 0));
    }

    public List<ResponderDTO> personResponders(int limit, int offset) {
        return mapper.responderEntitiesToDtos(Responder.findPersons(limit, offset));
    }

    @Transactional
    public ResponderDTO createResponder(ResponderDTO dto) {
        Responder entity = mapper.responderDtoToEntity(dto);
        Responder.persist(entity);
        ResponderDTO responder = mapper.responderEntityToDto(entity);
        publisher.createResponder(responder);
        return responder;
    }

    @Transactional
    public List<ResponderDTO> createResponders(List<ResponderDTO> dtos) {
        List<Responder> entities = mapper.responderDtosToEntities(dtos);
        Responder.persist(entities);
        List<ResponderDTO> responders = mapper.responderEntitiesToDtos(entities);
        publisher.createResponders(responders);
        return responders;
    }

    @Transactional
    public ResponderDTO updateResponderInfo(ResponderDTO dto) {
        ResponderDTO responder = mapper.responderEntityToDto(Responder.updateResponderInfo(mapper.responderDtoToEntity(dto)));
        publisher.updateResponder(responder);
        return responder;
    }

    @Transactional
    public ResponderDTO updateResponderAvailable(ResponderDTO dto) {
        ResponderDTO responder = mapper.responderEntityToDto(Responder.updateResponderAvailable(mapper.responderDtoToEntity(dto)));
        publisher.updateResponder(responder);
        return responder;
    }

    @Transactional
    public ResponderDTO updateResponderLocation(ResponderDTO dto) {
        ResponderDTO responder = mapper.responderEntityToDto(Responder.updateLocation(mapper.responderDtoToEntity(dto)));
        publisher.updateResponderLocation(responder);
        return responder;
    }

    @Transactional
    public void reset() {
        Responder.reset();
    }

    @Transactional
    public void clear(boolean deleteBots, boolean deleteAll) {
        if (deleteBots) {
            Responder.deleteBots();
            Responder.resetPerson();
        } else if (deleteAll) {
            Responder.deleteAll();
        } else {
            Responder.clearBots();
            Responder.resetPerson();
        }
    }
}
