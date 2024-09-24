package project.source.services.implement;
/**
 * @autor An Nguyen
 */

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.dtos.ReviewsDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reviews;
import project.source.models.entities.User;
import project.source.models.enums.Status;
import project.source.repositories.ReviewsRepository;
import project.source.services.IReviewService;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewsRepository reviewsRepository;
    private final HotelService hotelService;
    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void saveReview(Long hotelId, Long userId, ReviewsDTO reviewsDTO) {
        Hotel hotel =hotelService.getHotelById(hotelId);
        User user = userService.getUserById(userId);
        Reviews reviews = Reviews.builder()
                .comment(reviewsDTO.getComment())
                .rating(reviewsDTO.getRating())
                .hotel(hotel)
                .user(user)
                .status(Status.ACTIVE)
                .build();
        reviewsRepository.save(reviews);
    }

    @Override
    public Page<Reviews> getAllReviews(Long id, PageRequest pageRequest) {
        return reviewsRepository.findAllById(id, pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void updateReview(Long id, ReviewsDTO reviewsDTO) {
        Reviews reviews = reviewsRepository.findById(id).orElseThrow(()-> new NotFoundException("Review not found by Id = "+id));
        reviews.setRating(reviewsDTO.getRating());
        reviews.setComment(reviewsDTO.getComment());
        reviews.setHotel(hotelService.getHotelById(reviewsDTO.getHotelId()));
        reviews.setStatus(reviewsDTO.getStatus());
        reviews.setUser(userService.getUserById(reviewsDTO.getUserId()));
        reviewsRepository.save(reviews);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void disableReview(Long id) {
        Reviews reviews = reviewsRepository.findById(id).orElseThrow(()-> new NotFoundException("Review not found by Id = "+id));
        if(reviews.getStatus() == Status.ACTIVE){
            reviews.setStatus(Status.INACTIVE);
        }else {
            reviews.setStatus(Status.ACTIVE);
        }
        reviewsRepository.save(reviews);
    }
}
