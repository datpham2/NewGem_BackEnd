package project.source.controllers;
/**
 * @autor An Nguyen
 */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.ReviewsDTO;
import project.source.models.entities.Reviews;
import project.source.respones.ApiResponse;
import project.source.respones.ReviewResponse;
import project.source.services.implement.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Tag(name = "Review", description = "Operations related to reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            method = "POST",
            summary = "Post the review body to create a new review object",
            description = "Send a request with hotel body ('comment', 'rating') to build and save a new object for class 'Reviews'" )
    @PostMapping("/createReview")
    public ResponseEntity<ApiResponse> createReview(@RequestParam(value = "hotelId")Long hotelId,
                                                    @RequestParam(value = "userId")Long userId,
                                                    @RequestBody ReviewsDTO reviewsDTO){
        reviewService.saveReview(hotelId, userId, reviewsDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(reviewsDTO)
                        .message("Saved review successfully")
                        .status(HttpStatus.CREATED.value())
                .build());
    }

    @Operation(
            method = "GET",
            summary = "Get review by id",
            description = "Send a request to get the review with targeted id")
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
    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateReview(@Valid @PathVariable Long id,
                                                     @RequestBody ReviewsDTO reviewsDTO,
                                                     BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't Update Review")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            reviewService.updateReview(id, reviewsDTO);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(reviewsDTO)
                    .message("Update review successfully")
                    .status(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> disableReview(@PathVariable Long id){
        reviewService.disableReview(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Disable Review Successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
