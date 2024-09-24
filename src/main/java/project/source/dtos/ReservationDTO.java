package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import project.source.models.entities.Reservation;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDTO {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("hotel_id")
    private Long hotelId;

    @Future(message = "Start date must be in the future")
    private LocalDate checkIn;

    @Future(message = "End date must be in the future")
    private LocalDate checkOut;

    @Min(value = 1, message = "Number of adults must be greater than 0")
    private int adults;

    @Min(value = 0, message = "Number of children must be greater than or equal to 0")
    private int children;

    public static ReservationDTO fromReservation(Reservation reservation) {
        return ReservationDTO.builder()
                .roomId(reservation.getRoom().getId())
                .userId(reservation.getUser().getId())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .build();
    }

    public Reservation toReservation() {
        return Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .adults(adults)
                .children(children)
                .build();
    }
}
