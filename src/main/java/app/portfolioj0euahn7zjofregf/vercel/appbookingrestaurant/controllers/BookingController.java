package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(bookingCreated){
            return ResponseEntity.ok("Booking created successfully");
        }
        else{
            return ResponseEntity.badRequest().body("Booking creation failed!!");
        }
    }
}
