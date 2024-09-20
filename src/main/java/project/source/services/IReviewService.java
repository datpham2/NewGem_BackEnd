package project.source.services;
/**
 * @autor An Nguyen
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.source.dtos.ReviewsDTO;
import project.source.models.entities.Reviews;

import java.util.List;

public interface IReviewService {
    void saveReview(Long hotelId, Long userId, ReviewsDTO reviewsDTO);
    Page<Reviews> getAllReviews(Long id, PageRequest pageRequest);
    void updateReview(Long id, ReviewsDTO reviewsDTO);
}
