package requests.filestation;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmDownloadException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.filestation.DsmDownloadResponse;
import responses.Response;
import utils.DsmUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

/**
 * Download files/folders. If only one file is specified, the file content is responded. If more than one
 * file/folder is given, binary content in ZIP format which they are compressed to is responded.
 */
public class DsmDownloadRequest extends DsmAbstractRequest<DsmDownloadResponse> {

    /**
     * One or more file/folder paths starting with a
     * shared folder to be downloaded, separated
     * by a commas. When more than one file
     * is to be downloaded, files/folders will be
     * compressed as a zip file.
     */
    private String filePath;
    /**
     * Mode used to download files/folders, value
     * could be:
     * (1) open: try to trigger the application,
     * such as a web browser, to open it.
     * Content-Type of the HTTP header of
     * the response is set to MIME type
     * according to file extension.
     * (2) download: try to trigger the application,
     * such as a web browser, to download it.
     * Content-Type of the HTTP header of
     * response is set to application/octetstream and Content-Disposition of the
     * HTTP header of the response is set to
     * attachment.
     */
    private DsmRequestParameters.Mode mode = DsmRequestParameters.Mode.OPEN;

    /**
     * the destination where to save the downloaded file
     */
    private String destinationPath;
    public DsmDownloadRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Download";
        this.version = 1;
        this.method = "download";
        this.path = "webapi/entry.cgi";
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmDownloadResponse>>() {};
    }

    public DsmDownloadRequest setFileToDownload(String filePath) {
        this.filePath = filePath;
        return this;
    }


    public DsmDownloadRequest setMode(DsmRequestParameters.Mode mode) {
        this.mode = mode;
        return this;
    }

    public DsmDownloadRequest setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
        return this;
    }

    @Override
    public Response<DsmDownloadResponse> call() {

        addParameter("path", escape(Optional.ofNullable(this.filePath).orElseThrow(() -> new DsmDownloadException("You have to add a folder or file to download"))));
        addParameter("mode", this.mode.name());

        try {
            HttpURLConnection conn = handleRequest(build());
            File downloadedFile = DsmUtils.downloadFile(conn.getInputStream(), Optional.ofNullable(this.destinationPath).orElseThrow(() -> new DsmDownloadException("You have to set a destination path"))+"/"+DsmUtils.extractFileName(this.filePath));

            Response<DsmDownloadResponse> response = new Response<>();
            response.setSuccess(true);
            response.setData(new DsmDownloadResponse(downloadedFile));
            return response;
        } catch (IOException e) {
            throw new DsmDownloadException("the file does not exist on the server");
        }
    }
}
