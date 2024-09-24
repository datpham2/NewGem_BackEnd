package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import project.source.models.enums.Status;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Slf4j
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reservations")
public class Reservation extends BaseEntity<Long> {
    @Column(name = "check_in", nullable = false)
    LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    LocalDate checkOut;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "adults")
    int adults;

    @Column(name = "children")
    int children;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bill_id")
    Bill bill;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;

    public double getTotalPrice() {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Check-out date cannot be before check-in date.");
        }
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);;
        return room.getPrice() * daysBetween;
    }
}