package ru.tsystems.tsproject.sbb.responses;

import java.util.List;

/**
 * Created by apple on 25.11.2014.
 */
public class ValidationResponse {
    private String status;
    private List<ErrorMessage> errorMessageList;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<ErrorMessage> getErrorMessageList() {
        return this.errorMessageList;
    }
    public void setErrorMessageList(List<ErrorMessage> errorMessageList) {
        this.errorMessageList = errorMessageList;
    }
}