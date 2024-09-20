package project.source.respones;
/**
 * @autor An Nguyen
 */
import lombok.*;
import project.source.models.entities.Reviews;

import java.util.List;

@Setter
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private List<Reviews> listReview;
    private int totalPage;
}
