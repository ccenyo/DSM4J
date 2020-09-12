package Requests;

import Responses.DsmSharedFolderResponse;
import Responses.Response;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DsmSharedFolderRequest extends DsmAbstractRequest<DsmSharedFolderResponse> {

    enum Sort {
        /**
         * file name
         */
        name,
        /**
         * file owner
         */
        user,
        /**
         * file group
         */
        group,
        /**
         * last modified time
         */
        mtime,
        /**
         * last access time
         */
        atime,
        /**
         * last change time
         */
        ctime,
        /**
         * create time
         */
        crtime,
        /**
         * POSIX permission
         */
        posix
    }

    enum SortDirection {
        /**
         * sort ascending
         */
        asc,
        /**
         * sort descending
         */
        desc
    }

    enum Additional {
        /**
         * return a real path in volume
         */
        real_path,
        /**
         * return file byte size
         */
        size,
        /**
         * return information about file owner including user name, group name, UID and GID
         */
        owner,
        /**
         * return information about time including last access time, last modified time, last change time and create time
         */
        time,
        /**
         * return information about file permission
         */
        perm,
        /**
         * return a type of a virtual file system of a mount point
         */
        mount_point_type,
        /**
         * return volume statuses including free space, total space and read-only status
         */
        volume_status
    }

    private Integer offset;
    private Integer limit;
    private List<Sort> sorts = new LinkedList<>();
    private SortDirection sortDirection;
    private boolean onlyWritable =false;
    private List<Additional> additionals = new LinkedList<>();

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
    public DsmSharedFolderRequest setSortDirection(SortDirection sortDirection) {
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

    public DsmSharedFolderRequest addAdditionalInfo(Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    public DsmSharedFolderRequest removeAdditionalInfo(Additional additional) {
        this.additionals.remove(additional);
        return this;
    }

    public DsmSharedFolderRequest addSort(Sort sort) {
        this.sorts.add(sort);
        return this;
    }

    public DsmSharedFolderRequest removeSort(Sort sort) {
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
            addParameter("sort_by", sorts.stream().map(Sort::name).collect(Collectors.joining(",")));
        }

        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(Additional::name).collect(Collectors.joining(",")));
        }

        return super.call();
    }
}
