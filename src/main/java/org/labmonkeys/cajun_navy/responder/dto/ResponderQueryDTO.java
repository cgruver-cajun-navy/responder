package org.labmonkeys.cajun_navy.responder.dto;

import lombok.Data;

@Data
public class ResponderQueryDTO {
    String disasterId;
    Integer limit;
    Integer offset;
}
