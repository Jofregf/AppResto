package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/user/{userId}/{restaurantId}/review")
    public ResponseEntity<ReviewDTO> saveReview(@PathVariable(value = "userId")String userId,
                                                @PathVariable(value = "restaurantId")String restaurantId,
                                                @RequestBody ReviewDTO reviewDTO){

        return new ResponseEntity<>(reviewService.createReview(userId, restaurantId, reviewDTO), HttpStatus.CREATED);
    }

    @PutMapping("/review/{userId}/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String reviewId,
                                                  @PathVariable String userId,
                                                  @RequestBody ReviewDTO reviewDTO){
        ReviewDTO reviewResponse = reviewService.updateReview(reviewId, reviewDTO, userId);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @DeleteMapping("/review/{userId}/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String reviewId, @PathVariable String userId){
        reviewService.deleteReview(reviewId, userId);
        return new ResponseEntity<>("Review deleted succefully", HttpStatus.OK);
    }

    @GetMapping("/reviews")
    public List<ReviewDTO> getReviews(){
        return reviewService.getReviews();
    }

    @GetMapping("/restaurant/{restaurantId}/reviews")
    public List<ReviewDTO> listReviewsByRestaurant(@PathVariable String restaurantId){
        return reviewService.findReviewsByRestaurantId(restaurantId);
    }

    @GetMapping("/user/{userId}/reviews")
    public List<ReviewDTO> listReviewsByUser(@PathVariable String userId){
        return reviewService.findReviewsByUserId(userId);
    }

}
