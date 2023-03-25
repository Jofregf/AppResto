package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class BookingModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "booking_id", updatable = false, columnDefinition = "VARCHAR(50)")
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "userId", columnDefinition = "VARCHAR(50)")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "restaurantId", columnDefinition = "VARCHAR(50)")
    private RestaurantModel restaurant;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "booking_time")
    private LocalTime bookingTime;

    @Column(name = "party_size", nullable = false)
    private int bookingPartySize;

    @Column(name = "booking_active",  columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;

    public BookingModel() {
    }

    public BookingModel(RestaurantModel restaurant, LocalDate bookingDate,
                        LocalTime bookingTime, int bookingPartySize, boolean isActive) {
        this.restaurant = restaurant;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingPartySize = bookingPartySize;
        this.isActive = isActive;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getBookingPartySize() {
        return bookingPartySize;
    }

    public void setBookingPartySize(int bookingPartySize) {
        this.bookingPartySize = bookingPartySize;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
