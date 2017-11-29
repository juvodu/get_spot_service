package com.juvodu.serverless.response;

/**
 * Response for create operations
 *
 * @author Juvodu
 */
public class CreateResponse extends CrudResponse {

    private String id;

    public CreateResponse(String id, String message){
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
