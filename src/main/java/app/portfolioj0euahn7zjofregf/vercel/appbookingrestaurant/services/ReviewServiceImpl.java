package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.ReviewModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ForbiddenException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.ReviewRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ReviewDTO mapDTO(ReviewModel reviewModel){

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(reviewModel.getReviewId());
        reviewDTO.setRatingReview(reviewModel.getRatingReview());
        reviewDTO.setCommentReview(reviewModel.getCommentReview());

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
    public ReviewDTO createReview(String userId, String restaurantId, ReviewDTO reviewDTO) {

        ReviewModel review = mapEntity(reviewDTO);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));


        review.setUser(user);

        review.setRestaurant(restaurant);

        ReviewModel newReview = reviewRepository.save(review);

        Double avgRating = reviewRepository.getAverageRatingForRestaurant(restaurantId);
        avgRating = avgRating == null ? 0.0 : avgRating;

        restaurant.setAverageRanting(avgRating);
        restaurantRepository.save(restaurant);

        return mapDTO(newReview);
    }

    @Override
    public ReviewDTO updateReview(String reviewId, ReviewDTO reviewDTO, String userId) {

        ReviewModel review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if(userId.equals(review.getUser().getUserId())) {
            int oldRating = review.getRatingReview();
            int newRating = reviewDTO.getRatingReview();
            review.setRatingReview(newRating);
            review.setCommentReview(reviewDTO.getCommentReview());
            ReviewModel reviewUpdate = reviewRepository.save(review);

            RestaurantModel restaurant = review.getRestaurant();
            Double avgRating = reviewRepository.getAverageRatingForRestaurant(restaurant.getRestaurantId());
            avgRating = avgRating == null ? 0.0 : avgRating;
            restaurant.setAverageRanting(avgRating);
            restaurantRepository.save(restaurant);

            return mapDTO(reviewUpdate);
        }
        else {
            throw new ForbiddenException("You don't have permission to delete this review.");
        }

    }

    @Override
    public void deleteReview(String reviewId, String userId) {

        ReviewModel review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if(userId.equals(review.getUser().getUserId())){
            reviewRepository.delete(review);
        }
        else {
            throw new ForbiddenException("You don't have permission to delete this review.");
        }
    }

    @Override
    public List<ReviewDTO> getReviews() {

        List<ReviewModel> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewList = reviews.stream().map(review -> mapDTO(review)).collect(Collectors.toList());

        return reviewList;
    }

    @Override
    public List<ReviewDTO> findReviewsByRestaurantId(String restaurantId) {

        List<ReviewModel> reviews = reviewRepository.findByRestaurant_RestaurantId(restaurantId);

        List<ReviewDTO> listReviews = reviews.stream().map(review -> mapDTO(review)).collect(Collectors.toList());

        if(listReviews.isEmpty()){
            throw new ResourceNotFoundException("Reviews", "restaurantId", restaurantId);
        }
        return listReviews;
    }

    @Override
    public List<ReviewDTO> findReviewsByUserId(String userId) {

        List<ReviewModel> reviews = reviewRepository.findByUser_UserId(userId);

        List<ReviewDTO> listReviews = reviews.stream().map(review -> mapDTO(review)).collect(Collectors.toList());

        if(listReviews.isEmpty()){
            throw new ResourceNotFoundException("Reviews", "userId", userId);
        }
        return listReviews;
    }
}
