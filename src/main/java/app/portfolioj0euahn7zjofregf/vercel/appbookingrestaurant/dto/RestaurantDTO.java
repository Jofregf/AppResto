package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Arrays;

public class RestaurantDTO {

    private String restaurantId;

    @NotEmpty
    @Size(min = 3, message = "The restaurant's name must have at least 3 characters")
    private String restaurantName;

    @NotEmpty(message = "You must enter the address of the restaurant")
    private String restaurantAddress;

    @NotEmpty(message = "You must enter the number phone of the restaurant")
    private String restaurantPhone;

    @NotEmpty(message = "The email cannot be empty.")
    @Email
    private String restaurantEmail;

    @NotEmpty
    @Size(min = 10, message = "The restaurant's description must have at least 10 characters")
    private String restaurantDescription;

    @NotNull(message = "You must enter the opening hours of the restaurant")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime openingHoursRestaurant;

    @NotNull(message = "You must enter the closing time of the restaurant")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime closingHoursRestaurant;

    @NotEmpty(message = "You must add at least one photo link")
    private String[] restaurantImages;

    @NotNull(message = "You must indicate the capacity of the restaurant")
    @Min(3)
    private int restaurantCapacity;

    @NotNull
    private boolean enabled;

    private Double averageRating;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String restaurantId, String restaurantName, String restaurantAddress,
                         String restaurantPhone, String restaurantEmail, String restaurantDescription,
                         LocalTime openingHoursRestaurant, LocalTime closingHoursRestaurant,
                         String[] restaurantImages, int restaurantCapacity, boolean enabled, Double averageRating) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhone = restaurantPhone;
        this.restaurantEmail = restaurantEmail;
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

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
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
