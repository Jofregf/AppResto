package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingUserInfoDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantUserInfoDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.CapacityExceededException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.BookingRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private BookingDTO mapDTO(BookingModel bookingModel) {

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(bookingModel.getBookingId());
        bookingDTO.setBookingDate(bookingModel.getBookingDate());
        bookingDTO.setBookingTime(bookingModel.getBookingTime());
        bookingDTO.setBookingPartySize(bookingModel.getBookingPartySize());
        bookingDTO.setActive(bookingModel.isActive());
        bookingDTO.setRestaurant(bookingModel.getRestaurant());

        return bookingDTO;
    }

    private BookingModel mapEntity(BookingDTO bookingDTO) {

        BookingModel booking = new BookingModel();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setBookingPartySize(bookingDTO.getBookingPartySize());
        booking.setActive(bookingDTO.isActive());

        return booking;
    }

    private BookingUserInfoDTO mapUserInfo(BookingModel bookingModel){
        BookingUserInfoDTO bookingUserInfoDTO = new BookingUserInfoDTO();
        bookingUserInfoDTO.setBookingId(bookingModel.getBookingId());
        bookingUserInfoDTO.setBookingDate(bookingModel.getBookingDate());
        bookingUserInfoDTO.setBookingTime(bookingModel.getBookingTime());
        bookingUserInfoDTO.setBookingPartySize(bookingModel.getBookingPartySize());
        bookingUserInfoDTO.setActive(bookingModel.isActive());

        RestaurantUserInfoDTO restaurantUserInfoDTO = new RestaurantUserInfoDTO();
        restaurantUserInfoDTO.setRestaurantId(bookingModel.getRestaurant().getRestaurantId());
        restaurantUserInfoDTO.setRestaurantName(bookingModel.getRestaurant().getRestaurantName());
        restaurantUserInfoDTO.setRestaurantAddress(bookingModel.getRestaurant().getRestaurantAddress());
        restaurantUserInfoDTO.setRestaurantPhone(bookingModel.getRestaurant().getRestaurantPhone());
        restaurantUserInfoDTO.setRestaurantEmail(bookingModel.getRestaurant().getRestaurantEmail());

        bookingUserInfoDTO.setRestaurant(restaurantUserInfoDTO);

        return bookingUserInfoDTO;
        }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO, String restaurantId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        UserModel user = userRepository
                .findById(idToken)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", idToken));

        BookingModel booking = mapEntity(bookingDTO);

        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getBookingTime());

        LocalTime openingTime = restaurant.getOpeningHoursRestaurant();
        LocalTime closingTime = restaurant.getClosingHoursRestaurant();

        LocalDateTime closingDateTime;
        if (closingTime.isBefore(openingTime)) {
            closingDateTime = LocalDateTime.of(booking.getBookingDate().plusDays(1), closingTime);
        } else {
            closingDateTime = LocalDateTime.of(booking.getBookingDate(), closingTime);
        }

        LocalDateTime now = LocalDateTime.now();
        if (bookingDateTime.isBefore(now)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The reservation time is in the past.");
        } else if (!bookingDateTime.toLocalTime().isAfter(openingTime) && !bookingDateTime.toLocalTime().isBefore(closingTime.minusHours(1))) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The restaurant is not open at the reservation time.");
        } else if (bookingDateTime.plusHours(1).isAfter(closingDateTime)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The restaurant does not accept reservations for that time.");
        }

        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "You are trying to reserve a date prior to the current one.");
        }

        if (!restaurant.getEnabled()) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The restaurant is not available.");
        }

        Set<BookingModel> existingBooking = restaurant.getBookings().stream()
                .filter(book -> book.getBookingDate().equals(booking.getBookingDate()) &&
                        book.getBookingTime().equals(booking.getBookingTime()))
                .collect(Collectors.toSet());

        int totalCapacity = 0;
        for (BookingModel bookingCap : existingBooking) {
            totalCapacity += bookingCap.getBookingPartySize();
        }

        if (totalCapacity + booking.getBookingPartySize() > restaurant.getRestaurantCapacity()) {
            throw new CapacityExceededException("There is not enough capacity in the restaurant to make the reservation");

        }

        booking.setRestaurant(restaurant);
        booking.setUser(user);
        booking.setActive(true);
        BookingModel newBooking = bookingRepository.save(booking);

        return mapDTO(newBooking);
    }

    @Override
    public void deleteBooking(String bookingId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);

        BookingModel booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "Id", bookingId));

        if(userId == null || !userId.equals(booking.getUser().getUserId())){
            throw new AccessDeniedException("Access denied, user does not correspond to the booking");
        }

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String restaurantId = booking.getRestaurant().getRestaurantId();

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        if (!restaurant.getRestaurantId().equals(booking.getRestaurant().getRestaurantId())) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The restaurant does not correspond to the booking");
        }

        bookingRepository.delete(booking);
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO, String bookingId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId =  jwtTokenProvider.getUserIdFromToken(token);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        BookingModel booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "Id", bookingId));

        if(userId == null || !userId.equals(booking.getUser().getUserId())){
            throw new AccessDeniedException("Access denied, user does not correspond to the booking");
        }

        String restaurantId = booking.getRestaurant().getRestaurantId();
        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        if (!restaurant.getRestaurantId().equals(booking.getRestaurant().getRestaurantId())) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The restaurant does not correspond to the booking");
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

        if (bookingDTO.getBookingPartySize() > availableCapacity) {
            throw new CapacityExceededException("There is not enough capacity in the restaurant to make the reservation");
        }

        booking.setBookingDate(bookingDTO.getBookingDate());
        LocalDate currentDate = LocalDate.now();

        if (bookingDTO.getBookingDate().isBefore(currentDate)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "You are trying to reserve a date prior to the current one.");
        }
        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setBookingPartySize(bookingDTO.getBookingPartySize());
        booking.setActive(true);

        BookingModel bookingUpdated = bookingRepository.save(booking);

        return mapDTO(bookingUpdated);
    }

    @Override
    public List<BookingUserInfoDTO> findBookingByUserId(String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        List<BookingModel> bookings = bookingRepository.findByUser_UserId(userId);

        List<BookingUserInfoDTO> listBookings = bookings.stream()
                .map(booking -> mapUserInfo(booking)).collect(Collectors.toList());

        if(userId == null){
            throw new AccessDeniedException("Access denied, user does not correspond to the booking");
        }

        if (listBookings.isEmpty()) {
            throw new ResourceNotFoundException("Bookings", "User Id", userId);
        }

        return listBookings;
    }

    @Override
    public List<BookingDTO> findBookingByRestaurantName(String restaurantName, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        List<RestaurantModel> restaurantsList = restaurantRepository.findByUser_UserId(userId);
        Optional<RestaurantModel> restaurant = restaurantsList.stream()
                .filter(resto -> resto.getRestaurantName().equalsIgnoreCase(restaurantName))
                .findFirst();
        if(!restaurant.isPresent()){
            throw new ResourceNotFoundException("Restaurant", "name", restaurantName);
        }

        List<BookingModel> bookings = bookingRepository.findByRestaurant_RestaurantNameContainingIgnoreCase(restaurantName);

        List<BookingDTO> listBookings = bookings.stream().map(booking -> mapDTO(booking)).collect(Collectors.toList());

        if (listBookings.isEmpty()) {
            throw new ResourceNotFoundException("Bookings", "Restaurant name", restaurantName);
        }

        return listBookings;

    }

    @Override
    public List<BookingDTO> findByBookingDate(LocalDate date, String restaurantName, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        RestaurantModel restaurant = restaurantRepository
                .findByUser_UserIdAndRestaurantNameContainingIgnoreCase(userId, restaurantName)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "name", restaurantName));

        List<BookingModel> bookings = bookingRepository.findByBookingDateAndRestaurant_RestaurantName(date, restaurant.getRestaurantName());

        List<BookingDTO> listBookings = bookings
                .stream()
                .map(booking -> mapDTO(booking))
                .collect(Collectors.toList());

        if (listBookings.isEmpty()) {
            throw new ResourceNotFoundException("Bookings", "Date and Restaurant Name", date + " and " + restaurantName);
        }

        return listBookings;
    }

    @Scheduled(fixedDelay = 1296000000)
    public void updateBookingStatus() {
        LocalDate currentDate = LocalDate.now();
        LocalDate updateActiveDate = currentDate.minusDays(7);
        LocalDate deleteInactiveDate = currentDate.minusDays(30);
        List<BookingModel> bookings = bookingRepository.findAll();
        for (BookingModel booking : bookings) {
            if (booking.getBookingDate().isBefore(updateActiveDate)) {
                booking.setActive(false);
                bookingRepository.save(booking);
            }
        }
    }
}
