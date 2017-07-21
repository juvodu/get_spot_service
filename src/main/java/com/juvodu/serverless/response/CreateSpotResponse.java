package com.juvodu.serverless.response;

/**
 * Response model for spot creation
 *
 * @author Juvodu
 */
public class CreateSpotResponse {

    private String id;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
