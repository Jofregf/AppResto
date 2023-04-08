package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.DeleteBearerService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.ReviewService;
import jakarta.validation.Valid;
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

    @Autowired
    private DeleteBearerService deleteBearerService;

    @PostMapping("/reviews/restaurant/{restaurantId}")
    public ResponseEntity<ReviewDTO> saveReview(@PathVariable(value = "restaurantId")String restaurantId,
                                                @Valid @RequestBody ReviewDTO reviewDTO,
                                                @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return new ResponseEntity<>(reviewService.createReview(restaurantId, reviewDTO, token), HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String reviewId,
                                                  @Valid @RequestBody ReviewDTO reviewDTO,
                                                  @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        ReviewDTO reviewResponse = reviewService.updateReview(reviewId, reviewDTO, token);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String reviewId,
                                               @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        reviewService.deleteReview(reviewId, token);
        return new ResponseEntity<>("Review deleted succefully", HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}/reviews")
    public List<ReviewDTO> listReviewsByRestaurant(@PathVariable String restaurantId){
        return reviewService.findReviewsByRestaurantId(restaurantId);
    }


}
