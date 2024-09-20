package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blogs")
public class Blog extends BaseEntity<Long>{

    @Column(name = "title", nullable = false)
    @NotEmpty(message = "You must create a title for this blog")
    private String title;

    @Column(name = "content", nullable = false)
    @NotEmpty(message = "Blogs can not be empty!!")
    private String content;

//    @Column (name = "author_id", referenceColumnName = "user_id", nullable = false)
//    private User user;

    @OneToMany
    private List<Image> images;
}
