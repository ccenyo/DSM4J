package requests;

import com.fasterxml.jackson.core.type.TypeReference;
import responses.DsmLogoutResponse;
import responses.Response;

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
