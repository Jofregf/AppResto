package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="restaurants")
public class RestaurantModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "restaurantId", updatable = false, columnDefinition = "VARCHAR(50)")
    private String restaurantId;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String restaurantName;

    @Column(name = "address", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String restaurantAddress;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(50)")
    private String restaurantPhone;

    @Column(name = "description", nullable = false)
    private String restaurantDescription;

    @Column(name = "open", nullable = false)
    private LocalTime openingHoursRestaurant;

    @Column(name = "close", nullable = false)
    private LocalTime closingHoursRestaurant;

    @Column(name = "images")
    private String[] restaurantImages;

    @Column(name = "capacity", nullable = false)
    private int restaurantCapacity;

    @Column(name = "restaurant_enabled", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean enabled;

    @Column(name = "average_rating")
    private Double averageRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewModel> reviews = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<MenuModel> menus = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private Set<BookingModel> bookings = new HashSet<>();

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "userId")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private UserModel user;



    public RestaurantModel() {
    }

    public RestaurantModel(String restaurantId, String restaurantName, String restaurantAddress, String restaurantPhone,
                           String restaurantDescription, LocalTime openingHoursRestaurant,
                           LocalTime closingHoursRestaurant, String[] restaurantImages,
                           int restaurantCapacity, Boolean enabled, Double averageRating, UserModel user) {
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
        this.user = user;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Double getAverageRanting() {
        return averageRating;
    }

    public void setAverageRanting(Double averageRanting) {
        this.averageRating = averageRanting;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Set<BookingModel> getBookings() {
        return bookings;
    }

    public void setBookings(Set<BookingModel> bookings) {
        this.bookings = bookings;
    }
}