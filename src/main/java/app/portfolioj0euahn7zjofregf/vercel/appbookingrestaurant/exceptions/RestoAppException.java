package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions;

import org.springframework.http.HttpStatus;

public class RestoAppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus state;

    private String message;

    public RestoAppException(HttpStatus state, String message) {
        super();
        this.state = state;
        this.message = message;
    }

    public RestoAppException(HttpStatus state, String message, String anotherMessage) {
        super();
        this.state = state;
        this.message = message;
        this.message = anotherMessage;
    }

    public HttpStatus getState() {
        return state;
    }

    public void setState(HttpStatus state) {
        this.state = state;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}