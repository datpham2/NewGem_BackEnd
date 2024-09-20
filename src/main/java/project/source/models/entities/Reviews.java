package project.source.models.entities;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.source.models.enums.Status;

@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
public class Reviews extends BaseEntity<Long>{
    @NotNull(message = "Comment can't be empty")
    private String comment;
    @Min(1)
    @Max(5)
    private int rating;
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @Enumerated(EnumType.STRING)
    private Status status;
}
