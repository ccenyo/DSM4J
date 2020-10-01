package responses.filestation.lists;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmSharedFolderResponse {
    /**
     * Total number of shared folders.
     */
   Integer total;
    /**
     * Requested offset.
     */
   Integer offset;

   List<DsmResponseFields.Share> shares;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<DsmResponseFields.Share> getShares() {
        return shares;
    }
}
