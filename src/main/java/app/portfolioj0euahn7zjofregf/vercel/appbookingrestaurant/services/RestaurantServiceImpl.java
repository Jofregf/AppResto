package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RestaurantResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.BookingRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.ReviewRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RestaurantDTO mapDTO(RestaurantModel restaurantModel) {

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(restaurantModel.getRestaurantId());
        restaurantDTO.setRestaurantName(restaurantModel.getRestaurantName());
        restaurantDTO.setRestaurantAddress(restaurantModel.getRestaurantAddress());
        restaurantDTO.setRestaurantPhone(restaurantModel.getRestaurantPhone());
        restaurantDTO.setRestaurantEmail(restaurantModel.getRestaurantEmail());
        restaurantDTO.setRestaurantDescription(restaurantModel.getRestaurantDescription());
        restaurantDTO.setOpeningHoursRestaurant(restaurantModel.getOpeningHoursRestaurant());
        restaurantDTO.setClosingHoursRestaurant(restaurantModel.getClosingHoursRestaurant());
        restaurantDTO.setRestaurantImages(restaurantModel.getRestaurantImages());
        restaurantDTO.setRestaurantCapacity(restaurantModel.getRestaurantCapacity());
        restaurantDTO.setEnabled(restaurantModel.getEnabled());
        restaurantDTO.setAverageRating(restaurantModel.getAverageRating());
        restaurantDTO.setReviews(restaurantModel.getReviews());


        return restaurantDTO;
    }

    private RestaurantModel mapEntity( RestaurantDTO restaurantDTO){

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setRestaurantId(restaurantDTO.getRestaurantId());
        restaurant.setRestaurantName(restaurantDTO.getRestaurantName());
        restaurant.setRestaurantAddress(restaurantDTO.getRestaurantAddress());
        restaurant.setRestaurantPhone(restaurantDTO.getRestaurantPhone());
        restaurant.setRestaurantEmail(restaurantDTO.getRestaurantEmail());
        restaurant.setRestaurantDescription(restaurantDTO.getRestaurantDescription());
        restaurant.setOpeningHoursRestaurant(restaurantDTO.getOpeningHoursRestaurant());
        restaurant.setClosingHoursRestaurant(restaurantDTO.getClosingHoursRestaurant());
        restaurant.setRestaurantImages(restaurantDTO.getRestaurantImages());
        restaurant.setRestaurantCapacity(restaurantDTO.getRestaurantCapacity());
        restaurant.setEnabled(true);
        restaurant.setAverageRating(0.0);
        restaurant.setReviews(restaurantDTO.getReviews());

        return restaurant;
    }

    @Override
    public RestaurantResponse getRestaurants(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

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
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        RestaurantDTO restaurantDTO = mapDTO(restaurant);

        return restaurantDTO;
    }

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO, String token ) {

        RestaurantModel restaurant = mapEntity(restaurantDTO);

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if (idToken == null || !idToken.equals(user.getUserId())){
            throw new AccessDeniedException("Access denied, id does not match");
        }

        restaurant.setUser(user);

        RestaurantModel newRestaurant = restaurantRepository.save(restaurant);

        return mapDTO(newRestaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(String restaurantId, RestaurantDTO restaurantDTO,
                                          String token) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals(user.getUserId())){
            throw new AccessDeniedException("Access denied, id does not match");
        }

        if(!userId.equals(restaurant.getUser().getUserId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The user is not the owner of the restaurant");
        }

        restaurant.setRestaurantName(restaurantDTO.getRestaurantName());
        restaurant.setRestaurantAddress(restaurantDTO.getRestaurantAddress());
        restaurant.setRestaurantPhone(restaurantDTO.getRestaurantPhone());
        restaurant.setRestaurantEmail(restaurantDTO.getRestaurantEmail());
        restaurant.setRestaurantDescription(restaurantDTO.getRestaurantDescription());
        restaurant.setOpeningHoursRestaurant(restaurantDTO.getOpeningHoursRestaurant());
        restaurant.setClosingHoursRestaurant(restaurantDTO.getClosingHoursRestaurant());
        restaurant.setRestaurantImages(restaurantDTO.getRestaurantImages());
        restaurant.setRestaurantCapacity(restaurantDTO.getRestaurantCapacity());

        RestaurantModel restaurantUpdate = restaurantRepository.save(restaurant);
        return mapDTO(restaurantUpdate);
    }

    @Override
    public void deleteRestaurant(String restaurantId, String token) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals(user.getUserId())){
            throw new AccessDeniedException("Access denied, id does not match");
        }

        if(!userId.equals(restaurant.getUser().getUserId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The user is not the owner of the restaurant");
        }
        restaurantRepository.delete(restaurant);
    }

    @Override
    public RestaurantDTO updateEnabled(RestaurantDTO restaurantDTO, String restaurantId, String token) {

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        restaurant.setEnabled(restaurantDTO.isEnabled());

        RestaurantModel updateRestaurant = restaurantRepository.save(restaurant);

        return mapDTO(updateRestaurant);
    }

    @Override
    public List<RestaurantDTO> findRestaurantsByMenuName(String menuName) {

        List<RestaurantModel> restaurants = restaurantRepository.findByMenus_MenuNameContainingIgnoreCase(menuName);

        List<RestaurantDTO> listRestaurants = restaurants.stream().map(restaurant -> mapDTO(restaurant)).collect(Collectors.toList());

        if(listRestaurants.isEmpty()){
            throw new ResourceNotFoundException("Restaurant", "Menu name", menuName);
        }
        return listRestaurants;
    }

    @Override
    public RestaurantDTO findRestaurantByBookingId(String bookingId, String token) {

        String idToken = jwtTokenProvider.getUserIdFromToken(token);

        Optional<BookingModel> booking = bookingRepository.findById(bookingId);

        Optional<RestaurantModel> restaurant = Optional.ofNullable(restaurantRepository.findByBookings_BookingId(bookingId));

        if(!idToken.equals(booking.get().getUser().getUserId())) {
            throw new ResourceNotFoundException("Booking", "User Id", idToken);
        }

        if(!restaurant.isPresent()){
            throw new ResourceNotFoundException("Restaurant", "Booking Id", bookingId);
        }

        return mapDTO(restaurant.get());
    }

    @Override
    public List<RestaurantDTO> findRestaurantByAverageRating(Double averageRating) {

        List<RestaurantModel> restaurants = restaurantRepository.findByAverageRating(averageRating);
        List<RestaurantDTO> listRestaurants = restaurants.stream().map(restaurant -> mapDTO(restaurant)).collect(Collectors.toList());
        if(listRestaurants.isEmpty()){
            throw new ResourceNotFoundException("Restaurant", "Average rating", averageRating.toString());
        }
        return listRestaurants;
    }

    @Override
    public List<RestaurantDTO> findRestaurantByUserId(String token) {

        String userId = jwtTokenProvider.getUserIdFromToken(token);

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        List<RestaurantModel> restaurants = restaurantRepository.findByUser_UserId(userId);

        List<RestaurantDTO> listRestaurants = restaurants.stream().map(restaurant -> mapDTO(restaurant)).collect(Collectors.toList());

        if(listRestaurants.isEmpty()){
            throw new ResourceNotFoundException("Restaurant", "User", userId);
        }
        return listRestaurants;
    }
}
