package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;
import project.source.models.enums.Status;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private Set<Image> images = new HashSet<Image>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;
}
