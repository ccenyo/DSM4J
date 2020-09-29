package requests.filestation;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmRenameException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmRenameResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DsmRenameRequest  extends DsmAbstractRequest<DsmRenameResponse> {

    /**
     * One or more paths of files/folders to
     * be renamed, separated by commas . The number of paths must be the
     * same as the number of names in the
     * name parameter. The first path
     * parameter corresponds to the first
     * name parameter.
     */
    private final List<String> paths = new LinkedList<>();

    /**
     * One or more new names, separated
     * by commas. The number of
     * names must be the same as the
     * number of folder paths in the path
     * parameter. The first name parameter
     * corresponding to the first path
     * parameter
     */
    private List<String> names = new LinkedList<>();

    /**
     * Optional. Additional requested file
     * information, separated by commas
     * . When an additional option is
     * requested, responded objects will be
     * provided in the specified additional
     * option.
     * Options include:
     *
     * real_path: return a real path in
     * volume
     *size: return file byte size
     *
     * owner: return information
     * about file owner including user
     * name, group name, UID and
     * GID
     *
     * time: return information about
     * time including last access time,
     * last modified time, last change
     * time and create time
     *
     * perm: return information about
     * file permission
     *
     * type: return a file extension
     */
    private final List<DsmRequestParameters.Additional> additionals = new LinkedList<>();

    public DsmRenameRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Rename";
        this.version = 1;
        this.method = "rename";
        this.path = "webapi/entry.cgi";
    }

    public DsmRenameRequest addFileOrFolderToRename(String path) {
        this.paths.add(path);
        return this;
    }

    public DsmRenameRequest addNewNames(String name) {
        this.names.add(name);
        return this;
    }

    public DsmRenameRequest addAdditionalInfo(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmRenameResponse>>() {};
    }

    @Override
    public Response<DsmRenameResponse> call() {
        if(this.paths.isEmpty()) {
            throw new DsmRenameException("You have to add at least one path to rename");
        }

        if(this.names.isEmpty()) {
            throw new DsmRenameException("You have to add names to rename elements in paths");
        }

        if(this.paths.size() > this.names.size()) {
            throw new DsmRenameException("You have to add new names to all your paths to rename");
        }

        this.names = this.names.stream().limit(this.paths.size()).collect(Collectors.toList());

        addParameter("path", String.join(",", this.paths));
        addParameter("name", String.join(",", this.names));
        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }
        return super.call();
    }
}
