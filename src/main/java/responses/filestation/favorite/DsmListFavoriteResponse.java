package responses.filestation.favorite;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmListFavoriteResponse {

    private Integer total;

    private Integer offset;

    private List<DsmResponseFields.Favorite> favorites;

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<DsmResponseFields.Favorite> getFavorites() {
        return favorites;
    }
}
