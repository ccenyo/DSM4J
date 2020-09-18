package requests.filestation;

import responses.Response;

public interface DsmNonBlockingProcess<T> {
        Response<T> start();
        Response<T> status();
        Response<T> stop();
}
