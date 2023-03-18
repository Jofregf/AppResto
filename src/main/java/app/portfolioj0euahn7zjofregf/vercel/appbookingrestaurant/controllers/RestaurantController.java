package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/user/{userId}/restaurant")
    public ResponseEntity<RestaurantDTO> saveRestaurant(@PathVariable(value = "userId")String userId,
                                                        @RequestBody RestaurantDTO restaurantDTO){

        return new ResponseEntity<>(restaurantService.createRestaurant(userId, restaurantDTO), HttpStatus.CREATED);
    }

    @GetMapping("/restaurants")
    public RestaurantResponse restaurantsList(@RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "9", required = false) int pageSize){
        return restaurantService.getRestaurants(pageNumber, pageSize);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable String restaurantId){
        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @PutMapping("/restaurant/{userId}/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable String userId,
                                                          @PathVariable String restaurantId,
                                                          @RequestBody RestaurantDTO restaurantDTO){

        RestaurantDTO restaurantResponse = restaurantService.updateRestaurant(userId, restaurantId, restaurantDTO);
        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    @DeleteMapping("/restaurant/{userId}/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable String userId, @PathVariable String restaurantId){
        restaurantService.deleteRestaurant(userId, restaurantId);
        return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/admin/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateEnable(@PathVariable String restaurantId,
                                                      @RequestBody RestaurantDTO restaurantDTO){

        RestaurantDTO restaurantResponse = restaurantService.updateEnabled(restaurantDTO, restaurantId);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    @GetMapping("/restaurants/menu/{menuName}")
    public List<RestaurantDTO> listRestaurantsByMenu(@PathVariable String menuName){
        return restaurantService.findRestaurantsByMenuName(menuName);
    }

    @GetMapping("/booking/{bookingId}/restaurant")
    public RestaurantDTO findRestaurantByBooking(@PathVariable String bookingId){
        return restaurantService.findRestaurantByBookingId(bookingId);
    }
}
