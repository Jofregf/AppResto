package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, String> {

    public List<RestaurantModel> findByMenus_MenuNameContainingIgnoreCase(String menuName);

    public RestaurantModel findByBookings_BookingId(String BookingId);
}
