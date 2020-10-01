package clients;

import exeptions.DsmLoginException;
import requests.DsmAuth;
import requests.filestation.*;
import requests.filestation.share.DsmShareCreateOrEditRequest;
import requests.filestation.share.DsmShareDeleteRequest;
import requests.filestation.share.DsmShareInfoRequest;
import requests.filestation.share.DsmShareListRequest;
import responses.Response;
import responses.filestation.DsmLoginResponse;
import responses.filestation.DsmResponseFields;
import responses.filestation.DsmSimpleResponse;
import utils.DsmUtils;

import java.util.Optional;

public class DsmFileStationClient {

    private DsmAuth dsmAuth;

    public DsmFileStationClient(DsmAuth dsmAuth) {
        this.dsmAuth = dsmAuth;
    }

    public DsmAuth getDsmAuth() {
        return dsmAuth;
    }

    public static DsmFileStationClient login(DsmAuth auth) {
        Response<DsmLoginResponse> response = new DsmLoginRequest(Optional.ofNullable(auth).orElseThrow(() -> new DsmLoginException("DsmAuth can't be null"))).call();

        response = Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("An error occurred while trying to connect"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }
        auth = auth.setSid(response.getData().getSid());

        return new DsmFileStationClient(auth);
    }

    public boolean logout() {
        Response<DsmSimpleResponse> response = new DsmLogoutRequest(
                Optional.ofNullable(this.dsmAuth).orElseThrow(() -> new DsmLoginException("You are already logged out"))
        )
        .call();

        this.dsmAuth = null;
        response = Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("The request generates no response"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }

        return response.isSuccess();
    }

    public DsmSharedFolderRequest getAllSharedFolders() {
        return new DsmSharedFolderRequest(dsmAuth);
    }

    public DsmListFolderRequest ls(String folderPath) {
        return new DsmListFolderRequest(dsmAuth)
                    .setFolderPath(folderPath);
    }

    public DsmUploadRequest upload(String destinationPath, String filePath) {
        return new DsmUploadRequest(dsmAuth)
                .setDestinationFolderPath(destinationPath)
                .setFilePath(filePath);
    }

    public DsmDownloadRequest download(String fileOrFolderToDownload, String destinationPath) {
        return new DsmDownloadRequest(dsmAuth)
                .setFileToDownload(fileOrFolderToDownload)
                .setDestinationPath(destinationPath);

    }

    public DsmSimpleDeleteRequest simpleDelete(String filePath) {
        return new DsmSimpleDeleteRequest(dsmAuth)
                .addFileToDelete(filePath);
    }

    public DsmAdvancedDeleteRequest advancedDelete() {
        return new DsmAdvancedDeleteRequest(dsmAuth);
    }


    public boolean exists(String filePath) {
       return this.ls(DsmUtils.extractRootFolderPath(filePath))
               .call()
               .getData()
               .getFiles()
               .stream()
               .anyMatch(f -> f.getName().equals(DsmUtils.extractFileName(filePath)));
    }

    public DsmRenameRequest rename(String path, String newName) {
        return new DsmRenameRequest(dsmAuth).addFileOrFolderToRename(path).addNewNames(newName);
    }

    public DsmCopyMoveRequest copyOrMove(String pathToCopy, String destination) {
        return new DsmCopyMoveRequest(dsmAuth)
                .addPathToCopy(pathToCopy)
                .setDestinationFolderPath(destination);
    }

    public DsmCreateFolderRequest createFolder(String parentPath, String newFolderName) {
        return new DsmCreateFolderRequest(dsmAuth)
                .addNewFolder(parentPath, newFolderName);
    }

    public Response<DsmResponseFields.SharingLink> getShareLinkInfo(String id) {
        return new DsmShareInfoRequest(dsmAuth).getInfo(id);
    }

    public DsmShareListRequest getAllShareLinks() {
        return new DsmShareListRequest(dsmAuth);
    }

    public Response<DsmSimpleResponse> deleteShareLink(String id) {
        return new DsmShareDeleteRequest(dsmAuth).delete(id);
    }

    public Response<DsmSimpleResponse> clearInvalidShareLinks() {
        return new DsmShareDeleteRequest(dsmAuth).clearInvalidLinks();
    }

    public DsmShareCreateOrEditRequest editShareLink(String id) {
        return new DsmShareCreateOrEditRequest(dsmAuth)
                .setId(id);
    }

    public DsmShareCreateOrEditRequest createLink(String fileOrFilePath) {
        return new DsmShareCreateOrEditRequest(dsmAuth)
                .addFileOrFolder(fileOrFilePath);
    }
}
