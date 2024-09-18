package project.source.models.entities;
/**
 * @autor An Nguyen
 */
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Size(min = 1, max = 5, message = "Rate must between 1 and 5")
    private int rating;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
