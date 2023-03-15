package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.BookingRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    private BookingDTO mapDTO(BookingModel bookingModel){

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(bookingModel.getBookingId());
        bookingDTO.setBookingDate(bookingModel.getBookingDate());
        bookingDTO.setBookingTime(bookingModel.getBookingTime());
        bookingDTO.setBookingPartySize(bookingModel.getBookingPartySize());

        return bookingDTO;
    }

    private BookingModel mapEntity(BookingDTO bookingDTO){

        BookingModel booking = new BookingModel();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setBookingPartySize(bookingDTO.getBookingPartySize());

        return booking;
    }

    @Override
    public boolean createBooking(BookingDTO bookingDTO, String restaurantId, String userId) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        BookingModel booking = mapEntity(bookingDTO);

        LocalTime closingTime = restaurant.getClosingHoursRestaurant();

        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getBookingTime());

        if(!restaurant.getEnabled()){
            return false;
        }

        if(bookingDateTime.plusHours(1).toLocalTime().isAfter(closingTime)){
            return false;
        }
//        LocalDateTime bookingDateTime = booking.getBookingDate().atTime(booking.getBookingTime());


        Set<BookingModel> existingBooking = restaurant.getBookings().stream()
                .filter(book -> book.getBookingDate().equals(booking.getBookingDate()) &&
                        book.getBookingTime().equals(booking.getBookingTime()))
                .collect(Collectors.toSet());

        int totalCapacity = 0;
        for(BookingModel bookingCap:existingBooking){
            totalCapacity += bookingCap.getBookingPartySize();
        }

        if(totalCapacity + booking.getBookingPartySize() > restaurant.getRestaurantCapacity()){
            return false;
        }

        booking.setRestaurant(restaurant);
        booking.setUser(user);
        BookingModel newBooking = bookingRepository.save(booking);

        return true;
    }

    @Override
    public void deleteBooking(String bookingId, String userId, String restaurantId) {

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        BookingModel booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

        bookingRepository.delete(booking);
    }
}
