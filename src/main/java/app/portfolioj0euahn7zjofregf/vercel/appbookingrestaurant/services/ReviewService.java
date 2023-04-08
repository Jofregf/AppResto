package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    public ReviewDTO createReview(String restaurantId, ReviewDTO reviewDTO, String Token);

    public ReviewDTO updateReview(String reviewId, ReviewDTO reviewDTO, String token);

    public void deleteReview(String reviewId, String token);

    List<ReviewDTO> findReviewsByRestaurantId(String restaurantId);

}
