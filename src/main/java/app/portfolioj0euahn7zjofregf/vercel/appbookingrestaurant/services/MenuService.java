package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;

import java.util.List;

public interface MenuService {

    public MenuDTO createMenu(MenuDTO menuDTO, String restaurantId, String token);

    public MenuDTO updateMenu(String restaurantId, MenuDTO menuDTO, String menuId, String token);

    public void deleteMenu(String restaurantId, String menuId, String token);

    public List<MenuDTO> findMenuByRestaurantId(String restaurantId);

    public List<MenuDTO> getMenuByName(String menuName, String Token);

}
