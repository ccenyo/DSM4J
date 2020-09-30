package requests.filestation;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmCopyMoveException;
import exeptions.DsmDeleteException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmCopyMoveResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmCopyMoveRequest extends DsmAbstractRequest<DsmCopyMoveResponse> implements DsmNonBlockingProcess<DsmCopyMoveResponse> {

    /**
     * One or more copied/moved
     * file/folder path(s) starting with a
     * shared folder, separated by
     * commas
     */
    private List<String> paths = new LinkedList<>();
    /**
     * A desitination folder path where
     * files/folders are copied/moved.
     */
    private String destinationFolderPath;
    /**
     * Optional. true: overwrite all
     * existing files with the same
     * name; false: skip all existing
     * files with the same name;
     * (None): do not overwrite or skip
     * existed files. If there is any
     * existing files, an error occurs
     * (error code: 1003).
     */
    private DsmRequestParameters.OverwriteBehaviour overwrite = DsmRequestParameters.OverwriteBehaviour.ERROR;
    /**
     * Optional. true: move
     * filess/folders;”false”: copy
     * files/folders
     */
    private boolean removeSrc = false;
    /**
     * Optional true: calculate the
     * progress by each
     * moved/copied file within subfolder. false: calculate the
     * progress by files which you give
     * in path parameters. This
     * calculates the progress faster,
     * but is less precise.
     */
    private boolean accurateProgress = true;
    /**
     * Optional. A unique ID for the
     * search task which is gotten
     * from SYNO.FileSation.Search
     * API with start method. This
     * is used to update the search
     * result.
     */
    private String searchTaskId;

    /**
     * A unique ID for the delete task which is
     * gotten from start method.
     */
    private String taskId;

    public DsmCopyMoveRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.CopyMove";
        this.version = 1;
        this.path = "webapi/entry.cgi";
    }

    public DsmCopyMoveRequest addPathToCopy(String path) {
        this.paths.add(path);
        return this;
    }

    public DsmCopyMoveRequest setDestinationFolderPath(String destinationFolderPath) {
        this.destinationFolderPath = destinationFolderPath;
        return this;
    }

    public DsmCopyMoveRequest setOverwriteBehaviour(DsmRequestParameters.OverwriteBehaviour overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public DsmCopyMoveRequest setRemoveSrc(boolean removeSrc) {
        this.removeSrc = removeSrc;
        return this;
    }

    public DsmCopyMoveRequest setAccurateProgress(boolean accurateProgress) {
        this.accurateProgress = accurateProgress;
        return this;
    }

    public DsmCopyMoveRequest setSearchTaskId(String searchTaskId) {
        this.searchTaskId = searchTaskId;
        return this;
    }

    public DsmCopyMoveRequest setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmCopyMoveResponse>>() {};
    }

    @Override
    public Response<DsmCopyMoveResponse> start() {
        String paths = String.join(",", this.paths);
        String destinationPath = Optional.ofNullable(this.destinationFolderPath).orElseThrow(() -> new DsmCopyMoveException("You have to set destination path"));
        if(paths.isEmpty()) {
            throw new DsmCopyMoveException("The paths to copy or move cannot be empty");
        }
        if(!this.overwrite.equals(DsmRequestParameters.OverwriteBehaviour.ERROR)) {
            addParameter("overwrite", String.valueOf(this.overwrite.getValue()));
        }
        addParameter("path", paths);
        addParameter("dest_folder_path", destinationPath);
        this.method = "start";
        return this.call();
    }

    @Override
    public Response<DsmCopyMoveResponse> status() {
        if(this.taskId == null) {
            throw new DsmDeleteException("You have to set taskId");
        }
        this.method = "status";
        return this.call();
    }

    @Override
    public Response<DsmCopyMoveResponse> stop() {
        if(this.taskId == null) {
            throw new DsmDeleteException("You have to set task id");
        }
        this.method = "stop";
        return this.call();
    }

    @Override
    public Response<DsmCopyMoveResponse> call() {
        addParameter("accurate_progress", String.valueOf(this.accurateProgress));
        addParameter("remove_src", String.valueOf(this.removeSrc));
        Optional.ofNullable(this.searchTaskId).ifPresent(s -> addParameter("search_taskid", s));
        Optional.ofNullable(this.taskId).ifPresent(s -> addParameter("taskid", s));
        return super.call();
    }
}
