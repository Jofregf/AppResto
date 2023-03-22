package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/user/{userId}/restaurant/{restaurantId}/booking")
    public ResponseEntity<String> createBooking(@PathVariable String userId,
                                                @PathVariable String restaurantId,
                                                @RequestBody BookingDTO bookingDTO){

        boolean bookingCreated = bookingService.createBooking(bookingDTO, restaurantId, userId);
        return ResponseEntity.ok("Booking created successfully");
    }

    @DeleteMapping("/user/{userId}/restaurant/{restaurantId}/booking/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable String userId,
                                                @PathVariable String restaurantId,
                                                @PathVariable String bookingId){

        bookingService.deleteBooking(bookingId, userId, restaurantId);
        return new ResponseEntity<>("Booking deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/restaurant/{restaurantId}/booking/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable String userId,
                                                    @PathVariable String restaurantId,
                                                    @PathVariable String bookingId,
                                                    @RequestBody BookingDTO bookingDTO){

        BookingDTO bookingResponse = bookingService.updateBooking(bookingDTO, userId, restaurantId, bookingId);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/booking")
    public List<BookingDTO> listBookingByUserId(@PathVariable String userId){
        return bookingService.findBookingByUserId(userId);
    }

    @GetMapping("/restaurant/{restaurantId}/booking")
    public List<BookingDTO> listBookingByRestaurantId(@PathVariable String restaurantId){
        return bookingService.findBookingByRestaurantId(restaurantId);
    }

    @GetMapping("/booking/date/{date}")
    public List<BookingDTO> listBookingByDate(@PathVariable LocalDate date){
        return bookingService.findByBookingDate(date);
    }
}
