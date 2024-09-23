package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoomDTO {
    @JsonProperty("hotel_id")
    @NotNull(message = "Hotel id must not be null")
    private Long hotelId;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private double price;
    //

    @NotNull(message = "Type must not be null")
    @Enumerated(EnumType.STRING)
    private String type;

    @Min(value = 1, message = "Guests must be at least 1")
    private int guests;
}
