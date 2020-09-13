import exeptions.DsmLoginException;
import requests.*;
import responses.DsmLoginResponse;
import responses.DsmLogoutResponse;
import responses.Response;

import java.util.Optional;

public class DsmClient {

    private DsmAuth dsmAuth;

    public DsmClient(DsmAuth dsmAuth) {
        this.dsmAuth = dsmAuth;
    }

    public DsmAuth getDsmAuth() {
        return dsmAuth;
    }

    public static DsmClient login(DsmAuth auth) {
        Optional.ofNullable(auth).orElseThrow(() -> new DsmLoginException("DsmAuth can't be null"));

        Response<DsmLoginResponse> response = new DsmLoginRequest(auth).call();

        Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("An error occurred while trying to connect"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }
        auth = auth.setSid(response.getData().getSid());

        return new DsmClient(auth);
    }

    public boolean logout() {
        Optional.ofNullable(this.dsmAuth).orElseThrow(() -> new DsmLoginException("You are already logged out"));
        Response<DsmLogoutResponse> response = new DsmLogoutRequest(this.dsmAuth)
                .call();
        this.dsmAuth = null;
        Optional.ofNullable(response).orElseThrow(() -> new DsmLoginException("The request generates no response"));

        if(!response.isSuccess()) {
            throw new DsmLoginException(response.getError());
        }

        return response.isSuccess();
    }

    DsmSharedFolderRequest getAllSharedFolders() {
        return new DsmSharedFolderRequest(dsmAuth);
    }

    DsmListFolderRequest ls(String folderPath) {
        return new DsmListFolderRequest(dsmAuth)
                    .setFolderPath(folderPath);
    }
}
