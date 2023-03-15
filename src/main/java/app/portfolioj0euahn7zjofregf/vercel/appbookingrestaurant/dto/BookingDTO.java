package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingDTO {

    private String bookingId;

    private LocalDate bookingDate;

    private LocalTime bookingTime;

    private int bookingPartySize;

    public BookingDTO() {
    }

    public BookingDTO(String bookingId, LocalDate bookingDate, LocalTime bookingTime, int bookingPartySize) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingPartySize = bookingPartySize;
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


}
