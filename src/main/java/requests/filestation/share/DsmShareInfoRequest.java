package requests.filestation.share;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmShareException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmResponseFields;

import java.util.Optional;

public class DsmShareInfoRequest extends DsmAbstractRequest<DsmResponseFields.SharingLink> {
    public DsmShareInfoRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Sharing";
        this.version = 1;
        this.method = "getinfo";
        this.path = "webapi/entry.cgi";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmResponseFields.SharingLink>>() {};
    }

    public Response<DsmResponseFields.SharingLink> getInfo(String id) {
        addParameter("id", Optional.ofNullable(id).orElseThrow(() -> new DsmShareException("You have to specify an id")));
        this.method = "getinfo";
        return call();
    }
}
