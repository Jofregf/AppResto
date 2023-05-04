package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.DeleteBearerService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.RestaurantService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.utilities.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DeleteBearerService deleteBearerService;

    @GetMapping("/restaurants")
    public RestaurantResponse restaurantsList(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_NUMBER_PAGE, required = false)int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_BY_DIRECTION, required = false) String sortDir){

        return restaurantService.getRestaurants(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable String restaurantId){

        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @GetMapping("/search/menus/restaurants")
    public List<RestaurantDTO> listRestaurantsByMenu(@RequestHeader(value="menuName") String menuName){
        return restaurantService.findRestaurantsByMenuName(menuName);
    }

    @GetMapping("/bookings/{bookingId}/restaurant")
    public RestaurantDTO findRestaurantByBooking(@PathVariable String bookingId,
                                                 @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return restaurantService.findRestaurantByBookingId(bookingId, token);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDTO> saveRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO,
                                                        @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return new ResponseEntity<>(restaurantService.createRestaurant(restaurantDTO, token), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PutMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable String restaurantId,
                                                          @Valid @RequestBody RestaurantDTO restaurantDTO,
                                                          @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        RestaurantDTO restaurantResponse = restaurantService.updateRestaurant(restaurantId, restaurantDTO, token);
        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable String restaurantId,
                                                   @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        restaurantService.deleteRestaurant(restaurantId, token);
        return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateEnable(@PathVariable String restaurantId,
                                                      @RequestBody RestaurantDTO restaurantDTO,
                                                      @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        RestaurantDTO restaurantResponse = restaurantService.updateEnabled(restaurantDTO, restaurantId, token);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }
    @GetMapping("/restaurants/rating")
    public List<RestaurantDTO> listRestaurantsByAverageRating(@RequestHeader(value="rating") Double averageRating){
        return restaurantService.findRestaurantByAverageRating(averageRating);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @GetMapping("/restaurants/list")
    public List<RestaurantDTO> listRestaurantsByUserId(@RequestHeader(value="Authorization") String authorizHeader){
        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return restaurantService.findRestaurantByUserId(token);
    }
}
