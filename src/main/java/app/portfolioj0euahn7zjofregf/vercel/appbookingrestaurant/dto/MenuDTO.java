package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MenuDTO {

    private String menuId;

    @NotEmpty
    @Size(min = 3, message = "The menu's name must have at least 3 characters")
    private String menuName;

    @NotEmpty
    @Size(min = 10, message = "The menu description must have at least 10 characters")
    private String menuDescription;

    @NotEmpty(message = "You must add at least one photo link")
    private String menuImage;

    public MenuDTO() {
    }

    public MenuDTO(String menuId, String menuName, String menuDescription, String menuImage) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.menuImage = menuImage;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }
}
