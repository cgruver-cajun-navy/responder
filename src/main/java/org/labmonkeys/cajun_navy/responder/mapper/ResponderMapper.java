package org.labmonkeys.cajun_navy.responder.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.model.Responder;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface ResponderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    Responder responderDtoToEntity(ResponderDTO dto);

    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    ResponderDTO responderEntityToDto(Responder entity);

    List<Responder> responderDtosToEntities(List<ResponderDTO> dtos);
    List<ResponderDTO> responderEntitiesToDtos(List<Responder> entities);

    @AfterMapping
    default void responderDtoToEntityCustom(ResponderDTO dto, @MappingTarget Responder entity) {
        entity.setLatitude(dto.getLatitude().setScale(5, RoundingMode.HALF_UP).toString());
        entity.setLongitude(dto.getLongitude().setScale(5, RoundingMode.HALF_UP).toString());
    }

    @AfterMapping
    default void responderEntityToDtoCustom(Responder entity, @MappingTarget ResponderDTO dto) {
        dto.setLatitude(new BigDecimal(entity.getLatitude()));
        dto.setLongitude(new BigDecimal(entity.getLongitude()));
    }
}
