package com.juvodu.serverless.response;

/**
 * Response for crud operations on the spot model
 *
 * @author Juvodu
 */
public class CrudSpotResponse {

    private String id;

    private String message;

    public CrudSpotResponse(String id, String message){

        this.id = id;
        this.message = message;
    }

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
