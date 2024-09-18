package project.source.services;
/**
 * @autor An Nguyen
 */

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.ReviewsDTO;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reviews;
import project.source.repositories.ReviewsRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

    private final ReviewsRepository reviewsRepository;
    private final HotelService hotelService;

    @Override
    public void saveReview(Long hotelId, Long userId, ReviewsDTO reviewsDTO) {
        Hotel hotel =hotelService.getHotelById(hotelId);
        //User user = userService
        Reviews reviews = Reviews.builder()
                .comment(reviewsDTO.getComment())
                .rating(reviewsDTO.getRating())
                .hotel(hotel)
                .build();
        reviewsRepository.save(reviews);
    }

    @Override
    public Page<Reviews> getAllReviews(Long id, PageRequest pageRequest) {
        return reviewsRepository.findAllById(id, pageRequest);
    }

    @Override
    public void updateReview(Long hotelId, Long userId, ReviewsDTO reviewsDTO) {

    }
}
