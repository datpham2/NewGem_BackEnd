package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDTO {
    @Future(message = "Check in date must be in the future")
    LocalDate checkIn;

    @Future(message = "Check out date must be in the future")
    LocalDate checkOut;

    @NotNull
    Long hotel;

    @NonNull
    Long room;

    @NonNull
    Long user;

    int adults;
    int children;
    Long bill;

    public static ReservationDTO fromReservation(Reservation reservation){
        return ReservationDTO.builder()
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .hotel(reservation.getHotel().getId())
                .room(reservation.getRoom().getId())
                .user(reservation.getUser().getId())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .build();
    }
}
