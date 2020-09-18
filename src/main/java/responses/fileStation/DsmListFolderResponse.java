package responses.fileStation;

import java.util.List;

public class DsmListFolderResponse {
    private Integer total;
    private Integer offset;
    private List<DsmResponseFields.Files> files;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<DsmResponseFields.Files> getFiles() {
        return files;
    }
}
