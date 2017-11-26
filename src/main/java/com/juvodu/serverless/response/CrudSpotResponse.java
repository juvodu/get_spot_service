package com.juvodu.serverless.response;

/**
 * Response for crud operations on the spot model
 *
 * @author Juvodu
 */
public class CrudSpotResponse extends CrudResponse {

    private String id;

    public CrudSpotResponse(String id, String message){
        super(message);
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
