package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.MenuDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PostMapping("/menus/restaurant/{restaurantId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<MenuDTO> saveMenu(@PathVariable String restaurantId,
                                            @Valid @RequestBody MenuDTO menuDTO,
                                            @RequestHeader(value="Authorization") String authorizHeader) {

        String token = authorizHeader.replace("Bearer ", "");
        return new ResponseEntity<>(menuService.createMenu(menuDTO, restaurantId, token), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')")
    @PutMapping("/menus/{menuId}/restaurant/{restaurantId}")//TODO: YA LISTO!!!!!
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable String restaurantId,
                                              @PathVariable String menuId,
                                              @Valid @RequestBody MenuDTO menuDTO,
                                              @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");
        MenuDTO menuResponse = menuService.updateMenu(restaurantId, menuDTO, menuId, token);

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_RESTO')") //TODO: YA LISTO!!!!!
    @DeleteMapping("/menus/{menuId}/restaurant/{restaurantId}")
    public ResponseEntity<String> deleteMenu(@PathVariable String restaurantId, @PathVariable String menuId,
                                             @RequestHeader(value="Authorization") String authorizHeader){

        String token = authorizHeader.replace("Bearer ", "");
        menuService.deleteMenu(restaurantId, menuId, token);

        return new ResponseEntity<>("Menu deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/menus/restaurant/{restaurantId}") //TODO: YA LISTO!!!!!
    public List<MenuDTO> listMenuByRestaurantId(@PathVariable String restaurantId){

        return menuService.findMenuByRestaurantId(restaurantId);
    }

    @GetMapping("/menus") //TODO: YA LISTO!!!!!
    public List<MenuDTO> listMenuByName(@RequestHeader(value="menuName") String menuName){

        return menuService.getMenuByName(menuName);
    }

    @GetMapping("/menus/{menuId}") //TODO: YA LISTO!!!!!
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable String menuId){

        return ResponseEntity.ok(menuService.getMenuById(menuId));
    }
}
