package requests;

import responses.DsmLoginResponse;
import responses.DsmLogoutResponse;
import responses.Response;
import com.fasterxml.jackson.core.type.TypeReference;

public class DsmLogoutRequest extends DsmAbstractRequest<DsmLogoutResponse>  {
    public DsmLogoutRequest(DsmAuth auth) {
        super(auth);
    }

    @Override
    public String getAPIName() {
        return "SYNO.API.Auth";
    }

    @Override
    public Integer getVersion() {
        return 1;
    }

    @Override
    public String getPath() {
        return "/webapi/auth.cgi";
    }

    @Override
    public String getMethod() {
        return "logout";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmLogoutResponse>>() {};
    }
}
