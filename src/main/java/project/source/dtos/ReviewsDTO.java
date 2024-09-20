package project.source.dtos;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReviewsDTO {
    @JsonProperty(value = "user_id")
    private Long userId;
    @JsonProperty(value = "hotel_id")
    private Long hotelId;
    @NotNull(message = "Comment can't be empty")
    private String comment;
    @Min(1)
    @Max(5)
    private int rating;
}
