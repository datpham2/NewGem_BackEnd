package project.source.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private double price;

    @NotNull(message = "Type must not be null")
    private String type;

    private int guests;
}
