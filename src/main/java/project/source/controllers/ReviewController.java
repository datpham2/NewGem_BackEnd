package project.source.controllers;
/**
 * @autor An Nguyen
 */
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.ReviewsDTO;
import project.source.models.entities.Reviews;
import project.source.respones.ApiResponse;
import project.source.respones.ReviewResponse;
import project.source.services.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/createReview")
    public ResponseEntity<ApiResponse> createReview(@RequestParam(value = "hotelId")Long hotelId,
                                                    @RequestParam(value = "userId")Long userId,
                                                    @RequestBody ReviewsDTO reviewsDTO){
        reviewService.saveReview(hotelId, 1L, reviewsDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(reviewsDTO)
                        .message("Saved review successfully")
                        .status(HttpStatus.CREATED.value())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> hotelDetail(@PathVariable(value = "id")Long id,
                                                   @RequestParam(value = "page", defaultValue = "0")int page,
                                                   @RequestParam(value = "size", defaultValue = "5")int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Reviews> reviews = reviewService.getAllReviews(id,pageRequest);
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .listReview(reviews.getContent())
                .totalPage(reviews.getTotalPages())
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all reviews successfully")
                .data(reviewResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
