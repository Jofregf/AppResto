package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewModel, String> {

    public List<ReviewModel> findByRestaurant_RestaurantId(String restaurantId);

    public List<ReviewModel> findByUser_UserId(String userId);

    @Query("SELECT AVG(r.ratingReview) FROM ReviewModel r WHERE r.restaurant.restaurantId = :restaurantId")
    Double getAverageRatingForRestaurant(@Param("restaurantId") String restaurantId);
}
