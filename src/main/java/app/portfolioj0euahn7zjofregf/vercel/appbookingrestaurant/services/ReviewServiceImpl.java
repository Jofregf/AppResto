package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.ReviewModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.ReviewRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private ReviewDTO mapDTO(ReviewModel reviewModel){

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(reviewModel.getReviewId());
        reviewDTO.setRatingReview(reviewModel.getRatingReview());
        reviewDTO.setCommentReview(reviewModel.getCommentReview());
        reviewDTO.setUserName(reviewModel.getUser().getUserName());

        return reviewDTO;
    }

    private ReviewModel mapEntity(ReviewDTO reviewDTO){

        ReviewModel review = new ReviewModel();
        review.setReviewId(reviewDTO.getReviewId());
        review.setRatingReview(reviewDTO.getRatingReview());
        review.setCommentReview(reviewDTO.getCommentReview());


        return review;
    }

    @Override
    public ReviewDTO createReview(String restaurantId, ReviewDTO reviewDTO, String token) {

        ReviewModel review = mapEntity(reviewDTO);

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        review.setUser(user);

        review.setRestaurant(restaurant);

        ReviewModel newReview = reviewRepository.save(review);

        Double avgRating = reviewRepository.getAverageRatingForRestaurant(restaurantId);
        avgRating = avgRating == null ? 0.0 : avgRating;

        restaurant.setAverageRating((double) Math.round(avgRating));
        restaurantRepository.save(restaurant);

        return mapDTO(newReview);
    }

    @Override
    public ReviewDTO updateReview(String reviewId, ReviewDTO reviewDTO, String token) {

        ReviewModel review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "Id", reviewId));

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if(!userId.equals(review.getUser().getUserId())) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The review does not belong to the user.");
        }

        int newRating = reviewDTO.getRatingReview();
        review.setRatingReview(newRating);
        review.setCommentReview(reviewDTO.getCommentReview());
        ReviewModel reviewUpdate = reviewRepository.save(review);

        RestaurantModel restaurant = review.getRestaurant();
        Double avgRating = reviewRepository.getAverageRatingForRestaurant(restaurant.getRestaurantId());
        avgRating = avgRating == null ? 0.0 : avgRating;
        restaurant.setAverageRating((double) Math.round(avgRating));
        restaurantRepository.save(restaurant);

        return mapDTO(reviewUpdate);
    }

    @Override
    public void deleteReview(String reviewId, String token) {

        ReviewModel review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "Id", reviewId));

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if(!userId.equals(review.getUser().getUserId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The review does not belong to the user.");
        }
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> findReviewsByRestaurantId(String restaurantId) {

        List<ReviewModel> reviews = reviewRepository.findByRestaurant_RestaurantId(restaurantId);

        List<ReviewDTO> listReviews = reviews.stream().map(review -> mapDTO(review)).collect(Collectors.toList());

        if(listReviews.isEmpty()){
            throw new ResourceNotFoundException("Reviews", "Id", restaurantId);
        }

        return listReviews;
    }

}
