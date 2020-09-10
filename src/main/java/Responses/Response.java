package Responses;

import java.util.ArrayList;
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

    public static class Error {
        private String code;
        private List<ErrorDetail> errors = new ArrayList<>();

        public String getCode() {
            return code;
        }

        public List<ErrorDetail> getErrors() {
            return errors;
        }

        public static class ErrorDetail {
            String code;
            String path;

            public String getCode() {
                return code;
            }

            public String getPath() {
                return path;
            }

            @Override
            public String toString() {
                return "ErrorDetail{" +
                        "code='" + code + '\'' +
                        ", path='" + path + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code='" + code + '\'' +
                    ", errors=" + errors +
                    '}';
        }

    }
}
