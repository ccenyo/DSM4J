package requests.fileStation;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmDeleteException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.fileStation.DsmDeleteResponse;
import responses.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmAdvancedDeleteRequest  extends DsmAbstractRequest<DsmDeleteResponse> implements DsmNonBlockingProcess<DsmDeleteResponse>{

    /**
     * One or more deleted file/folder
     * paths starting with a shared
     * folder, separated by commas “,”
     */
    private List<String> filePaths = new LinkedList<>();

    /**
     * Optional. “true”: calculates the
     * progress of each deleted file with
     * the sub-folder recursively; “false”:
     * calculates the progress of files
     * which you give in path
     * parameters. The latter is faster
     * than recursively, but less precise.
     *
     * Note: Only non-blocking methods
     * suits using the status method
     * to get progress.
     */
    private boolean accurateProgress = true;

    /**
     * Optional. “true”: Recursively
     * delete files within a folder. “false”:
     * Only delete first-level file/folder. If
     * a deleted folder contains any file,
     * an error occurs because the
     * folder can’t be directly deleted.
     */
    private boolean recursive = true;

    /**
     * Optional. A unique ID for the
     * search task which is gotten from
     * start method. It’s used to
     * delete the file in the search
     * result.
     */
    private String searchTaskId;

    /**
     * A unique ID for the delete task which is
     * gotten from start method.
     */
    private String taskid;

    public DsmAdvancedDeleteRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Delete";
        this.version = 1;
        this.method = "start";
        this.path = "webapi/entry.cgi";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmDeleteResponse>>() {};
    }

    public DsmAdvancedDeleteRequest addFileToDelete(String path) {
        this.filePaths.add(path);
        return this;
    }

    public DsmAdvancedDeleteRequest removeFileToDelete(String path) {
        this.filePaths.remove(path);
        return this;
    }

    public DsmAdvancedDeleteRequest showAccurateProgress(boolean accurateProgress) {
        this.accurateProgress = accurateProgress;
        return this;
    }

    public DsmAdvancedDeleteRequest recursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public DsmAdvancedDeleteRequest setSearchTaskId(String searchTaskId) {
        this.searchTaskId = searchTaskId;
        return this;
    }

    public DsmAdvancedDeleteRequest taskId(String taskid) {
        this.taskid = taskid;
        return this;
    }

    @Override
    public Response<DsmDeleteResponse> start() {
        String paths = String.join(",", this.filePaths);
        if(paths.isEmpty()) {
            throw new DsmDeleteException("The files to delete cannot be empty");
        }
        addParameter("path", paths);
        this.method = "start";
        return this.call();
    }

    @Override
    public Response<DsmDeleteResponse> status() {
        Optional.ofNullable(this.taskid).orElseThrow(() -> new DsmDeleteException("You have to set taskid"));
        this.method = "status";
        return this.call();
    }

    @Override
    public Response<DsmDeleteResponse> stop() {
        Optional.ofNullable(this.taskid).orElseThrow(() -> new DsmDeleteException("You have to set taskid"));
        this.method = "stop";
        return this.call();
    }

    public Response<DsmDeleteResponse> call() {
        addParameter("accurate_progress", String.valueOf(this.accurateProgress));
        addParameter("recursive", String.valueOf(this.recursive));
        Optional.ofNullable(this.searchTaskId).ifPresent(s -> addParameter("search_taskid", s));
        Optional.ofNullable(this.taskid).ifPresent(s -> addParameter("taskid", s));
        return super.call();
    }



}
