package project.source.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.services.implement.HotelService;

import java.util.Set;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDTO {
    @NotNull
    Long id;

    @NonNull
    Long hotelId;

    @NotNull
    int roomNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    double price;

    @NotNull(message = "Type must not be null")
    RoomType type;

    int guests;

    public static RoomDTO fromRoom(Room room){
        return RoomDTO.builder()
                .id(room.getId())
                .hotelId(room.getHotel().getId())
                .roomNumber(room.getRoomNumber())
                .price(room.getPrice())
                .type(room.getType())
                .build();
    }
}
