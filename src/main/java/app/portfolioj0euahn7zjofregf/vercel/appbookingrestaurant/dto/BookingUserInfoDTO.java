package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingUserInfoDTO {

    private String bookingId;

    private LocalDate bookingDate;

    private LocalTime bookingTime;

    private int bookingPartySize;

    private boolean isActive;

    private RestaurantUserInfoDTO restaurant;

    public BookingUserInfoDTO() {
    }

    public BookingUserInfoDTO(String bookingId, LocalDate bookingDate, LocalTime bookingTime,
                              int bookingPartySize, boolean isActive, RestaurantUserInfoDTO restaurant) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingPartySize = bookingPartySize;
        this.isActive = isActive;
        this.restaurant = restaurant;
    }

    public String getBookingId() {
        return bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public int getBookingPartySize() {
        return bookingPartySize;
    }

    public boolean isActive() {
        return isActive;
    }

    public RestaurantUserInfoDTO getRestaurant() {
        return restaurant;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setBookingPartySize(int bookingPartySize) {
        this.bookingPartySize = bookingPartySize;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRestaurant(RestaurantUserInfoDTO restaurant) {
        this.restaurant = restaurant;
    }

}
