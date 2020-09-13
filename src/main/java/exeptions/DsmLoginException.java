package exeptions;

import responses.Response;

public class DsmLoginException extends DsmException{
    public DsmLoginException(String errorMessage) {
        super(errorMessage);
    }

    public DsmLoginException(Exception e) {
        super(e);
    }

    public DsmLoginException(Response.Error error) {
        super(error);
    }

}
