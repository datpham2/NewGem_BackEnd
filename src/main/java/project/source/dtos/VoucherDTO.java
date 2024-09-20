package project.source.dtos;
/**
 * @Author An Nguyen
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.*;
import project.source.models.entities.Hotel;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class VoucherDTO {
    @Min(value = 1, message = "Discount must be greater than 1")
    private int discount;
    @Past(message = "Start Date must be one day in future")
    private Date startDate;
    @Past(message = "End Date must be one day in future")
    private Date endDate;
    private boolean active;
    @JsonProperty(value = "hotel_id")
    private Long hotelId;
}
