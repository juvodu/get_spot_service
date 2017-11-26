package com.juvodu.serverless.response;

/**
 * Generic response for crud operations to provide feedback to the client
 *
 * @author Juvodu
 */
public class CrudResponse {

    public CrudResponse(String message){
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
