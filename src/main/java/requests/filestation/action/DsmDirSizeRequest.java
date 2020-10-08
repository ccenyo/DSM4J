package requests.filestation.action;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmDirSizeException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import requests.filestation.DsmNonBlockingProcess;
import responses.Response;
import responses.filestation.action.DsmDirSizeResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmDirSizeRequest extends DsmAbstractRequest<DsmDirSizeResponse> implements DsmNonBlockingProcess<DsmDirSizeResponse> {
    public DsmDirSizeRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.DirSize";
        this.version = 1;
        this.path = "webapi/entry.cgi";
    }

    /**
     * One or more file/folder paths starting with
     * a shared folder for calculating cumulative
     * size, separated by a comma.
     */
    private final List<String> paths = new LinkedList<>();

    private String taskId;


    public DsmDirSizeRequest addPath(String path) {
        this.paths.add(path);
        return this;
    }

    public DsmDirSizeRequest setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmDirSizeResponse>>() {};
    }

    @Override
    public Response<DsmDirSizeResponse> start() {
        this.method = "start";
        if(paths.isEmpty()) {
            throw new DsmDirSizeException("You have to specify at least one path");
        }
        addParameter("path", String.join(",", this.paths));
        return super.call();
    }

    @Override
    public Response<DsmDirSizeResponse> status() {
        this.method = "status";
        Optional.ofNullable(this.taskId).orElseThrow(() -> new DsmDirSizeException("taskId is null"));
        addParameter("taskid", this.taskId);
        return super.call();
    }

    @Override
    public Response<DsmDirSizeResponse> stop() {
        this.method = "stop";
        Optional.ofNullable(this.taskId).orElseThrow(() -> new DsmDirSizeException("taskId is null"));
        addParameter("taskid", this.taskId);
        return super.call();
    }
}
