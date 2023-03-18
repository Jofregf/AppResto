package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.MenuModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.MenuRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

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
    public MenuDTO createMenu(MenuDTO menuDTO, String restaurantId) {

        MenuModel menu = mapEntity(menuDTO);
        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        menu.setRestaurant(restaurant);

        MenuModel newMenu = menuRepository.save(menu);

        return mapDTO(newMenu);
    }

    @Override
    public MenuDTO updateMenu(String restaurantId, MenuDTO menuDTO, String menuId) {

        MenuModel menu = menuRepository
                .findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        menu.setMenuName(menuDTO.getMenuName());
        menu.setMenuDescription(menuDTO.getMenuDescription());
        menu.setMenuImage(menuDTO.getMenuImage());

        MenuModel updatedMenu = menuRepository.save(menu);

        return mapDTO(updatedMenu);
    }

    @Override
    public MenuDTO getMenuById(String menuId) {

        MenuModel menu = menuRepository
                .findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        return mapDTO(menu);
    }

    @Override
    public void deleteMenu(String restaurantId, String menuId) {
        RestaurantModel restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        MenuModel menu = menuRepository
                .findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        menuRepository.delete(menu);
    }

    @Override
    public List<MenuDTO> findMenuByRestaurantId(String restaurantId) {

        List<MenuModel> menus = menuRepository.findByRestaurant_RestaurantId(restaurantId);

        List<MenuDTO> listMenus = menus.stream().map(menu -> mapDTO(menu)).collect(Collectors.toList());

        if(listMenus.isEmpty()){
            throw new ResourceNotFoundException("Menu", "restaurantId", restaurantId);
        }

        return listMenus;
    }
}
