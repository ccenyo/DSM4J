package requests.filestation.share;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmCreateFolderException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.share.DsmShareCreateResponse;
import utils.DateUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmShareCreateOrEditRequest extends DsmAbstractRequest<DsmShareCreateResponse> {
    public DsmShareCreateOrEditRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Sharing";
        this.version = 1;
        this.path = "webapi/entry.cgi";
    }

    /**
     * Unique ID(s) of sharing link(s) to edit,
     * separated by a comma
     */
    private String id;
    /**
     * One or more file/folder paths with which to
     * generate sharing links, separated by
     * commas
     */
    private final List<String> paths = new LinkedList<>();
    /**
     * Optional The password for the sharing link
     * when accessing it. The max password
     * length are 16 characters.
     */
    private String password;
    /**
     * Optional. The expiration date for the
     * sharing link, written in the format YYYYMM-DD hh:mm:ss. When set to 0 (default), the
     * sharing link is permanent.
     */
    private LocalDateTime dateExpired;
    /**
     * Optional. The available date for the sharing
     * link to become effective, written in the
     * format YYYY-MM-DD hh:mm:ss. When set to 0
     * (default), the sharing link is valid
     * immediately after creation.
     */
    private LocalDateTime dateAvailable;

    public DsmShareCreateOrEditRequest setId(String id) {
        this.id = id;
        return this;
    }

    public DsmShareCreateOrEditRequest addFileOrFolder(String path) {
        this.paths.add(path);
        return this;
    }

    public DsmShareCreateOrEditRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public DsmShareCreateOrEditRequest setDateExpired(LocalDateTime dateExpired) {
        this.dateExpired = dateExpired;
        return this;
    }

    public DsmShareCreateOrEditRequest setDateAvailable(LocalDateTime dateAvailable) {
        this.dateAvailable = dateAvailable;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmShareCreateResponse>>() {};
    }

    @Override
    public Response<DsmShareCreateResponse> call() {

        if(paths.isEmpty() && !Optional.ofNullable(this.id).isPresent()) {
            throw new DsmCreateFolderException("You have to specify at least one content to share");
        }

        if (Optional.ofNullable(this.id).isPresent()) {
            this.method = "edit";
        } else {
            this.method = "create";
        }

        if(!paths.isEmpty()) {
            addParameter("path", String.join(",", this.paths));
        }
        Optional.ofNullable(this.password).ifPresent(password -> addParameter("password", password));
        Optional.ofNullable(this.dateExpired).ifPresent(date -> addParameter("date_expired", DateUtils.convertDateToString(date)));
        Optional.ofNullable(this.dateAvailable).ifPresent(date -> addParameter("date_available", DateUtils.convertDateToString(date)));

        return super.call();
    }
}
