package requests;

import responses.DsmSharedFolderResponse;
import responses.Response;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DsmSharedFolderRequest extends DsmAbstractRequest<DsmSharedFolderResponse> {

    /**
     * Optional. Specify how many shared
     * folders are skipped before beginning
     * to return listed shared folders.
     */
    private Integer offset;
    /**
     * Optional. Number of shared folders
     * requested. 0 lists all shared folders
     */
    private Integer limit;
    /**
     * Optional. Specify which file
     * information to sort on.
     *
     * Options include:
     * name: file name
     * user: file owner
     * group: file group
     * mtime: last modified time
     * atime: last access time
     * ctime: last change time
     * crtime: create time
     * posix: POSIX permission
     */
    private List<DsmRequestParameters.Sort> sorts = new LinkedList<>();
    /**
     * Optional. Specify to sort ascending
     * or to sort descending.
     *
     * Options include:
     * asc: sort ascending
     * desc: sort descending
     */
    private DsmRequestParameters.SortDirection sortDirection;
    /**
     * Optional. “true”: List writable shared
     * folders; “false”: List writable and
     * read-only shared folders.
     */
    private boolean onlyWritable =false;
    /**
     * Optional. Additional requested file
     * information, separated by commas
     * “,”. When an additional option is
     * requested, responded objects will be
     * provided in the specified additional
     * option.
     *
     * Options include:
     *  real_path: return a real path in
     * volume
     *
     *  size: return file byte size
     *
     *  owner: return information
     * about file owner including user
     * name, group name, UID and
     * GID
     *
     *  time: return information about
     * time including last access time,
     * last modified time, last change
     * time and create time
     *
     *  perm: return information about
     * file permission
     *
     *  mount_point_type: return a
     * type of a virtual file system of a
     * mount point
     *
     *  volume_status: return volume
     * statuses including free space,
     * total space and read-only
     * status
     */
    private List<DsmRequestParameters.Additional> additionals = new LinkedList<>();

    public DsmSharedFolderRequest(DsmAuth auth) {
        super(auth);
    }

    @Override
    public String getAPIName() {
        return "SYNO.FileStation.List";
    }

    @Override
    public Integer getVersion() {
        return 1;
    }

    @Override
    public String getPath() {
        return "webapi/entry.cgi";
    }

    @Override
    public String getMethod() {
        return "list_share";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmSharedFolderResponse>>() {};
    }

    /**
     * Optional. Specify how many shared
     * folders are skipped before beginning
     * to return listed shared folders
     * @param offset
     * @return
     */
    public DsmSharedFolderRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Optional. Number of shared folders
     * requested. 0 lists all shared folders.
     * I
     * @param limit
     * @return
     */
    public DsmSharedFolderRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Optional. Specify to sort ascending
     * or to sort descending.
     *
     * Options include:
     * asc: sort ascending
     * desc: sort descending
     * @param sortDirection
     * @return
     */
    public DsmSharedFolderRequest setSortDirection(DsmRequestParameters.SortDirection sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    /**
     * Optional. “true”: List writable shared
     * folders; “false”: List writable and
     * read-only shared folders.
     * @param onlyWritable
     * @return
     */
    public DsmSharedFolderRequest setOnlyWritable(boolean onlyWritable) {
        this.onlyWritable = onlyWritable;
        return this;
    }

    public DsmSharedFolderRequest addAdditionalInfo(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    public DsmSharedFolderRequest removeAdditionalInfo(DsmRequestParameters.Additional additional) {
        this.additionals.remove(additional);
        return this;
    }

    public DsmSharedFolderRequest addSort(DsmRequestParameters.Sort sort) {
        this.sorts.add(sort);
        return this;
    }

    public DsmSharedFolderRequest removeSort(DsmRequestParameters.Sort sort) {
        this.sorts.remove(sort);
        return this;
    }

    @Override
    public Response<DsmSharedFolderResponse> call() {
        Optional.ofNullable(this.offset).ifPresent(offset -> addParameter("offset", String.valueOf(offset)));
        Optional.ofNullable(this.limit).ifPresent(limit -> addParameter("limit", String.valueOf(limit)));
        Optional.ofNullable(this.sortDirection).ifPresent(sortDirection -> addParameter("sort_direction", sortDirection.name()));
        addParameter("onlywritable", String.valueOf(onlyWritable));

        if(!sorts.isEmpty()) {
            addParameter("sort_by", sorts.stream().map(DsmRequestParameters.Sort::name).collect(Collectors.joining(",")));
        }

        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }

        return super.call();
    }
}
