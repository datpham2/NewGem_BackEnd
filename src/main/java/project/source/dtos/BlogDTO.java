package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;
import project.source.models.entities.Image;
import project.source.models.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BlogDTO {


    @NotEmpty(message = "You must create a title for this blog")
    private String title;


    @NotEmpty(message = "Blogs can not be empty!!")
    private String content;

    @CreationTimestamp

    private LocalDateTime createdAt;

    @UpdateTimestamp

    private LocalDateTime updatedAt;

//    @Column (name = "author_id", referenceColumnName = "user_id", nullable = false)
//    private User user;


    private List<Image> images;

    Status status;

}
