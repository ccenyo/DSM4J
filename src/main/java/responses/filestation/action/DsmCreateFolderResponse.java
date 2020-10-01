package responses.filestation.action;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmCreateFolderResponse {

    private List<DsmResponseFields.Files> folders;

    public List<DsmResponseFields.Files> getFolders() {
        return folders;
    }
}
