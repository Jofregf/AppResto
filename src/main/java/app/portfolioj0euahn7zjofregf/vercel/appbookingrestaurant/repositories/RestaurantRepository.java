package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantModel, String> {

    public List<RestaurantModel> findByMenus_MenuName(String menuName);

    public RestaurantModel findByBookings_BookingId(String BookingId);
}
