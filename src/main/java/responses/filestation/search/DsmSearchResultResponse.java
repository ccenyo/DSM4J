package responses.filestation.search;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmSearchResultResponse {

    /**
     * Total number of matched files.
     */
    private Integer total;
    /**
     * Requested offset.
     */
    private Integer offset;
    /**
     * If the searching task is finished or not.
     */
    private boolean finished;
    private List<DsmResponseFields.Files> files;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<DsmResponseFields.Files> getFiles() {
        return files;
    }
}
