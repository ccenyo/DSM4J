package requests.filestation.search;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmSearchException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmSimpleResponse;

import java.util.LinkedList;
import java.util.List;

public class DsmSearchStopRequest  extends DsmAbstractRequest<DsmSimpleResponse> {
    public DsmSearchStopRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Search";
        this.version = 1;
        this.method="stop";
        this.path = "webapi/entry.cgi";
    }

    /**
     * Unique ID(s) for the search task which are
     * gotten from start method. Specify
     * multiple search task IDs by commas.
     */
    private final List<String> taskIds = new LinkedList<>();

    public DsmSearchStopRequest addTaskId(String taskId) {
        this.taskIds.add(taskId);
        return this;
    }



    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmSimpleResponse>>() {};
    }

    @Override
    public Response<DsmSimpleResponse> call() {
        if(this.taskIds.isEmpty()) {
            throw  new DsmSearchException("You have to specify at least one taskId");
        }

        addParameter("taskid", String.join(",", this.taskIds));

        return super.call();
    }
}
