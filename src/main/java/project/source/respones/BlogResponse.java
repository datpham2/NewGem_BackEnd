package project.source.respones;

import lombok.*;
import project.source.models.entities.Blog;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private Long author_id;

    public BlogResponse fromBlog(Blog blog) {
        BlogResponse blogResponse = BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
//                .author_id(blog.getAuthor_id())
                .build();
        return blogResponse;
    }
}
