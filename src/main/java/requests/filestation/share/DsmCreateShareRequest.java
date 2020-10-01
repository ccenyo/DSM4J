package requests.filestation.share;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmCreateFolderException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.share.DsmCreateShareResponse;
import utils.DateUtils;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmCreateShareRequest extends DsmAbstractRequest<DsmCreateShareResponse> {
    public DsmCreateShareRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Sharing";
        this.version = 1;
        this.method = "create";
        this.path = "webapi/entry.cgi";
    }

    /**
     * One or more file/folder paths with which to
     * generate sharing links, separated by
     * commas
     */
    private List<String> paths = new LinkedList<>();
    /**
     * Optional The password for the sharing link
     * when accessing it. The max password
     * length are 16 characters.
     */
    private String password;
    /**
     * Optional. The expiration date for the
     * sharing link, written in the format YYYYMM-DD. When set to 0 (default), the
     * sharing link is permanent.
     */
    private LocalDate dateExpired;
    /**
     * Optional. The available date for the sharing
     * link to become effective, written in the
     * format YYYY-MM-DD. When set to 0
     * (default), the sharing link is valid
     * immediately after creation.
     */
    private LocalDate dateAvailable;

    public DsmCreateShareRequest addFileOrFolder(String path) {
        this.paths.add(path);
        return this;
    }

    public DsmCreateShareRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public DsmCreateShareRequest setDateExpired(LocalDate dateExpired) {
        this.dateExpired = dateExpired;
        return this;
    }

    public DsmCreateShareRequest setDateAvailable(LocalDate dateAvailable) {
        this.dateAvailable = dateAvailable;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmCreateShareResponse>>() {};
    }

    @Override
    public Response<DsmCreateShareResponse> call() {
        if(paths.isEmpty()) {
            throw new DsmCreateFolderException("You have to specify at least one content to share");
        }
        addParameter("path", String.join(",", this.paths));
        Optional.ofNullable(this.password).ifPresent(password -> addParameter("password", password));
        Optional.ofNullable(this.dateExpired).ifPresent(date -> addParameter("date_expired", DateUtils.convertDateToString(date)));
        Optional.ofNullable(this.dateAvailable).ifPresent(date -> addParameter("date_available", DateUtils.convertDateToString(date)));

        return super.call();
    }
}
