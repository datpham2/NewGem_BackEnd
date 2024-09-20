package project.source.dtos;
/**
 * @Author An Nguyen
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.*;
import project.source.models.enums.Status;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class VoucherDTO {
    @Min(value = 1, message = "Discount must be greater than 1")
    private int discount;
    @FutureOrPresent(message = "Start Date must be one day in future")
    private LocalDate startDate;
    @FutureOrPresent(message = "End Date must be one day in future")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonProperty(value = "hotel_id")
    private Long hotelId;
}
