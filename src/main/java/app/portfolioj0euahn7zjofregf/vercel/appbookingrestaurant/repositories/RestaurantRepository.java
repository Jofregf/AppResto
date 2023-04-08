package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, String> {

    public List<RestaurantModel> findByMenus_MenuNameContainingIgnoreCase(String menuName);

    public RestaurantModel findByBookings_BookingId(String BookingId);

    public List<RestaurantModel> findByUser_UserId(String userId);

    public Optional<RestaurantModel> findByUser_UserIdAndRestaurantNameContainingIgnoreCase(String userId, String restaurantName);
}
