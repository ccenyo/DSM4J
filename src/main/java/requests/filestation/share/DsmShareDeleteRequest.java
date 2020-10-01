package requests.filestation.share;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmShareException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmSimpleResponse;

import java.util.Optional;

public class DsmShareDeleteRequest extends DsmAbstractRequest<DsmSimpleResponse> {
    public DsmShareDeleteRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Sharing";
        this.version = 1;
        this.path = "webapi/entry.cgi";
    }

    public Response<DsmSimpleResponse> delete(String id) {
        addParameter("id", Optional.ofNullable(id).orElseThrow(() -> new DsmShareException("You have to provide an id")));
        this.method = "delete";
        return call();
    }

    public Response<DsmSimpleResponse> clearInvalidLinks() {
        this.method = "clear_invalid";
        return call();
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmSimpleResponse>>() {};
    }
}
