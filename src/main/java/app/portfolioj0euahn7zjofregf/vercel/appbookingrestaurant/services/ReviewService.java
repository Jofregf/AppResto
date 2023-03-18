package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    public ReviewDTO createReview(String userId, String restaurantId, ReviewDTO reviewDTO);

    public ReviewDTO updateReview(String reviewId, ReviewDTO reviewDTO, String userId);

    public void deleteReview(String reviewId, String userId);

    public List<ReviewDTO> getReviews();

    List<ReviewDTO> findReviewsByRestaurantId(String restaurantId);

    List<ReviewDTO> findReviewsByUserId(String userId);
}
