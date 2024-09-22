package project.source.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDTO {
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    double price;

    @NotNull(message = "Type must not be null")
    String type;

    int guests;
}
