package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.CapacityExceededException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.BookingRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        BookingModel booking = mapEntity(bookingDTO);

        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getBookingTime());

        LocalTime closingTime = restaurant.getClosingHoursRestaurant();

        LocalDateTime closingDateTime;

        if(closingTime.isBefore(LocalTime.of(0,0))){
            closingDateTime = LocalDateTime.of(booking.getBookingDate(), closingTime);
        } else {
            closingDateTime = LocalDateTime.of(booking.getBookingDate().plusDays(1), closingTime);
        }

        if(!restaurant.getEnabled()){
            return false;
        }

        if (bookingDateTime.plusHours(1).isAfter(closingDateTime)) {
            return false;
        }

        Set<BookingModel> existingBooking = restaurant.getBookings().stream()
                .filter(book -> book.getBookingDate().equals(booking.getBookingDate()) &&
                        book.getBookingTime().equals(booking.getBookingTime()))
                .collect(Collectors.toSet());

        int totalCapacity = 0;
        for(BookingModel bookingCap:existingBooking){
            totalCapacity += bookingCap.getBookingPartySize();
        }

        if(totalCapacity + booking.getBookingPartySize() > restaurant.getRestaurantCapacity()){
            throw new CapacityExceededException("There is not enough capacity in the restaurant to make the reservation");

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
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        BookingModel booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "Id", bookingId));

        if(!userId.equals(booking.getUser().getUserId()) || !restaurantId.equals(booking.getRestaurant().getRestaurantId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The user or restaurant does not correspond to the booking");
        }

        bookingRepository.delete(booking);
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO, String userId, String restaurantId, String bookingId) {

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        BookingModel booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "Id", bookingId));

        if(!userId.equals(booking.getUser().getUserId()) || !restaurantId.equals(booking.getRestaurant().getRestaurantId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The user or restaurant does not correspond to the booking");
        }

        int totalCapacity = restaurant.getRestaurantCapacity();

        Set<BookingModel> existingBooking = restaurant
                .getBookings()
                .stream()
                .filter(book -> book.getBookingDate().equals(bookingDTO.getBookingDate()) &&
                        book.getBookingTime().equals(bookingDTO.getBookingTime()) &&
                        !book.getBookingId().equals(bookingId))
                .collect(Collectors.toSet());

        int usedCapacity = existingBooking
                .stream()
                .mapToInt(BookingModel::getBookingPartySize)
                .sum();

        int availableCapacity = totalCapacity - usedCapacity;

        if(bookingDTO.getBookingPartySize() > availableCapacity){
            throw new CapacityExceededException("There is not enough capacity in the restaurant to make the reservation");
        }

        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setBookingPartySize(bookingDTO.getBookingPartySize());

        BookingModel bookingUpdated = bookingRepository.save(booking);

        return mapDTO(bookingUpdated);
    }

    @Override
    public List<BookingDTO> findBookingByUserId(String userId) {

        List<BookingModel> bookings = bookingRepository.findByUser_UserId(userId);

        List<BookingDTO> listBookings = bookings.stream().map(booking -> mapDTO(booking)).collect(Collectors.toList());

        if(listBookings.isEmpty()){
            throw new ResourceNotFoundException("Bookings", "User Id", userId);
        }

        return listBookings;
    }

    @Override
    public List<BookingDTO> findBookingByRestaurantId(String restaurantId) {

        List<BookingModel> bookings = bookingRepository.findByRestaurant_RestaurantId(restaurantId);

        List<BookingDTO> listBookings = bookings.stream().map(booking -> mapDTO(booking)).collect(Collectors.toList());

        if(listBookings.isEmpty()){
            throw new ResourceNotFoundException("Bookings", "Restaurant Id", restaurantId);
        }

        return listBookings;
    }

    @Override
    public List<BookingDTO> findByBookingDate(LocalDate date) {

        List<BookingModel> bookings = bookingRepository.findByBookingDate(date);

        List<BookingDTO> listBookings = bookings.stream().map(booking -> mapDTO(booking)).collect(Collectors.toList());

        if(listBookings.isEmpty()){
            throw new ResourceNotFoundException("Bookings", "Date", date);
        }

        return listBookings;
    }
}
