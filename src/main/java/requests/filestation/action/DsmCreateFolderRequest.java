package requests.filestation.action;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmCreateFolderException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.action.DsmCreateFolderResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DsmCreateFolderRequest extends DsmAbstractRequest<DsmCreateFolderResponse> {
    public DsmCreateFolderRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.CreateFolder";
        this.version = 1;
        this.method = "create";
        this.path = "webapi/entry.cgi";
    }

    /**
     * One or more shared folder paths,
     * separated by commas. If
     * force_parent is true and
     * folder_path does not exist, the
     * folder_path will be created. If
     * force_parent is false,
     * folder_path must exist or a
     * false value will be returned. The
     * number of paths must be the same
     * as the number of names in the
     * name parameter. The first
     * folder_path parameter
     * corresponds to the first name
     * parameter
     */
    private final List<String> folderPaths = new LinkedList<>();

    /**
     * One or more new folder names,
     * separated by commas. The
     * number of names must be the
     * same as the number of folder paths
     * in the folder_path parameter.
     * The first name parameter
     * corresponding to the first
     * folder_path parameter
     */
    private final List<String> names = new LinkedList<>();

    /**
     * Optional. true: no error occurs if a
     * folder exists and make parent
     * folders as needed; false: parent
     * folders are not created.
     */
    private boolean forceParent = false;

    /**
     * Optional. Additional requested file
     * information, separated by commas. When an additional option is
     * requested, responded objects will
     * be provided in the specified
     * additional option.
     * Options include:
     * real_path: return a real path
     * in volume
     * size: return file byte size
     * owner: return information
     * about file owner including
     * user name, group name, UID
     * and GID
     * time: return information about
     * time including last access
     * time, last modified time, last
     * change time and create time
     * perm: return information
     * about file permission
     * type: return a file extension
     */
    private final List<DsmRequestParameters.Additional> additionals = new LinkedList<>();

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmCreateFolderResponse>>() {};
    }

    public DsmCreateFolderRequest addNewFolder(String parentPath, String name) {
        this.folderPaths.add(parentPath);
        this.names.add(name);
        return this;
    }

    public DsmCreateFolderRequest forceCreateParentFolder(boolean forceParent) {
        this.forceParent = forceParent;
        return this;
    }

    public DsmCreateFolderRequest addAdditional(DsmRequestParameters.Additional additional) {
        this.additionals.add(additional);
        return this;
    }

    @Override
    public Response<DsmCreateFolderResponse> call() {
        String parentPaths = String.join(",", this.folderPaths);
        String newFolderNames = String.join(",", this.names);

        if(parentPaths.isEmpty()) {
            throw new DsmCreateFolderException("The parent path cannot be empty");
        }

        if(newFolderNames.isEmpty()) {
            throw new DsmCreateFolderException("You have to specify the folder to create");
        }

        addParameter("folder_path", parentPaths);
        addParameter("name", newFolderNames);
        addParameter("force_parent", String.valueOf(this.forceParent));

        if(!additionals.isEmpty()) {
            addParameter("additional", additionals.stream().map(DsmRequestParameters.Additional::name).collect(Collectors.joining(",")));
        }
        return super.call();
    }
}
