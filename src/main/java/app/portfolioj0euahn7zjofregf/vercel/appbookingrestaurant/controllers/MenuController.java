package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/restaurant/{restaurantId}/menu")
    public ResponseEntity<MenuDTO> saveMenu(@PathVariable String restaurantId, @RequestBody MenuDTO menuDTO) {

        return new ResponseEntity<>(menuService.createMenu(menuDTO, restaurantId), HttpStatus.CREATED);
    }

    @PutMapping("/restaurant/{restaurantId}/menu/{menuId}")
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable String restaurantId,
                                              @PathVariable String menuId,
                                              @RequestBody MenuDTO menuDTO){

        MenuDTO menuResponse = menuService.updateMenu(restaurantId, menuDTO, menuId);

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }

    @GetMapping("/restaurant/menu/{menuId}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable String menuId){

        return ResponseEntity.ok(menuService.getMenuById(menuId));
    }

    @DeleteMapping("/restaurant/{restaurantId}/menu/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable String restaurantId, @PathVariable String menuId){
        menuService.deleteMenu(restaurantId, menuId);
        return new ResponseEntity<>("Menu deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/menu")
    public List<MenuDTO> listMenuByRestaurantId(@PathVariable String restaurantId){
        return menuService.findMenuByRestaurantId(restaurantId);
    }

    @GetMapping("/menu/{menuName}")
    public List<MenuDTO> listMenuByName(@PathVariable String menuName){
        return menuService.getMenuByName(menuName);
    }
}
