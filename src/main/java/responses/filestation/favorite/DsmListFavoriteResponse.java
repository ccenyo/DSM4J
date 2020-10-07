package responses.filestation.favorite;

import requests.filestation.DsmRequestParameters;

import java.util.List;

public class DsmListFavoriteResponse {

    private Integer total;

    private Integer offset;

    private List<DsmRequestParameters.Favorite> favorites;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<DsmRequestParameters.Favorite> getFavorites() {
        return favorites;
    }
}
