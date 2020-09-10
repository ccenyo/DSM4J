package Exeptions;

import Responses.Response;

public class DsmLoginException extends DsmException{
    public DsmLoginException(String errorMessage) {
        super(errorMessage);
    }

    public DsmLoginException(Response.Error error) {
        super(error);
    }

}
