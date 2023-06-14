package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.MenuModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.MenuRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MenuDTO mapDTO(MenuModel menuModel) {

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setMenuId(menuModel.getMenuId());
        menuDTO.setMenuName(menuModel.getMenuName());
        menuDTO.setMenuDescription(menuModel.getMenuDescription());
        menuDTO.setMenuImage(menuModel.getMenuImage());

        return menuDTO;
    }

    private MenuModel mapEntity(MenuDTO menuDTO) {

        MenuModel menu = new MenuModel();
        menu.setMenuId(menuDTO.getMenuId());
        menu.setMenuName(menuDTO.getMenuName());
        menu.setMenuDescription(menuDTO.getMenuDescription());
        menu.setMenuImage(menuDTO.getMenuImage());


        return menu;
    }

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO, String restaurantId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        MenuModel menu = mapEntity(menuDTO);

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !restaurant.getUser().getUserId().equals(idToken)){
            throw new AccessDeniedException("Access denied, id does not match");
        }

        menu.setRestaurant(restaurant);

        MenuModel newMenu = menuRepository.save(menu);

        return mapDTO(newMenu);
    }

    @Override
    public MenuDTO updateMenu(String restaurantId, MenuDTO menuDTO, String menuId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        MenuModel menu = menuRepository
                .findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "Id", menuId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        if(!restaurantId.equals(menu.getRestaurant().getRestaurantId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The menu does not correspond to the restaurant");
        }

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals(menu.getRestaurant().getUser().getUserId())){
            throw new AccessDeniedException("Access denied, id does not match");
        }
        menu.setMenuName(menuDTO.getMenuName());
        menu.setMenuDescription(menuDTO.getMenuDescription());
        menu.setMenuImage(menuDTO.getMenuImage());

        MenuModel updatedMenu = menuRepository.save(menu);

        return mapDTO(updatedMenu);
    }

    @Override
    public void deleteMenu(String restaurantId, String menuId, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Id", restaurantId));

        MenuModel menu = menuRepository
                .findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "Id", menuId));

        if(!restaurantId.equals(menu.getRestaurant().getRestaurantId())){
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "The menu does not correspond to the restaurant");
        }

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals(menu.getRestaurant().getUser().getUserId())){
            throw new AccessDeniedException("Access denied, id does not match");
        }

        menuRepository.delete(menu);
    }

    @Override
    public List<MenuDTO> findMenuByRestaurantId(String restaurantId) {

        List<MenuModel> menus = menuRepository.findByRestaurant_RestaurantId(restaurantId);

        List<MenuDTO> listMenus = menus.stream().map(menu -> mapDTO(menu)).collect(Collectors.toList());

        if(listMenus.isEmpty()){
            throw new ResourceNotFoundException("Menu", "Restaurant Id", restaurantId);
        }

        return listMenus;
    }

    @Override
    public List<MenuDTO> getMenuByName(String menuName, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_RESTO")){
            throw new AccessDeniedException("access denied, role not allowed");
        }

        String idToken = jwtTokenProvider.getUserIdFromToken(token);

        List<MenuModel> menus = menuRepository.findByMenuNameContainingIgnoreCaseAndRestaurant_User_UserId(menuName, idToken);

        for(MenuModel menu:menus){
            if(!menu.getRestaurant().getUser().getUserId().equals(idToken)){
                System.out.println(menu.getRestaurant().getUser().getUserId());
                System.out.println(idToken);
                throw new AccessDeniedException("access denied");
            }
        }

        List<MenuDTO> listMenus = menus.stream().map(menu -> mapDTO(menu)).collect(Collectors.toList());

        if(listMenus.isEmpty()){
            throw new ResourceNotFoundException("Menu", "name", menuName);
        }

        return listMenus;
    }
}
