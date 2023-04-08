package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingModel, String> {

    public List<BookingModel> findByUser_UserId(String userId);

    public List<BookingModel> findByRestaurant_RestaurantNameContainingIgnoreCase(String restaurantName);

    List<BookingModel> findByBookingDateAndRestaurant_RestaurantName(LocalDate bookingDate, String restaurantName);
}
