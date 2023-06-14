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
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.utilities.EmailSender;
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
    private JwtTokenProvider  jwtTokenProvider;

    private EmailSender emailSender;

    @Autowired
    public RestaurantServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

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

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

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
        String text = "Estimado " + user.getUserName() + ":<br><br>" +
                "¡Felicitaciones por registrar el restaurante " +
                "<span style=\"color: #F15422; font-size: 25px;\">" + restaurant.getRestaurantName()  +
                "</span> en nuestra plataforma!<br><br>" +
                "En el panel correspondiente, encontrará todas las herramientas necesarias para gestionarlo y " +
                "promocionarlo. " +
                "Estamos seguros de que su restaurante será un éxito y estamos aquí para apoyarlo en cada paso " +
                "del camino.<br><br>" +
                "Si necesita ayuda adicional o tiene alguna pregunta, no dude en ponerse en contacto con nuestro " +
                "equipo de soporte. " +
                "Estaremos encantados de brindarle asistencia personalizada.<br><br>" +
                "Atentamente,<br>" +
                "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";

        emailSender.sendEmail(user.getUserEmail(), "Restaurante creado", text);

        return mapDTO(newRestaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(String restaurantId, RestaurantDTO restaurantDTO,
                                          String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

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

        String message = "Estimado " + restaurant.getUser().getUserName() + ":<br><br>"
                + "Queremos informarle que se han realizado modificaciones en los datos de su restaurante "
                + "<span style=\"color: #F15422; font-size: 25px;\">"
                + restaurant.getRestaurantName() + "</span>" + ". Le recomendamos revisar y verificar la precisión "
                + "de los nuevos datos. "
                + "Si encuentra alguna discrepancia o tiene alguna pregunta, no dude en ponerse en contacto con nuestro "
                + "equipo de soporte.<br><br>"
                + "Saludos cordiales,<br>"
                + "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";

        emailSender.sendEmail(restaurant.getUser().getUserEmail(), "Datos de restaurante modificados", message);
        return mapDTO(restaurantUpdate);
    }

    @Override
    public void deleteRestaurant(String restaurantId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

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

        String message = "Estimado " + restaurant.getUser().getUserName() + ":<br><br>"
                + "Lamentamos que haya decidido eliminar su restaurante "
                + "<span style=\"color: #F15422; font-size: 25px;\">"
                + restaurant.getRestaurantName() + "</span>" + " de nuestra base de datos. " +
                "Esperamos tenerlo de vuelta pronto.<br><br> "
                + "Saludos cordiales,<br>"
                + "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";
        emailSender.sendEmail(restaurant.getUser().getUserEmail(), "Restaurante eliminado", message);

        restaurantRepository.delete(restaurant);
    }

    @Override
    public RestaurantDTO updateEnabled(RestaurantDTO restaurantDTO, String restaurantId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        restaurant.setEnabled(restaurantDTO.isEnabled());

        RestaurantModel updateRestaurant = restaurantRepository.save(restaurant);

        if (!restaurant.getEnabled()) {
            String message = "Estimado " + restaurant.getUser().getUserName() + ":<br><br>"
                    + "Lamentamos informarle que se ha bloqueado la visibilidad de su restaurante "
                    + "<span style=\"color: #F15422; font-size: 25px;\">"
                    + restaurant.getRestaurantName() + "</span>" + " en nuestra plataforma debido a un incumplimiento de las " +
                    "reglas de convivencia. Si considera que ha habido un error o desea que reevaluemos su situación, " +
                    "le invitamos a responder a este correo electrónico y proporcionar una explicación detallada de su situación.<br><br>"
                    + "Nos comprometemos a revisar cuidadosamente su caso y tomar las acciones apropiadas en base a la " +
                    "información que nos proporcione. Agradecemos su cooperación y comprensión.<br><br>"
                    + "Saludos cordiales,<br>"
                    + "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";
            emailSender.sendEmail(restaurant.getUser().getUserEmail(), "Bloqueo de visibilidad del restaurante", message);
        } else {
            String message = "Estimado " + restaurant.getUser().getUserName() + ":<br><br>"
                    + "Luego de revisar su situación, hemos tomado la decisión de levantar la suspensión de su restaurante "
                    + "<span style=\"color: #F15422; font-size: 25px;\">"
                    + restaurant.getRestaurantName() + "</span>" + " y restaurar su visibilidad en nuestra plataforma.<br><br>"
                    + "Agradecemos su cooperación y comprensión durante este proceso. Si tiene alguna pregunta adicional o " +
                    "necesita más información, no dude en contactarnos.<br><br>"
                    + "Saludos cordiales,<br>"
                    + "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";
            emailSender.sendEmail(restaurant.getUser().getUserEmail(), "Restaurante restaurado con visibilidad completa", message);
        }

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

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

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

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

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
