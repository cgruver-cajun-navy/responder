package org.labmonkeys.cajun_navy.responder.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "reported_incident")
public class Responder extends PanacheEntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column(name = "responder_id", updatable = false, nullable = false, unique = true)
    private String responderId;

    @Column(name = "responder_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "boat_capacity")
    private Integer boatCapacity;

    @Column(name = "medical_kit")
    private Boolean medicalKit;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "person", updatable = false, nullable = false)
    private Boolean person;

    @Column(name = "enrolled")
    private Boolean enrolled;

    public static Responder findByResponderId(String responderId) {
        return find("responderId", responderId).firstResult();
    }

    public static List<Responder> findByName(String name) {
        return find("SELECT i from Responder i WHERE LOWER(i.name) LIKE :name", name.toLowerCase()).list();
    }

    public static Long countActiveResponders() {
        return count("enrolled", true);
    }

    public static Long countAllResponders() {
        return count();
    }

    public static Responder updateResponder(Responder entity) {
        update("phoneNumber = ?1, boatCapacity = ?2, medicalKit = ?3, available = ?4, enrolled = $5 where responderId = ?6", entity.getPhoneNumber(), entity.getBoatCapacity(), entity.getMedicalKit(), entity.getAvailable(), entity.getEnrolled(), entity.getResponderId());
        return find("responderId", entity.getResponderId()).firstResult();
    }

    public static Responder updateLocation(Responder responder) {
        update("latitude = ?1, longitude = ?2 where responderId = ?3", responder.getLatitude(), responder.getLongitude(), responder.getResponderId());
        return find("responderId", responder.getResponderId()).firstResult();
    }
}
