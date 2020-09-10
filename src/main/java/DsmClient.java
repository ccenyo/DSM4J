import Exeptions.DsmLoginException;
import Requests.DsmAuth;
import Requests.DsmLoginRequest;
import Responses.DsmLoginResponse;
import Responses.Response;

import java.util.Optional;

public class DsmClient {

    private final String SID;

    public DsmClient(String SID) {
        this.SID = SID;
    }

    public String getSID() {
        return SID;
    }

    public static DsmClient login(DsmAuth auth) {
        Optional.ofNullable(auth).orElseThrow(() -> new DsmLoginException("DsmAuth can't be null"));

        Response<DsmLoginResponse> response = new DsmLoginRequest(auth).call();

        Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("An error occurred while trying to connect"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }

        return new DsmClient(response.getData().getSid());
    }
}
