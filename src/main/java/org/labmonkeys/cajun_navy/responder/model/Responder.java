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

    public static List<Responder> findByAvailable(int limit, int offset) {
        if (limit == 0) {
            return find("available, enrolled", true, true).list();
        } else {
            int first = offset;
            int last = offset+limit-1;
            return find("available, enrolled", true, true).range(first, last).list();
        }
    }

    public static List<Responder> findAllResponders(int limit, int offset) {
        if (limit == 0) {
            return findAll().list();
        } else {
            int first = offset;
            int last = offset+limit-1;
            return findAll().range(first, last).list();
        }
    }

    public static List<Responder> findPersons(int limit, int offset) {
        if (limit == 0) {
            return find("person", true).list();
        } else {
            int first = offset;
            int last = offset+limit-1;
            return find("person", true).range(first, last).list();
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
        return count("enrolled, available", true, false);
    }

    public static Long countEnrolledResponders() {
        return count("enrolled", true);
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

    public static Responder updateResponderAvailable(Responder entity) {
        update("available = ?1, enrolled = $2 where responderId = ?3", entity.getAvailable(), entity.getEnrolled(), entity.getResponderId());
        return find("responderId", entity.getResponderId()).firstResult();
    }

    public static void reset() {
        update("available = true, enrolled = false");
    }

    public static void resetBot() {
        update("available = true, enrolled = false where person = false");
    }

    public static void resetPerson() {
        update("available = true, enrolled = false, latitude = null, longitude = null where person = true");
    }

    public static void clearBots() {
        update("available = false, enrolled = false where person = false");
    }

    public static void deleteBots() {
        delete("person", false);
    }
}
