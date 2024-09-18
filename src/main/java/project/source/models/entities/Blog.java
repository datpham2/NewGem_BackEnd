package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

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

    //   @Column (name = "author_id", nullable = false)
//    private int author_id;

//    @ManyToOne
//    @JoinColumn(name = "images", referencedColumnName = "image_id", nullable = false)
//    private
}
