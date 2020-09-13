package requests;

import responses.DsmLoginResponse;
import responses.Response;
import com.fasterxml.jackson.core.type.TypeReference;

public class DsmLoginRequest extends DsmAbstractRequest<DsmLoginResponse> {
    public DsmLoginRequest(DsmAuth auth) {
        super(auth);
        addParameter("account", auth.getUserName());
        addParameter("passwd", auth.getPassword());
    }

    public String getAPIName() {
        return "SYNO.API.Auth";
    }

    public Integer getVersion() {
        return 3;
    }

    public String getPath() {
        return "/webapi/auth.cgi";
    }

    public String getMethod() {
        return "login";
    }


    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmLoginResponse>>() {};
    }
}
