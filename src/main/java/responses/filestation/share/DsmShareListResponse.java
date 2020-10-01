package responses.filestation.share;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmShareListResponse {

    private Integer total;
    private Integer offset;
    private List<DsmResponseFields.SharingLink> links;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<DsmResponseFields.SharingLink> getLinks() {
        return links;
    }
}
