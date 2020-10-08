package clients;

import exeptions.DsmLoginException;
import requests.DsmAuth;
import requests.filestation.action.DsmCopyMoveRequest;
import requests.filestation.action.DsmCreateFolderRequest;
import requests.filestation.action.DsmDirSizeRequest;
import requests.filestation.action.DsmRenameRequest;
import requests.filestation.delete.DsmAdvancedDeleteRequest;
import requests.filestation.delete.DsmSimpleDeleteRequest;
import requests.filestation.favorite.DsmListFavoriteRequest;
import requests.filestation.favorite.DsmManageFavoriteRequest;
import requests.filestation.lists.DsmListFolderRequest;
import requests.filestation.lists.DsmSharedFolderRequest;
import requests.filestation.login.DsmLoginRequest;
import requests.filestation.login.DsmLogoutRequest;
import requests.filestation.search.DsmSearchResultRequest;
import requests.filestation.search.DsmSearchStartRequest;
import requests.filestation.search.DsmSearchStopRequest;
import requests.filestation.share.DsmShareCreateOrEditRequest;
import requests.filestation.share.DsmShareDeleteRequest;
import requests.filestation.share.DsmShareInfoRequest;
import requests.filestation.share.DsmShareListRequest;
import requests.filestation.transfert.DsmDownloadRequest;
import requests.filestation.transfert.DsmUploadRequest;
import responses.Response;
import responses.filestation.login.DsmLoginResponse;
import responses.filestation.DsmResponseFields;
import responses.filestation.DsmSimpleResponse;
import utils.DsmUtils;

import java.util.Optional;

/**
 * this is the principal client of fileStation
 * all the functions are specified here
 */
public class DsmFileStationClient {

    private DsmAuth dsmAuth;

    public DsmFileStationClient(DsmAuth dsmAuth) {
        this.dsmAuth = dsmAuth;
    }

    public DsmAuth getDsmAuth() {
        return dsmAuth;
    }

    /**
     * login to the server
     * @param auth information about the server and user to connect
     * @return DsmFileStationClient
     */
    public static DsmFileStationClient login(DsmAuth auth) {
        Response<DsmLoginResponse> response = new DsmLoginRequest(Optional.ofNullable(auth).orElseThrow(() -> new DsmLoginException("DsmAuth can't be null"))).call();

        response = Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("An error occurred while trying to connect"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }
        auth = auth.setSid(response.getData().getSid());

        return new DsmFileStationClient(auth);
    }

    /**
     * logout from server
     * @return boolean
     */
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

    /**
     * get the list of the files and folder in a folder
     * @param folderPath the path to the folder
     * @return DsmListFolderRequest
     */
    public DsmListFolderRequest ls(String folderPath) {
        return new DsmListFolderRequest(dsmAuth)
                    .setFolderPath(folderPath);
    }

    /**
     * upload a file to the server
     * @param destinationPath the destination path
     * @param filePath the file to upload
     * @return DsmUploadRequest
     */
    public DsmUploadRequest upload(String destinationPath, String filePath) {
        return new DsmUploadRequest(dsmAuth)
                .setDestinationFolderPath(destinationPath)
                .setFilePath(filePath);
    }

    /**
     * download a file
     * @param fileOrFolderToDownload the path of the file to download
     * @param destinationPath the destination path
     * @return DsmDownloadRequest
     */
    public DsmDownloadRequest download(String fileOrFolderToDownload, String destinationPath) {
        return new DsmDownloadRequest(dsmAuth)
                .setFileToDownload(fileOrFolderToDownload)
                .setDestinationPath(destinationPath);
    }

    /**
     *
     * @param filePath the path of the file to delete
     * @return DsmSimpleDeleteRequest
     */
    public DsmSimpleDeleteRequest simpleDelete(String filePath) {
        return new DsmSimpleDeleteRequest(dsmAuth)
                .addFileToDelete(filePath);
    }

    /**
     * delete folder of file asynchroniously
     * @return DsmAdvancedDeleteRequest
     */
    public DsmAdvancedDeleteRequest advancedDelete() {
        return new DsmAdvancedDeleteRequest(dsmAuth);
    }


    /**
     * check if the file or folder exits
     * @param filePath the path of the file or folder
     * @return boolean
     */
    public boolean exists(String filePath) {
       return this.ls(DsmUtils.extractRootFolderPath(filePath))
               .call()
               .getData()
               .getFiles()
               .stream()
               .anyMatch(f -> f.getName().equals(DsmUtils.extractFileName(filePath)));
    }

