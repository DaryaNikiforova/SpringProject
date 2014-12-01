package ru.tsystems.tsproject.sbb.responses;

/**
 * Created by apple on 25.11.2014.
 */
public class ErrorMessage {
    private String fieldName;
    private String message;

    public ErrorMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
    public String getFieldName() {
        return fieldName;
    }
    public String getMessage() {
        return message;
    }
}
