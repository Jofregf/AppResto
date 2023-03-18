package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;

import java.util.List;

public interface MenuService {

    public MenuDTO createMenu(MenuDTO menuDTO, String restaurantId);

    public MenuDTO updateMenu(String restaurantId, MenuDTO menuDTO, String menuId);

    public MenuDTO getMenuById(String menuId);

    public void deleteMenu(String restaurantId, String menuId);

    public List<MenuDTO> findMenuByRestaurantId(String restaurantId);

}
