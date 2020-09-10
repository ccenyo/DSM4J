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
        Response<DsmLoginResponse> dsmLoginResponse = new DsmLoginRequest(auth).call();

        Optional.ofNullable(dsmLoginResponse).orElseThrow(() -> new DsmLoginException("An error occured while trying to connect"));
        if(!dsmLoginResponse.isSuccess()) {
            throw  new DsmLoginException("");
        }

        return new DsmClient(dsmLoginResponse.getData().getSid());
    }
}
