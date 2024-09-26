package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import project.source.models.enums.Status;

import java.time.LocalDate;

/**
 * @Author An Nguyen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reviews extends BaseEntity<Long>{
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank(message = "Comment can't empty")
    private String comment;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @JsonIgnore

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
