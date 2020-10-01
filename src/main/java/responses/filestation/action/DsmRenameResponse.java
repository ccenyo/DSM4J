package responses.filestation.action;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmRenameResponse {

    private List<DsmResponseFields.Files> files;

    public List<DsmResponseFields.Files> getFiles() {
        return files;
    }
}
