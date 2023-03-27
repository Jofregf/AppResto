package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDTO {

    private String bookingId;

    @NotNull(message = "You must enter the booking day")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @NotNull(message = "You must enter the booking time")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime bookingTime;

    @NotNull(message = "You must indicate the number of people")
    @Min(1)
    private int bookingPartySize;

    private boolean isActive;

    public BookingDTO() {
    }

    public BookingDTO(String bookingId, LocalDate bookingDate, LocalTime bookingTime,
                      int bookingPartySize, boolean isActive) {
        this.bookingId = bookingId;
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getBookingPartySize() {
        return bookingPartySize;
    }

    public void setBookingPartySize(int bookingPartySize) {
        this.bookingPartySize = bookingPartySize;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
