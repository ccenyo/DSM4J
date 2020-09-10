package Responses;

import java.util.List;

public class Response<T>  {

    private boolean success;

    private T data;

    private Error error;

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    private static class Error {
        private String code;
        private List<ErrorDetail> errors;

        private static class ErrorDetail {
            String code;
            String path;

            public String getCode() {
                return code;
            }

            public String getPath() {
                return path;
            }
        }
    }
}
