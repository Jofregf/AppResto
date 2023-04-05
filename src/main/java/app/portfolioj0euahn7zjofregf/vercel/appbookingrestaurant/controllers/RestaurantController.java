package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.RestaurantService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.utilities.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants") //TODO: YA LISTO!!!!!
    public RestaurantResponse restaurantsList(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_NUMBER_PAGE, required = false)int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_BY_DIRECTION, required = false) String sortDir){

        return restaurantService.getRestaurants(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/restaurants/{restaurantId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable String restaurantId){

        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @GetMapping("/menus/restaurants") //TODO: YA LISTO!!!!!
    public List<RestaurantDTO> listRestaurantsByMenu(@RequestHeader(value="menuName") String menuName){

        return restaurantService.findRestaurantsByMenuName(menuName);
    }

    @GetMapping("/booking/{bookingId}/restaurant")
    public RestaurantDTO findRestaurantByBooking(@PathVariable String bookingId){

        return restaurantService.findRestaurantByBookingId(bookingId);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PostMapping("/restaurants") //TODO: YA LISTO!!!!!
    public ResponseEntity<RestaurantDTO> saveRestaurant(@RequestHeader(value = "userId")String userId,
                                                        @Valid @RequestBody RestaurantDTO restaurantDTO,
                                                        @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");
        return new ResponseEntity<>(restaurantService.createRestaurant(userId, restaurantDTO, token), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PutMapping("/restaurants/{restaurantId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable String restaurantId,
                                                          @Valid @RequestBody RestaurantDTO restaurantDTO,
                                                          @RequestHeader(value="userId") String userId,
                                                          @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");

        RestaurantDTO restaurantResponse = restaurantService.updateRestaurant(userId, restaurantId, restaurantDTO, token);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @DeleteMapping("/restaurants/{restaurantId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<String> deleteRestaurant(@PathVariable String restaurantId,
                                                   @RequestHeader(value="userId") String userId,
                                                   @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");
        restaurantService.deleteRestaurant(userId, restaurantId, token);
        return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/restaurants/{restaurantId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<RestaurantDTO> updateEnable(@PathVariable String restaurantId,
                                                      @RequestBody RestaurantDTO restaurantDTO,
                                                      @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");
        RestaurantDTO restaurantResponse = restaurantService.updateEnabled(restaurantDTO, restaurantId, token);

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }
}
