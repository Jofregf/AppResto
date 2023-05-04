package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    public RestaurantResponse getRestaurants(int pageNumber, int pageSize, String sortBy, String sortDir);

    public RestaurantDTO getRestaurantById(String restaurantId);

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO, String token);

    public RestaurantDTO updateRestaurant(String restaurantId, RestaurantDTO restaurant, String token);

    public void deleteRestaurant(String restaurantId, String token);

    public RestaurantDTO updateEnabled(RestaurantDTO restaurantDTO, String restaurantId, String token);

    List<RestaurantDTO> findRestaurantsByMenuName(String menuName);

    public RestaurantDTO findRestaurantByBookingId(String bookingId, String token);

    List<RestaurantDTO> findRestaurantByAverageRating(Double averageRating);

    List<RestaurantDTO> findRestaurantByUserId(String token);
}
