package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.MenuModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuModel, String> {

    public List<MenuModel> findByRestaurant_RestaurantId(String restaurantId);
}
