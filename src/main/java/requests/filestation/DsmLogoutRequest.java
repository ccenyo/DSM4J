package requests.filestation;

import com.fasterxml.jackson.core.type.TypeReference;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.filestation.DsmLogoutResponse;
import responses.Response;

public class DsmLogoutRequest extends DsmAbstractRequest<DsmLogoutResponse> {
    public DsmLogoutRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.API.Auth";
        this.version = 1;
        this.method = "logout";
        this.path = "webapi/auth.cgi";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmLogoutResponse>>() {};
    }
}
