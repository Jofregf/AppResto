package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    public boolean createBooking(BookingDTO booking, String restaurantId, String userId);

    public void deleteBooking(String bookingId, String userId, String restaurantId);

    public BookingDTO updateBooking(BookingDTO DTO, String userId, String restaurantId, String bookingId);

    public List<BookingDTO> findBookingByUserId(String userId);

    public List<BookingDTO> findBookingByRestaurantId(String restaurantId);

    public List<BookingDTO> findByBookingDate (LocalDate date);
}
