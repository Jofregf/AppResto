package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.ReviewRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private RestaurantDTO mapDTO(RestaurantModel restaurantModel) {

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(restaurantModel.getRestaurantId());
        restaurantDTO.setRestaurantName(restaurantModel.getRestaurantName());
        restaurantDTO.setRestaurantAddress(restaurantModel.getRestaurantAddress());
        restaurantDTO.setRestaurantPhone(restaurantModel.getRestaurantPhone());
        restaurantDTO.setRestaurantDescription(restaurantModel.getRestaurantDescription());
        restaurantDTO.setOpeningHoursRestaurant(restaurantModel.getOpeningHoursRestaurant());
        restaurantDTO.setClosingHoursRestaurant(restaurantModel.getClosingHoursRestaurant());
        restaurantDTO.setRestaurantImages(restaurantModel.getRestaurantImages());
        restaurantDTO.setRestaurantCapacity(restaurantModel.getRestaurantCapacity());
        restaurantDTO.setEnabled(restaurantModel.getEnabled());
        restaurantDTO.setAverageRanting(restaurantModel.getAverageRanting());
        return restaurantDTO;
    }

    private RestaurantModel mapEntity( RestaurantDTO restaurantDTO){

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setRestaurantId(restaurantDTO.getRestaurantId());
        restaurant.setRestaurantName(restaurantDTO.getRestaurantName());
        restaurant.setRestaurantAddress(restaurantDTO.getRestaurantAddress());
        restaurant.setRestaurantPhone(restaurantDTO.getRestaurantPhone());
        restaurant.setRestaurantDescription(restaurantDTO.getRestaurantDescription());
        restaurant.setOpeningHoursRestaurant(restaurantDTO.getOpeningHoursRestaurant());
        restaurant.setClosingHoursRestaurant(restaurantDTO.getClosingHoursRestaurant());
        restaurant.setRestaurantImages(restaurantDTO.getRestaurantImages());
        restaurant.setRestaurantCapacity(restaurantDTO.getRestaurantCapacity());
        restaurant.setEnabled(true);
        restaurant.setAverageRanting(0.0);

        return restaurant;
    }

    @Override
    public RestaurantDTO createRestaurant(String userId, RestaurantDTO restaurantDTO) {

        RestaurantModel restaurant = mapEntity(restaurantDTO);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        restaurant.setUser(user);

        RestaurantModel newRestaurant = restaurantRepository.save(restaurant);

        return mapDTO(newRestaurant);
    }

    @Override
    public RestaurantResponse getRestaurants(int pageNumber, int pageSize, String orderBy, String sortDir) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("restaurantName"));

        Page<RestaurantModel> restaurants = restaurantRepository.findAll(pageable);

        List<RestaurantModel> restaurantsList = restaurants.getContent();

        List<RestaurantDTO> content = restaurantsList.stream()
                .map(restaurant -> mapDTO(restaurant)).collect(Collectors.toList());

        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setContents(content);
        restaurantResponse.setPageNumber(restaurants.getNumber());
        restaurantResponse.setPageSize(restaurants.getSize());
        restaurantResponse.setTotalElements(restaurants.getTotalElements());
        restaurantResponse.setTotalPages(restaurants.getTotalPages());
        restaurantResponse.setLast(restaurants.isLast());

        return restaurantResponse;
    }

    @Override
    public RestaurantDTO getRestaurantById(String restaurantId) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        RestaurantDTO restaurantDTO = mapDTO(restaurant);

        return restaurantDTO;
    }

    @Override
    public RestaurantDTO updateRestaurant(String userId, String restaurantId, RestaurantDTO restaurantDTO) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if(userId.equals(restaurant.getUser().getUserId())){
            restaurant.setRestaurantName(restaurantDTO.getRestaurantName());
            restaurant.setRestaurantAddress(restaurantDTO.getRestaurantAddress());
            restaurant.setRestaurantPhone(restaurantDTO.getRestaurantPhone());
            restaurant.setRestaurantDescription(restaurantDTO.getRestaurantDescription());
            restaurant.setOpeningHoursRestaurant(restaurantDTO.getOpeningHoursRestaurant());
            restaurant.setClosingHoursRestaurant(restaurantDTO.getClosingHoursRestaurant());
            restaurant.setRestaurantImages(restaurantDTO.getRestaurantImages());
            restaurant.setRestaurantCapacity(restaurantDTO.getRestaurantCapacity());
        }

        RestaurantModel restaurantUpdate = restaurantRepository.save(restaurant);
        return mapDTO(restaurantUpdate);
    }

    @Override
    public void deleteRestaurant(String userId, String restaurantId) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if(userId.equals(restaurant.getUser().getUserId())){
            restaurantRepository.delete(restaurant);
        }
    }

    @Override
    public RestaurantDTO updateEnabled(RestaurantDTO restaurantDTO, String restaurantId) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        restaurant.setEnabled(restaurantDTO.isEnabled());

        RestaurantModel updateRestaurant = restaurantRepository.save(restaurant);

        return mapDTO(updateRestaurant);
    }

    @Override
    public List<RestaurantDTO> findRestaurantsByMenuName(String menuName) {

        List<RestaurantModel> restaurants = restaurantRepository.findByMenus_MenuName(menuName);

        List<RestaurantDTO> listRestaurants = restaurants.stream().map(restaurant -> mapDTO(restaurant)).collect(Collectors.toList());

        if(listRestaurants.isEmpty()){
            throw new ResourceNotFoundException("Restaurant", "menuName", menuName);
        }
        return listRestaurants;
    }

    @Override
    public RestaurantDTO findRestaurantByBookingId(String bookingId) {

        Optional<RestaurantModel> restaurant = Optional.ofNullable(restaurantRepository.findByBookings_BookingId(bookingId));

        if(restaurant.isPresent()){
            return mapDTO(restaurant.get());
        } else {
            throw new ResourceNotFoundException("Restaurant", "bookingId", bookingId);
        }
    }
}
