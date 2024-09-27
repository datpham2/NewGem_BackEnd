package project.source.respones;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import project.source.models.entities.Blog;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogListResponse {
    private List<Blog> blogList;
    @JsonProperty(value = "total_page")
    private int totalPage;
}
