package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import java.util.Date;

public class ErrorDetails {

    private Date timeMark;
    private String message;
    private String details;

    public ErrorDetails() {
    }

    public ErrorDetails(Date timeMark, String message, String details) {
        this.timeMark = timeMark;
        this.message = message;
        this.details = details;
    }

    public Date getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(Date timeMark) {
        this.timeMark = timeMark;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
