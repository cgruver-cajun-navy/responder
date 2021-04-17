package org.labmonkeys.cajun_navy.responder.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ResponderDTO {

    public static enum STATUS{OFF_LINE, ENROLLED, AVAILABLE, ASSIGNED}
    
    private String responderId;
    private String disasterId;
    private String name;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer boatCapacity;
    private Boolean medicalKit;
    private Boolean person;
    private STATUS status;
}
