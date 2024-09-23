package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.source.models.entities.Reservation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDTO {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("user_id")
    private Long userId;

    @Future(message = "Start date must be in the future")
    private String checkIn;

    @Future(message = "End date must be in the future")
    private String checkOut;

    @Min(value = 1, message = "Number of adults must be greater than 0")
    private int adults;

    @Min(value = 0, message = "Number of children must be greater than or equal to 0")
    private int children;

    public static ReservationDTO fromReservation(Reservation reservation) {
        return ReservationDTO.builder()
                .roomId(reservation.getRoom().getId())
                .userId(reservation.getUser().getId())
                .checkIn(reservation.getCheckIn().toString())
                .checkOut(reservation.getCheckOut().toString())
                .adults(reservation.getAdults())
                .children(reservation.getChildren())
                .build();
    }
}
