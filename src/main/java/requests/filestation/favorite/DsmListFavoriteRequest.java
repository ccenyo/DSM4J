package requests.filestation.favorite;

import com.fasterxml.jackson.core.type.TypeReference;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.favorite.DsmListFavoriteResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Add a folder to user’s favorites or perform operations on user’s favorites.
 */
public class DsmListFavoriteRequest extends DsmAbstractRequest<DsmListFavoriteResponse> {
    public DsmListFavoriteRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Favorite";
        this.version = 1;
        this.method = "list";
        this.path = "webapi/entry.cgi";
    }

    /**
     * Optional. Specify how many
     * favorites are skipped before
     * beginning to return user’s
     * favorites.
     */
    private Integer offset;

    /**
     * Optional. Number of favorites
     * requested. 0 indicates to list all
     * favorites.
     */
    private Integer limit;

    /**
     * Optional. Show favorites with a
     * given favorite status.
     * Options of favorite statuses
     * include:
     * valid: A folder which a favorite
     * links to exists
     * broken: A folder which a favorite
     * links to doesn’t exist or doesn’t be
     * permitted to access it
     * all: Both valid and broken statuses
     */
    private DsmRequestParameters.StatusFilter statusFilter = DsmRequestParameters.StatusFilter.ALL;

    /**
     * Optional. Additional requested
     * information of a folder which a
     * favorite links to, separated by a
     * comma. When an additional
     * option is requested, responded
     * objects will be provided in the
     * specified additional option
     *
     * Options include:
     *
     * real_path: return a real path
     * in volume
     *
     * owner: return information
     * about file owner including
     * user name, group name, UID
     * and GID
     *
     * time: return information
     * about time including last
     * access time, last modified
     * time, last change time and
     * create time
     *
     * perm: return information
     * about file permission
     *
     * mount_point_type: return a
     * type of a virtual file system of
     * a mount point
     */
    List<DsmRequestParameters.Additional> additionals = new LinkedList<>();


    public DsmListFavoriteRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public DsmListFavoriteRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DsmListFavoriteRequest setStatusFilter(DsmRequestParameters.StatusFilter statusFilter) {
        this.statusFilter = statusFilter;
        return this;
    }

    public DsmListFavoriteRequest addAdditionalInformation(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmListFavoriteResponse>>() {};
    }

    @Override
    public Response<DsmListFavoriteResponse> call() {
        Optional.ofNullable(this.offset).ifPresent(o -> addParameter("offset", String.valueOf(o)));
        Optional.ofNullable(this.limit).ifPresent(o -> addParameter("limit", String.valueOf(o)));
        addParameter("status_filter", this.statusFilter.name().toLowerCase());
        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }
        return super.call();
    }
}
