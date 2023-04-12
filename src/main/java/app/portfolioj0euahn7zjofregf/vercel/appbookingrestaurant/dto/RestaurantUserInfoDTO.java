package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

public class RestaurantUserInfoDTO {

    private String restaurantName;

    private String restaurantAddress;

    private String restaurantPhone;

    private String restaurantEmail;

    public RestaurantUserInfoDTO() {
    }

    public RestaurantUserInfoDTO(String restaurantName, String restaurantAddress, String restaurantPhone,
                                 String restaurantEmail) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhone = restaurantPhone;
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }
}
