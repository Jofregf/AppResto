package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

public class MenuDTO {

    private String menuId;

    private String menuName;

    private String menuDescription;

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
