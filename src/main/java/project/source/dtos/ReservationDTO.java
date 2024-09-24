package project.source.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.*;
import project.source.models.enums.Status;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDTO {
    Long reservationId;

    @Future(message = "Check in date must be in the future")
    LocalDate checkIn;

    @Future(message = "Check out date must be in the future")
    LocalDate checkOut;

    @NotNull
    Long hotelId;

    @NonNull
    Long roomId;

    @NonNull
    Long userId;

    int adults;
    int children;
    Long billId;

    Status status;

    public static ReservationDTO fromReservation(Reservation reservation){
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .reservationId(reservation.getId())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .hotelId(reservation.getHotel().getId())
                .roomId(reservation.getRoom().getId())
                .userId(reservation.getUser().getId())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .status(reservation.getStatus())
                .build();

        if (reservation.getBill() != null){
                reservationDTO.setBillId(reservation.getBill().getId());
        }

        return reservationDTO;
    }
}
