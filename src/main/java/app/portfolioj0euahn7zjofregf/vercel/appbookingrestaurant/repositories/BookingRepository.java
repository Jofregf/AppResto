package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingModel, String> {

    public List<BookingModel> findByUser_UserId(String userId);

    public List<BookingModel> findByRestaurant_RestaurantId(String restaurantId);

    public List<BookingModel> findByBookingDate(LocalDate date);
}
