package requests.filestation.search;

import com.fasterxml.jackson.core.type.TypeReference;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.search.DsmSearchStartResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmSearchStartRequest extends DsmAbstractRequest<DsmSearchStartResponse> {
    public DsmSearchStartRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Search";
        this.version = 1;
        this.method="start";
        this.path = "webapi/entry.cgi";
    }

    /**
     * A searched folder path starting
     * with a shared folder.
     */
    private String rootFolderPath;

    /**
     *Optional. If searching files within a
     * folder and subfolders recursively or
     * not
     */
    private boolean recursive = true;

    /**
     * Optional. Search for files whose
     * names and extensions match a
     * case-insensitive glob pattern.
     * Note:
     * 1. If the pattern doesn’t contain
     * any glob syntax (? and *), * of
     * glob syntax will be added at
     * begin and end of the string
     * automatically for partially
     * matching the pattern.
     * 2. You can use commas to separate
     * multiple glob patterns.
     */
    private List<String> patterns = new LinkedList<>();

    /**
     * Optional. Search for files whose
     * extensions match a file type
     * pattern in a case-insensitive glob
     * pattern. If you give this criterion,
     * folders aren’t matched
     *
     * Note: You can use commas to
     * separate multiple glob patterns.
     */
    private String extension;

    /**
     * Optional. file: enumerate regular
     * files; dir: enumerate folders; all
     * enumerate regular files and
     * folders.
     */
    private DsmRequestParameters.FileType fileType = DsmRequestParameters.FileType.ALL;

    /**
     * Optional. Search for files whose
     * sizes are greater than the given
     * byte size.
     */
    private Long minimumSize;

    /**
     * Optional. Search for files whose
     * sizes are less than the given byte
     * size.
     */
    private Long maximumSize;

    /**
     * Optional. Search for files whose
     * last modified time after the given
     * Linux timestamp in second.
     */
    private Long modifiedTimeAfter;

    /**
     * Optional. Search for files whose
     * last modified time before the given
     * Linux timestamp in second.
     */
    private Long modifiedTimeBefore;

    /**
     * Optional. Search for files whose
     * create time after the given Linux
     * timestamp in second.
     */
    private Long createdTimeAfter;

    /**
     * Optional. Search for files whose
     * create time before the given Linux
     * timestamp in second.
     */
    private Long createdTimeBefore;

    /**
     * Optional. Search for files whose
     * last access time after the given
     * Linux timestamp in second.
     */
    private Long lastAccessTimeAfter;

    /**
     * Optional. Search for files whose
     * last access time before the given
     * Linux timestamp in second.
     */
    private Long lastAccessTimeBefore;

    /**
     * Optional. Search for files whose
     * user name matches this criterion.
     * This criterion is case-insensitive.
     */
    private String owner;

    /**
     * Optional. Search for files whose
     * group name matches this criterion.
     * This criterion is case-insensitive
     */
    private String group;


    public DsmSearchStartRequest setRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
        return this;
    }

    public DsmSearchStartRequest setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public DsmSearchStartRequest addPattern(String pattern) {
        this.patterns.add(pattern) ;
        return this;
    }

    public DsmSearchStartRequest setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public DsmSearchStartRequest setFileType(DsmRequestParameters.FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public DsmSearchStartRequest setMinimumSize(Long minimumSize) {
        this.minimumSize = minimumSize;
        return this;
    }

    public DsmSearchStartRequest setMaximumSize(Long maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public DsmSearchStartRequest setModifiedTimeAfter(Long modifiedTimeAfter) {
        this.modifiedTimeAfter = modifiedTimeAfter;
        return this;
    }

    public DsmSearchStartRequest setModifiedTimeBefore(Long modifiedTimeBefore) {
        this.modifiedTimeBefore = modifiedTimeBefore;
        return this;
    }

    public DsmSearchStartRequest setCreatedTimeAfter(Long createdTimeAfter) {
        this.createdTimeAfter = createdTimeAfter;
        return this;
    }

    public DsmSearchStartRequest setCreatedTimeBefore(Long createdTimeBefore) {
        this.createdTimeBefore = createdTimeBefore;
        return this;
    }

    public DsmSearchStartRequest setLastAccessTimeAfter(Long lastAccessTimeAfter) {
        this.lastAccessTimeAfter = lastAccessTimeAfter;
        return this;
    }

    public DsmSearchStartRequest setLastAccessTimeBefore(Long lastAccessTimeBefore) {
        this.lastAccessTimeBefore = lastAccessTimeBefore;
        return this;
    }

    public DsmSearchStartRequest setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public DsmSearchStartRequest setGroup(String group) {
        this.group = group;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmSearchStartResponse>>() {};
    }

    @Override
    public Response<DsmSearchStartResponse> call() {
        Optional.ofNullable(this.rootFolderPath).ifPresent(root -> addParameter("folder_path", root));
        addParameter("recursive", String.valueOf(this.recursive));
        if(!this.patterns.isEmpty()) {
            addParameter("pattern", String.join(",", this.patterns));
        }
        Optional.ofNullable(this.extension).ifPresent(s -> addParameter("extension", s));
        addParameter("filetype", this.fileType.name().toLowerCase());
        Optional.ofNullable(this.minimumSize).ifPresent(s -> addParameter("size_from", String.valueOf(s)));
        Optional.ofNullable(this.maximumSize).ifPresent(s -> addParameter("size_to", String.valueOf(s)));
        Optional.ofNullable(this.modifiedTimeAfter).ifPresent(s -> addParameter("mtime_from", String.valueOf(s)));
        Optional.ofNullable(this.modifiedTimeBefore).ifPresent(s -> addParameter("mtime_to", String.valueOf(s)));
        Optional.ofNullable(this.createdTimeAfter).ifPresent(s -> addParameter("crtime_from", String.valueOf(s)));
        Optional.ofNullable(this.createdTimeBefore).ifPresent(s -> addParameter("crtime_to", String.valueOf(s)));
        Optional.ofNullable(this.lastAccessTimeAfter).ifPresent(s -> addParameter("atime_from", String.valueOf(s)));
        Optional.ofNullable(this.lastAccessTimeBefore).ifPresent(s -> addParameter("atime_to", String.valueOf(s)));
        Optional.ofNullable(this.owner).ifPresent(s -> addParameter("owner", s));
        Optional.ofNullable(this.group).ifPresent(s -> addParameter("group", s));
        return super.call();
    }

}
