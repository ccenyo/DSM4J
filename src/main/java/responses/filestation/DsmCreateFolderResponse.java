package responses.filestation;

import java.util.List;

public class DsmCreateFolderResponse {

    private List<DsmResponseFields.Files> folders;

    public List<DsmResponseFields.Files> getFolders() {
        return folders;
    }
}
