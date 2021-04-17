package org.labmonkeys.cajun_navy.responder.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO.STATUS;

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

    @Column(name = "disaster_id")
    private String disasterId;

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

    @Column(name = "person", updatable = false, nullable = false)
    private Boolean person;

    @Column(name = "status")
    private STATUS status;

    public static Responder findByResponderId(String responderId) {
        return find("responderId", responderId).firstResult();
    }

    public static Responder findByDisasterId(String disasterId) {
        return find("disasterId", disasterId).firstResult();
    }

    public static List<Responder> findByName(String name) {
        return find("SELECT i from Responder i WHERE LOWER(i.name) LIKE :name", name.toLowerCase()).list();
    }

    public static List<Responder> findByAvailable(String disasterId, int limit, int offset) {
        if (limit == 0) {
            return find("status, disasterId", STATUS.AVAILABLE, disasterId).list();
        } else {
            int first = offset;
            int last = offset+limit-1;
            return find("status, disasterId", STATUS.AVAILABLE, disasterId).range(first, last).list();
        }
    }

    public static List<Responder> findResponders(String disasterId, int limit, int offset) {
        if(limit == 0) {
            if (disasterId == "") {
                return findAll().list();
            } else {
                return find("disasterId", disasterId).list();
            }
        } else {
            int first = offset;
            int last = offset+limit-1;
            if (disasterId == "") {
                return findAll().range(first, last).list();
            } else {
                return find("disasterId", disasterId).range(first, last).list();
            }
        }
    }

    public static List<Responder> findPersons(String disasterId, int limit, int offset) {
        if(limit == 0) {
            if (disasterId == "") {
                return find("person", true).list();
            } else {
                return find("disasterId, person", disasterId, true).list();
            }
        } else {
            int first = offset;
            int last = offset+limit-1;
            if (disasterId == "") {
                return find("person", true).range(first, last).list();
            } else {
                return find("disasterId, person", disasterId, true).range(first, last).list();
            }
        }
    }

    public static List<Responder> findBots(int limit, int offset) {
        if (limit == 0) {
            return find("person", false).list();
        } else {
            int first = offset;
            int last = offset+limit-1;
            return find("person", false).range(first, last).list();
        }
    }

    public static Long countActiveResponders() {
        return count("status", STATUS.ASSIGNED);
    }

    public static Long countEnrolledResponders() {
        return count() - count("status", STATUS.OFF_LINE);
    }

    public static Long countAllResponders() {
        return count();
    }

    public static Responder updateResponderInfo(Responder entity) {
        update("phoneNumber = ?1, boatCapacity = ?2, medicalKit = ?3 where responderId = ?4", entity.getPhoneNumber(), entity.getBoatCapacity(), entity.getMedicalKit(), entity.getResponderId());
        return find("responderId", entity.getResponderId()).firstResult();
    }

    public static Responder updateLocation(Responder responder) {
        update("latitude = ?1, longitude = ?2 where responderId = ?3", responder.getLatitude(), responder.getLongitude(), responder.getResponderId());
        return find("responderId", responder.getResponderId()).firstResult();
    }

    public static Responder updateResponderStatus(Responder entity) {
        update("status = ?1 where responderId = ?2", entity.getStatus(), entity.getResponderId());
        return find("responderId", entity.getResponderId()).firstResult();
    }

    public static void reset() {
        update("status = ?1", STATUS.OFF_LINE);
    }

    public static void resetBots() {
        update("status = ?1 where person = false", STATUS.OFF_LINE);
    }

    public static void resetPerson() {
        update("status = ?1, latitude = null, longitude = null where person = true", STATUS.OFF_LINE);
    }

    public static void deleteBots() {
        delete("person", false);
    }
}
