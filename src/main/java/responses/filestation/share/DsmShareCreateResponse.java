package responses.filestation.share;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmShareCreateResponse {

    List<DsmResponseFields.SharedLink> links;

    public List<DsmResponseFields.SharedLink> getLinks() {
        return links;
    }
}
