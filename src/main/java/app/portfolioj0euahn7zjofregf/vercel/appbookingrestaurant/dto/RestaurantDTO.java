package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.Arrays;

public class RestaurantDTO {

    private String restaurantId;

    @NotEmpty
    @Size(min = 3, message = "The restaurant's name must have at least 2 characters")
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;
    private String restaurantDescription;
    private LocalTime openingHoursRestaurant;
    private LocalTime closingHoursRestaurant;
    private String[] restaurantImages;
    private int restaurantCapacity;
    private boolean enabled;
    private Double averageRating;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String restaurantId, String restaurantName, String restaurantAddress,
                         String restaurantPhone, String restaurantDescription,
                         LocalTime openingHoursRestaurant, LocalTime closingHoursRestaurant,
                         String[] restaurantImages, int restaurantCapacity, boolean enabled, Double averageRating) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhone = restaurantPhone;
        this.restaurantDescription = restaurantDescription;
        this.openingHoursRestaurant = openingHoursRestaurant;
        this.closingHoursRestaurant = closingHoursRestaurant;
        this.restaurantImages = restaurantImages;
        this.restaurantCapacity = restaurantCapacity;
        this.enabled = enabled;
        this.averageRating = averageRating;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
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

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public LocalTime getOpeningHoursRestaurant() {
        return openingHoursRestaurant;
    }

    public void setOpeningHoursRestaurant(LocalTime openingHoursRestaurant) {
        this.openingHoursRestaurant = openingHoursRestaurant;
    }

    public LocalTime getClosingHoursRestaurant() {
        return closingHoursRestaurant;
    }

    public void setClosingHoursRestaurant(LocalTime closingHoursRestaurant) {
        this.closingHoursRestaurant = closingHoursRestaurant;
    }

    public String[] getRestaurantImages() {
        return restaurantImages;
    }

    public void setRestaurantImages(String[] restaurantImages) {
        this.restaurantImages = restaurantImages;
    }

    public int getRestaurantCapacity() {
        return restaurantCapacity;
    }

    public void setRestaurantCapacity(int restaurantCapacity) {
        this.restaurantCapacity = restaurantCapacity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Double getAverageRanting() {
        return averageRating;
    }

    public void setAverageRanting(Double averageRanting) {
        this.averageRating = averageRanting;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", restaurantPhone='" + restaurantPhone + '\'' +
                ", restaurantDescription='" + restaurantDescription + '\'' +
                ", openingHoursRestaurant=" + openingHoursRestaurant +
                ", closingHoursRestaurant=" + closingHoursRestaurant +
                ", restaurantImages=" + Arrays.toString(restaurantImages) +
                ", restaurantCapacity=" + restaurantCapacity +
                ", enabled=" + enabled +
                ", averageRanting=" + averageRating +
                '}';
    }
}
