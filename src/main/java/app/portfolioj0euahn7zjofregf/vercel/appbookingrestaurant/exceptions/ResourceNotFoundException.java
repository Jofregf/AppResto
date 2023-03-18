package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    private String resourceName;
    private String fieldName;
    private String fieldValue;

    private LocalDate fieldDate;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s no encontrado con: %s : '%s'", resourceName, fieldName, fieldValue ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, LocalDate fieldDate){
        super(String.format("%s no encontrado con: %s : '%s'", resourceName, fieldName, fieldDate ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldDate = fieldDate;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public LocalDate getFieldDate() {
        return fieldDate;
    }

    public void setFieldDate(LocalDate fieldDate) {
        this.fieldDate = fieldDate;
    }
}