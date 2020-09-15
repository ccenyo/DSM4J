package requests;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmException;
import exeptions.DsmUploadException;
import responses.DsmUploadResponse;
import responses.Response;
import utils.DateUtils;
import utils.DsmUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DsmUploadRequest  extends DsmAbstractRequest<DsmUploadResponse> {

    public enum OverwriteBehaviour {
        SKIP(false),
        OVERWRITE(true),
        ERROR(null);

        OverwriteBehaviour(Boolean value) {
            this.value = value;
        }
        private final Boolean value;

        public Boolean getValue() {
            return value;
        }
    }
    private String destinationFolderPath;
    private Boolean createParents;
    private OverwriteBehaviour overwrite = OverwriteBehaviour.ERROR;
    private String filePath;
    private LocalDateTime lastModifiedTime;
    private LocalDateTime createdTime;
    private LocalDateTime lastAccessedTime;

    public DsmUploadRequest(DsmAuth auth) {
        super(auth);
    }

    @Override
    public String getAPIName() {
        return "SYNO.FileStation.Upload";
    }

    @Override
    public Integer getVersion() {
        return 2;
    }

    @Override
    public String getPath() {
        return "webapi/entry.cgi";
    }

    @Override
    public String getMethod() {
        return "upload";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmUploadResponse>>() {};
    }

    public DsmUploadRequest setDestinationFolderPath(String destinationFolderPath) {
        this.destinationFolderPath = destinationFolderPath;
        return this;
    }

    public DsmUploadRequest createParentFolders(Boolean createParents) {
        this.createParents = createParents;
        return this;
    }

    public DsmUploadRequest overwrite(OverwriteBehaviour overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public DsmUploadRequest setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }


    public DsmUploadRequest setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public DsmUploadRequest setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public DsmUploadRequest setLastAccessedTime(LocalDateTime lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
        return this;
    }

    @Override
    public Response<DsmUploadResponse> call() {

        Map<String, String> params = new HashMap<>();
        params.put("path", Optional.ofNullable(this.destinationFolderPath).orElseThrow(() -> new DsmException("you must define destination folder")));
        Optional.ofNullable(this.createParents).ifPresent(c -> params.put("create_parents", c.toString()));

        if(!this.overwrite.equals(OverwriteBehaviour.ERROR)) {
            params.put("overwrite", String.valueOf(this.overwrite.getValue()));
        }

        if(!new File(Optional.ofNullable(this.filePath).orElseThrow(() -> new DsmException("you must define source file to upload"))).exists()) {
            throw new DsmUploadException("File does not exist");
        }

        addDatesToRequest("atime", params, this.lastAccessedTime);
        addDatesToRequest("crtime", params, this.createdTime);
        addDatesToRequest("mtime", params, this.lastModifiedTime);

       try {
           String resp = DsmUtils.makePostRequest(
                    build(),
                    this.filePath,
                    params
            );
           return deserialize(resp);
        } catch (IOException e) {
           throw new DsmException(e);
       }
    }

    private void addDatesToRequest(String key, Map<String, String> params, LocalDateTime time) {
        Optional.ofNullable(time).ifPresent(c -> params.put(key, String.valueOf(DateUtils.convertLocalDateTimeToUnixTimestamp(c))));
    }

    @Override
     protected String build() {
        return auth.getHost() +
                (auth.getPort() == null ? "" : ":" + auth.getPort()) +
                "/" +
                getPath()+
                "?_sid="+
                auth.getSid()+"&"+
                "api="+getAPIName()+"&"+
                "method="+getMethod()+"&"+
                "version="+getVersion();
    }
}