    /**
     * rename a file or folder
     * @param path the path of the folder or dile
     * @param newName the new name of the folder or file
     * @return DsmRenameRequest
     */
    public DsmRenameRequest rename(String path, String newName) {
        return new DsmRenameRequest(dsmAuth).addFileOrFolderToRename(path).addNewNames(newName);
    }

    /**
     * copy or move a file or folder
     * @param pathToCopy the path of the file or folder to move or copy
     * @param destination the destination where to copy or move the file
     * @return DsmCopyMoveRequest
     */
    public DsmCopyMoveRequest copyOrMove(String pathToCopy, String destination) {
        return new DsmCopyMoveRequest(dsmAuth)
                .addPathToCopy(pathToCopy)
                .setDestinationFolderPath(destination);
    }

    /**
     * create a new folder
     * @param parentPath the path of the parent folder
     * @param newFolderName the name of the new folder
     * @return DsmCreateFolderRequest
     */
    public DsmCreateFolderRequest createFolder(String parentPath, String newFolderName) {
        return new DsmCreateFolderRequest(dsmAuth)
                .addNewFolder(parentPath, newFolderName);
    }

    /**
     * get the information about the share links
     * @param id the id of the shared link
     * @return DsmResponseFields.SharingLink
     */
    public Response<DsmResponseFields.SharingLink> getShareLinkInfo(String id) {
        return new DsmShareInfoRequest(dsmAuth).getInfo(id);
    }

    /**
     * get all shared links
     * @return DsmShareListRequest
     */
    public DsmShareListRequest getAllShareLinks() {
        return new DsmShareListRequest(dsmAuth);
    }

    /**
     * delete a shared link
     * @param id the id of the share link to delete
     * @return DsmSimpleResponse
     */
    public Response<DsmSimpleResponse> deleteShareLink(String id) {
        return new DsmShareDeleteRequest(dsmAuth).delete(id);
    }

    /**
     * clear invalid links
     * @return DsmSimpleResponse
     */
    public Response<DsmSimpleResponse> clearInvalidShareLinks() {
        return new DsmShareDeleteRequest(dsmAuth).clearInvalidLinks();
    }

    /**
     * edit the shared link
     * @param id the id of the shared link to edit
     * @return DsmShareCreateOrEditRequest
     */
    public DsmShareCreateOrEditRequest editShareLink(String id) {
        return new DsmShareCreateOrEditRequest(dsmAuth)
                .setId(id);
    }

    /**
     * create a new shared link
     * @param fileOrFilePath the path of the file or folder to share
     * @return DsmShareCreateOrEditRequest
     */
    public DsmShareCreateOrEditRequest createShareLink(String fileOrFilePath) {
        return new DsmShareCreateOrEditRequest(dsmAuth)
                .addFileOrFolder(fileOrFilePath);
    }

    /**
     * Search for file or directory
     *
     * @param fileName the file or directoru to search
     * @return DsmSearchStartRequest
     */
    public DsmSearchStartRequest startSearch(String fileName) {
        return new DsmSearchStartRequest(dsmAuth)
                .addPattern(fileName);
    }

    /**
     * get the search result
     *
     * @param id the search task id
     * @return DsmSearchResultRequest
     */
    public DsmSearchResultRequest getSearchResult(String id) {
        return new DsmSearchResultRequest(dsmAuth)
                .setTaskId(id);
    }

    /**
     * stop the search task
     * @param id the it of the search task
     * @return DsmSearchStopRequest
     */
    public DsmSearchStopRequest stopSearch(String id) {
        return new DsmSearchStopRequest(dsmAuth).addTaskId(id);
    }

    /**
     * @param path the path of file or folder for favorite
     * @param name the favorite name
     * @return DsmManageFavoriteRequest
     */
    public DsmManageFavoriteRequest favoriteManager(String path, String name) {
        return new DsmManageFavoriteRequest(dsmAuth)
                .addPath(path, name);
    }

    /**
     * get favorite manager
     * @return DsmManageFavoriteRequest
     */
    public DsmManageFavoriteRequest favoriteManager() {
        return new DsmManageFavoriteRequest(dsmAuth);
    }

    /**
     * the list of all favorite
     * @return DsmListFavoriteRequest
     */
    public DsmListFavoriteRequest listFavorite() {
        return new DsmListFavoriteRequest(dsmAuth);
    }

    /**
     * get the size of directory or file
     * @param path the path the file or folder
     * @return DsmDirSizeRequest
     */
    public DsmDirSizeRequest getSize(String path) {
        return new DsmDirSizeRequest(dsmAuth)
                .addPath(path);
    }
}
