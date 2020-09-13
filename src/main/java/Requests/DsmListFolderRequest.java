package Requests;

import Exeptions.DsmListFolderException;
import Responses.DsmListFolderResponse;
import Responses.Response;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DsmListFolderRequest extends DsmAbstractRequest<DsmListFolderResponse> {

    /**
     * A listed folder path started with a
     * shared folder.
     */
    private String folderPath;
    /**
     * Optional. Specify how many files
     * are skipped before beginning to
     * return listed files.
     */
    private Integer offset;
    /**
     * Optional. Number of files
     * requested. 0 indicates to list all
     * files with a given folder
     */
    private Integer limit;
    /**
     * Optional. Specify which file
     * information to sort on.
     *
     *Options include:
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
     * Optional. Specify to sort ascending
     * or to sort descending
     *
     * Options include:
     * asc: sort ascending
     * desc: sort descending
     */
    private DsmRequestParameters.SortDirection sortDirection;
    /**
     * Optional. Given glob pattern(s) to
     * find files whose names and
     * extensions match a caseinsensitive glob pattern.
     *
     * Note:
     * 1. If the pattern doesn’t contain
     * any glob syntax (? and *), * of
     * glob syntax will be added at
     * begin and end of the string
     * automatically for partially
     * matching the pattern.
     *
     * 2. You can use ”,” to separate
     * multiple glob patterns.
     */
    private List<String> patterns = new LinkedList<>();
    /**
     * Optional. “file”: only enumerate
     * regular files; “dir”: only enumerate
     * folders; “all” enumerate regular
     * files and folders
     */
    private DsmRequestParameters.FileType fileType;
    /**
     * Optional. Folder path started with a
     * shared folder. Return all files and
     * sub-folders within folder_path
     * path until goto_path path
     * recursively.
     */
    private String goToPath;
    /**
     * Optional. Additional requested file
     * information, separated by a
     * comma, “,”. When an additional
     * option is requested, responded
     * objects will be provided in the
     * specified additional option.
     * Options include:
     *
     *  real_path: return a real path
     * in volume
     *
     *  size: return file byte size
     *
     *  owner: return information
     * about file owner including
     * user name, group name, UID
     * and GID
     *
     *  time: return information
     * about time including last
     * access time, last modified
     * time, last change time and
     * create time
     *
     *  perm: return information
     * about file permission
     *
     *  mount_point_type: return a
     * type of a virtual file system of
     * a mount point
     *
     *  type: return a file extension
     */
    private List<DsmRequestParameters.Additional> additionals = new LinkedList<>();

    public DsmListFolderRequest(DsmAuth auth) {
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
        return "list";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmListFolderResponse>>() {};
    }

    /**
     * A listed folder path started with a
     * shared folder.
     * @param folderPath
     * @return
     */
    public DsmListFolderRequest setFolderPath(String folderPath) {
        this.folderPath = folderPath;
        return this;
    }

    /**
     * Optional. Specify how many files
     * are skipped before beginning to
     * return listed files.
     * @param offset
     * @return
     */
    public DsmListFolderRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Optional. Number of files
     * requested. 0 indicates to list all
     * files with a given folder.
     * @param limit
     * @return
     */
    public DsmListFolderRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Optional. Specify which file
     * information to sort on.
     * @param sort
     * @return
     */
    public DsmListFolderRequest addSort(DsmRequestParameters.Sort sort) {
        this.sorts.add(sort);
        return this;
    }

    /**
     * delete sort
     * @param sort
     * @return
     */
    public DsmListFolderRequest removeSort(DsmRequestParameters.Sort sort) {
        this.sorts.remove(sort);
        return this;
    }

    /**
     * Optional. Specify to sort ascending
     * or to sort descending
     * @param sortDirection
     * @return
     */
    public DsmListFolderRequest setSortDirection(DsmRequestParameters.SortDirection sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    /**
     * Optional. Given glob pattern(s) to
     * find files whose names and
     * extensions match a caseinsensitive glob pattern
     * @param pattern
     * @return
     */
    public DsmListFolderRequest addPattern(String pattern) {
        this.patterns.add(pattern);
        return this;
    }

    /**
     * remove pattern
     * @param pattern
     * @return
     */
    public DsmListFolderRequest removePattern(String pattern) {
        this.patterns.remove(pattern);
        return this;
    }

    /**
     * Optional. “file”: only enumerate
     * regular files; “dir”: only enumerate
     * folders; “all” enumerate regular
     * files and folders
     * @param fileType
     * @return
     */
    public DsmListFolderRequest setFileType(DsmRequestParameters.FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    /**
     * Optional. Folder path started with a
     * shared folder. Return all files and
     * sub-folders within folder_path
     * path until goto_path path
     * recursively.
     * @param goToPath
     * @return
     */
    public DsmListFolderRequest setGoToPath(String goToPath) {
        this.goToPath = goToPath;
        return this;
    }

    /**
     * Optional. Additional requested file
     * information, separated by a
     * comma, “,”. When an additional
     * option is requested, responded
     * objects will be provided in the
     * specified additional option
     * @param additional
     * @return
     */
    public DsmListFolderRequest addAdditional(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    /**
     * remove additional
     * @param additional
     * @return
     */
    public DsmListFolderRequest removeAdditional(DsmRequestParameters.Additional additional) {
        this.additionals.remove(additional);
        return this;
    }

    @Override
    public Response<DsmListFolderResponse> call() {
        Optional.ofNullable(this.folderPath).orElseThrow(() -> new DsmListFolderException("the root folder path can not be null"));

        addParameter("folder_path", escape(this.folderPath));
        Optional.ofNullable(this.offset).ifPresent(offset -> addParameter("offset", String.valueOf(offset)));
        Optional.ofNullable(this.limit).ifPresent(limit -> addParameter("limit", String.valueOf(limit)));
        Optional.ofNullable(this.sortDirection).ifPresent(sortDirection -> addParameter("sort_direction", sortDirection.name()));
        Optional.ofNullable(this.fileType).ifPresent(fileType -> addParameter("filetype", fileType.name()));
        Optional.ofNullable(this.goToPath).ifPresent(goToPath -> addParameter("goto_path", goToPath));

        if(!sorts.isEmpty()) {
            addParameter("sort_by", sorts.stream().map(DsmRequestParameters.Sort::name).collect(Collectors.joining(",")));
        }

        if(!patterns.isEmpty()) {
            addParameter("pattern", String.join(",", patterns));
        }

        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }

        return super.call();
    }
}
