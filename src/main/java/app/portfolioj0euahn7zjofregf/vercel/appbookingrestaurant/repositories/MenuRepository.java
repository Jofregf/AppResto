package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.MenuModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuModel, String> {

    public List<MenuModel> findByRestaurant_RestaurantId(String restaurantId);

    public List<MenuModel> findByMenuNameContainingIgnoreCase(String menuName);
}
