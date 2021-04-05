package org.labmonkeys.cajun_navy.responder.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;
import org.labmonkeys.cajun_navy.responder.mapper.ResponderMapper;
import org.labmonkeys.cajun_navy.responder.model.Responder;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ResponderService {
    //private static final Logger log = LoggerFactory.getLogger(ResponderService.class);

    @Inject ResponderMapper mapper;
    
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
        return null;
    }

    @Transactional
    public List<ResponderDTO> availableResponders(int limit, int offset) {
        return null;
    }

    @Transactional
    public List<ResponderDTO> allResponders() {
        return null;
    }

    @Transactional
    public List<ResponderDTO> allResponders(int limit, int offset) {
        return null;
    }

    @Transactional
    public List<ResponderDTO> personResponders() {
        return null;
    }

    public List<ResponderDTO> personResponders(int limit, int offset) {
        return null;
    }

    @Transactional
    public ResponderDTO createResponder(ResponderDTO responder) {
        Responder entity = mapper.responderDtoToEntity(responder);
        Responder.persist(entity);
        return mapper.responderEntityToDto(entity);
    }

    @Transactional
    public List<ResponderDTO> createResponders(List<ResponderDTO> responders) {
        List<Responder> entities = mapper.responderDtosToEntities(responders);
        Responder.persist(entities);
        return mapper.responderEntitiesToDtos(entities);
    }

    @Transactional
    public ResponderDTO updateResponder(ResponderDTO responder) {
        return mapper.responderEntityToDto(Responder.updateResponder(mapper.responderDtoToEntity(responder)));
    }

    @Transactional
    public ResponderDTO updateResponderLocation(ResponderDTO responder) {
        return mapper.responderEntityToDto(Responder.updateLocation(mapper.responderDtoToEntity(responder)));
    }
}
