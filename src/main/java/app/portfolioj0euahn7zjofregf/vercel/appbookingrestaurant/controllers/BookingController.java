package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.BookingService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.DeleteBearerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DeleteBearerService deleteBearerService;

    @PostMapping("/bookings/restaurant/{restaurantId}") //TODO: LISTO
    public ResponseEntity<BookingDTO> createBooking(@PathVariable String restaurantId,
                                                    @Valid @RequestBody BookingDTO bookingDTO,
                                                    @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return new ResponseEntity<>(bookingService.createBooking(bookingDTO, restaurantId, token), HttpStatus.CREATED);
    }

    @DeleteMapping("/bookings/{bookingId}") //TODO: LISTO!!!!!!!
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId,
                                                @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        bookingService.deleteBooking(bookingId, token);
        return new ResponseEntity<>("Booking deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/bookings/{bookingId}")//TODO: LISTO!!!!!!!
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable String bookingId,
                                                    @Valid @RequestBody BookingDTO bookingDTO,
                                                    @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        BookingDTO bookingResponse = bookingService.updateBooking(bookingDTO, bookingId, token);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

//    @GetMapping("/user/{userId}/booking")
    @GetMapping("/bookings")//TODO: LISTO!!!!!!!
    public List<BookingDTO> listBookingByUserId(@RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return bookingService.findBookingByUserId(token);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")//TODO: LISTO!!!!!!!
    @GetMapping("/restaurants/{restaurantName}/bookings")
    public List<BookingDTO> listBookingByRestaurantId(@PathVariable String restaurantName,
                                                      @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return bookingService.findBookingByRestaurantName(restaurantName, token);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")//TODO: LISTO!!!!!!!
    @GetMapping("restaurants/{restaurantName}/bookings/{date}")
    public List<BookingDTO> listBookingByDate(@PathVariable String restaurantName,
                                              @PathVariable LocalDate date,
                                              @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return bookingService.findByBookingDate(date, restaurantName, token);
    }

}
