package requests.filestation.share;

import com.fasterxml.jackson.core.type.TypeReference;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.share.DsmShareListResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DsmShareListRequest extends DsmAbstractRequest<DsmShareListResponse> {
    public DsmShareListRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Sharing";
        this.version = 3;
        this.method = "list";
        this.path = "webapi/entry.cgi";
    }

    /**
     * Optional. Specify how many
     * sharing links are skipped before
     * beginning to return listed sharing
     * links.
     */
    private Integer offset;
    /**
     * Optional. Number of sharing links
     * requested. 0 means to list all
     * sharing links.
     */
    private Integer limit;
    /**
     * Optional. Specify information of
     * the sharing link to sort on.
     * Options include:
     * id: a unique ID of sharing a
     * file/folder
     * name: file name
     * isFolder: if it’s a folder or not
     * path: file path
     * date_expired: the expiration
     * date for the sharing link
     * date_available: the available
     * date for the sharing link start
     * effective
     * status: the link accessibility
     * status
     * has_password: If the sharing
     * link is protected or not
     * url: a URL of a sharing link
     * link_owner: the user name of
     * the sharing link owner
     */
    private final List<DsmRequestParameters.ShareSort> sorts = new LinkedList<>();
    /**
     * Optional. Specify to sort
     * ascending or to sort descending.
     * Options include:
     * asc: sort ascending
     * desc: sort descending
     */
    private DsmRequestParameters.SortDirection direction;
    /**
     * Optional. If set to false, the data
     * will be retrieval from cache
     * database rapidly. If set to true, all
     * sharing information including
     * sharing statuses and user name
     * of sharing owner will be
     * synchronized. It consumes some
     * time
     */
    boolean forceClean = false;

    public DsmShareListRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public DsmShareListRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DsmShareListRequest addSort(DsmRequestParameters.ShareSort sort) {
        this.sorts.add(sort);
        return this;
    }

    public DsmShareListRequest setDirection(DsmRequestParameters.SortDirection direction) {
        this.direction = direction;
        return this;
    }

    public DsmShareListRequest setForceClean(boolean forceClean) {
        this.forceClean = forceClean;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmShareListResponse>>() {};
    }

    @Override
    public Response<DsmShareListResponse> call() {

        addParameter("offset", String.valueOf(Optional.ofNullable(offset).orElse(0)));
        addParameter("limit", String.valueOf(Optional.ofNullable(limit).orElse(0)));
        if(!sorts.isEmpty()) {
            addParameter("sort_by", sorts.stream().map(DsmRequestParameters.ShareSort::name).collect(Collectors.joining(",")));
        }
        Optional.ofNullable(this.direction).ifPresent(d -> addParameter("sort_direction", d.name()));
        addParameter("force_clean", String.valueOf(forceClean));
        return super.call();
    }
}
