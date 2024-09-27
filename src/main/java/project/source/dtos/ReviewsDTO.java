package project.source.dtos;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.source.models.entities.Reviews;
import project.source.models.entities.Voucher;
import project.source.models.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReviewsDTO {
    private Long reviewId;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "hotel_id")
    private Long hotelId;

    @NotNull(message = "Comment can't be empty")
    private String comment;

    @Min(1)
    @Max(5)
    private int rating;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static ReviewsDTO fromReview(Reviews reviews){
        return ReviewsDTO.builder()
                .reviewId(reviews.getId())
                .userId(reviews.getUser().getId())
                .hotelId(reviews.getHotel().getId())
                .comment(reviews.getComment())
                .rating(reviews.getRating())
                .status(reviews.getStatus())
                .build();
    }
}
