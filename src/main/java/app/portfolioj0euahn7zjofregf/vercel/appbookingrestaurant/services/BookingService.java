package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingUserInfoDTO;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    public BookingDTO createBooking(BookingDTO booking, String restaurantId, String token);

    public void deleteBooking(String bookingId, String token);

    public BookingDTO updateBooking(BookingDTO DTO, String bookingId, String token);

    public List<BookingUserInfoDTO> findBookingByUserId(String token);

    public List<BookingDTO> findBookingByRestaurantName(String restaurantName, String token);

    public List<BookingDTO> findByBookingDate (LocalDate date, String restaurantName, String token);

}
