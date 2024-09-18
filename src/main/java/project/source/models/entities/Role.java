package project.source.models.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity<Long> {
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

