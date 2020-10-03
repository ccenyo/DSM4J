package requests.filestation.search;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmSearchException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.search.DsmSearchResultResponse;
import responses.filestation.search.DsmSearchStartResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * List matched files in a search temporary database. You can check the finished value in response to
 * know if the search operation is processing or has been finished.
 */
public class DsmSearchResultRequest  extends DsmAbstractRequest<DsmSearchResultResponse> {
    public DsmSearchResultRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Search";
        this.version = 1;
        this.method="list";
        this.path = "webapi/entry.cgi";
    }

    /**
     * A unique ID for the search task
     * which is gotten from start
     * method.
     */
    private String taskId;

    /**
     * Optional. Specify how many
     * matched files are skipped before
     * beginning to return listed
     * matched files.
     */
    private Integer offset;

    /**
     * Optional. Number of matched
     * files requested. 0 indicates to list
     * all matched files
     */
    private Integer limit;

    /**
     * Optional. Specify which file
     * information to sort on.
     * Options include:
     * name: file name
     * size: file size
     * user: file owner
     * group: file group
     * mtime: last modified time
     * atime: last access time
     * ctime: last change time
     * crtime: create time
     * posix: POSIX permission
     * type: file extension
     */
    private List<DsmRequestParameters.Sort> sorts = new LinkedList<>();

    /**
     * Optional. Specify to sort
     * ascending or to sort descending.
     * Options include:
     * asc: sort ascending
     * desc: sort descending
     */
    private DsmRequestParameters.SortDirection sortDirection;

    /**
     * Optional. Given glob pattern(s) to
     * find files whose names and
     * extensions match a caseinsensitive glob pattern.
     * Note:
     * 1. If the pattern doesn’t contain
     * any glob syntax (? and *), *
     * of glob syntax will be added
     * at begin and end of the string
     * automatically for partially
     * matching the pattern.
     * 2. You can use commas to separate
     * multiple glob patterns.
     */
    private List<String> patterns = new LinkedList<>();

    /**
     * Optional. file: enumerate
     * regular files; dir: enumerate
     * folders; all enumerate regular
     * files and folders.
     */
    private DsmRequestParameters.FileType fileType = DsmRequestParameters.FileType.ALL;

    /**
     * Optional. Additional requested
     * file information, separated by a
     * comma. When an additional
     * option is requested, responded
     * objects will be provided in the
     * specified additional option.
     * Options include:
     *
     * real_path: return a real
     * path in volume
     *
     * size: return file byte size
     *
     * owner: returns information
     * about file owner including
     * user name, group name,
     * UID and GID
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
     * type: return a file extension
     */
    private List<DsmRequestParameters.Additional> additionals = new LinkedList<>();

    public DsmSearchResultRequest setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public DsmSearchResultRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public DsmSearchResultRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DsmSearchResultRequest addSorts(DsmRequestParameters.Sort sort) {
        this.sorts.add(sort);
        return this;
    }

    public DsmSearchResultRequest setSortDirection(DsmRequestParameters.SortDirection sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    public DsmSearchResultRequest addPattern(String pattern) {
        this.patterns.add(pattern);
        return this;
    }

    public DsmSearchResultRequest setFileType(DsmRequestParameters.FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public DsmSearchResultRequest addAdditionalInformation(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new  TypeReference<Response<DsmSearchResultResponse>>() {};
    }

    @Override
    public Response<DsmSearchResultResponse> call() {
        addParameter("taskid", Optional.ofNullable(taskId).orElseThrow(() -> new DsmSearchException("taskId must not be null")));
        Optional.ofNullable(this.offset).ifPresent(o -> addParameter("offset", String.valueOf(o)));
        Optional.ofNullable(this.limit).ifPresent(o -> addParameter("limit", String.valueOf(o)));
        if(!sorts.isEmpty()) {
            addParameter("sort_by", sorts.stream().map(DsmRequestParameters.Sort::name).collect(Collectors.joining(",")));
        }

        Optional.ofNullable(this.sortDirection).ifPresent(direction -> addParameter("sort_direction", direction.name()));

        if(!patterns.isEmpty()) {
            addParameter("pattern", String.join(",", patterns));
        }

        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }

        addParameter("filetype", this.fileType.name().toLowerCase());
        return super.call();
    }
}
