package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;
import project.source.models.entities.Blog;
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
    private Long blogId;

    @NotEmpty(message = "You must create a title for this blog")
    private String title;


    @NotEmpty(message = "Blogs can not be empty!!")
    private String content;

    private Long userId;

    private List<Image> images;

    Status status;

    public static BlogDTO fromBlog(Blog blog){
        return BlogDTO.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .images(blog.getImages().stream().toList())
                .content(blog.getContent())
                .userId(blog.getUser().getId())
                .status(blog.getStatus())
                .build();
    }
}
