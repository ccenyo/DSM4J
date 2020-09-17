package requests;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmDeleteException;
import responses.DsmSimpleDeleteResponse;
import responses.Response;

import java.util.Optional;

/**
 * Delete files/folders. This is a blocking method. The response is not returned until the deletion
 * operation is completed.
 */
public class DsmSimpleDeleteRequest extends DsmAbstractRequest<DsmSimpleDeleteResponse> {

    private String filePath = new String();
    private boolean recursive = true;
    private Boolean searchTaskId;

    public DsmSimpleDeleteRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Delete";
        this.version = 2;
        this.method = "delete";
        this.path = "webapi/entry.cgi";
    }

    /**
     * One or more deleted file/folder path(s)
     * started with a shared folder, separated by
     * a comma, “,”.
     * @param filePath the path
     * @return DsmSimpleDeleteRequest
     */
    public DsmSimpleDeleteRequest addFileToDelete(String filePath) {
        this.filePath = filePath;
        return this;
    }


    /**
     * Optional. “true”: Recursively delete files
     * within a folder. “false”: Only delete firstlevel file/folder. If a deleted folder contains
     * any file, an error will occur because the
     * folder can’t be directly deleted.
     * @param recursive recursive or not
     * @return DsmSimpleDeleteRequest
     */
    public DsmSimpleDeleteRequest setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    /**
     * Optional. A unique ID for the search task
     * which is gotten from start method. It’s
     * used to delete the file in the search result.
     * @param searchTaskId searchId should be set
     * @return DsmSimpleDeleteRequest
     */
    public DsmSimpleDeleteRequest useSearchTaskId(boolean searchTaskId) {
        this.searchTaskId = searchTaskId;
        return this;
    }

    @Override
    protected TypeReference getClassForMapper() {
        return  new TypeReference<Response<DsmSimpleDeleteResponse>>() {};
    }

    public Response<DsmSimpleDeleteResponse> call() {
        addParameter("path", Optional.ofNullable(this.filePath).orElseThrow(() -> new DsmDeleteException("the files to delete can't be empty")));
        addParameter("recursive", String.valueOf(this.recursive));
        Optional.ofNullable(this.searchTaskId).ifPresent(searchTaskId -> addParameter("search_taskid", searchTaskId.toString()));
        return super.call();
    }
}
