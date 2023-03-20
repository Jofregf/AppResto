package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    public RestaurantDTO createRestaurant(String userId, RestaurantDTO restaurantDTO);

    public RestaurantResponse getRestaurants(int pageNumber, int pageSize, String sortBy, String sortDir);

    public RestaurantDTO getRestaurantById(String restaurantId);

    public RestaurantDTO updateRestaurant(String userId, String restaurantId, RestaurantDTO restaurant);

    public void deleteRestaurant(String userId, String restaurantId);

    public RestaurantDTO updateEnabled(RestaurantDTO restaurantDTO, String restaurantId);

    List<RestaurantDTO> findRestaurantsByMenuName(String menuName);

    public RestaurantDTO findRestaurantByBookingId(String bookingId);

}
