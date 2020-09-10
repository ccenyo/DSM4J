package Exeptions;

import Responses.Response;

import java.io.IOException;

public class DsmException extends RuntimeException {

    public DsmException(String errorMessage) {
        super(errorMessage);
    }

    public DsmException(String errorMessage, Object... args) {
        super(String.format(errorMessage, args));
    }

    public DsmException(String errorMessage, Throwable errCause) {
        super(errorMessage, errCause);
    }

    public DsmException(IOException e) {

    }

    public DsmException(Response.Error error) {
        super("MainErrorCode= "+error.getCode()+" Error= "+error.getErrors());
    }
}
