package requests.filestation.favorite;

import com.fasterxml.jackson.core.type.TypeReference;
import exeptions.DsmFavoriteException;
import requests.DsmAbstractRequest;
import requests.DsmAuth;
import responses.Response;
import responses.filestation.DsmSimpleResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DsmManageFavoriteRequest extends DsmAbstractRequest<DsmSimpleResponse> {
    public DsmManageFavoriteRequest(DsmAuth auth) {
        super(auth);
        this.apiName = "SYNO.FileStation.Favorite";
        this.version = 1;
        this.path = "webapi/entry.cgi";
    }

    /**
     * A folder path starting with a shared folder
     * is added to the user’s favorites.
     *
     * One or more folder paths starting with
     * a shared folder, separated by a
     * comma is added to the user’s
     * favorites. The number of paths must
     * be the same as the number of favorite
     * names in the name parameter. The
     * first path parameter corresponds to
     * the first name parameter
     */
    private final List<String> paths = new LinkedList<>();

    /**
     * A favorite name.
     *
     * One or more new favrorite names,
     * separated by a comma. The
     * number of favorite names must be the
     * same as the number of folder paths in
     * the path parameter. The first name
     * parameter corresponding to the first
     * path parameter.
     */
    private final List<String> names = new LinkedList<>();

    /**
     * Optional. Index of location of an added
     * favorite. If it’s equal to -1, the favorite will
     * be added to the last one in user’s
     * favorite. If it’s between 0 ~ total number
     * of favorites-1, the favorite will be inserted
     * into user’s favorites by the index.
     */
    private Integer index;

    public DsmManageFavoriteRequest addPath(String path, String name) {
        this.paths.add(path);
        this.names.add(name);
        return this;
    }


    public DsmManageFavoriteRequest setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public Response<DsmSimpleResponse> add() {
        if(this.paths.isEmpty() || this.names.isEmpty()) {
            throw new DsmFavoriteException("You have to add path or names to make favorite");
        }

        this.method = "add";
        prepareDate();
        return super.call();
    }

    public Response<DsmSimpleResponse> delete() {
        if(this.paths.isEmpty()) {
            throw new DsmFavoriteException("You have to add path to delete favorite");
        }

        this.method = "delete";
        prepareDate();
        return super.call();
    }

    public Response<DsmSimpleResponse> clear() {
        this.method = "clear_broken";
        prepareDate();
        return super.call();
    }


    public Response<DsmSimpleResponse> edit() {
        if(this.paths.size() > 1) {
            throw new DsmFavoriteException("use replaceAll function to edit multiple favorites");
        }
        this.method = "edit";
        prepareDate();
        return super.call();
    }

    public Response<DsmSimpleResponse> replaceAll() {
        if(this.paths.isEmpty()) {
            throw new DsmFavoriteException("You have to add path to edit favorite");
        }
        this.method = "replace_all";
        prepareDate();
        return super.call();
    }

    private void prepareDate() {
        if(!names.isEmpty()) {
            addParameter("name", String.join(",", names));
        }
        if(!paths.isEmpty()) {
            addParameter("path", String.join(",", paths));
        }
        Optional.ofNullable(this.index).ifPresent(o -> addParameter("index", String.valueOf(o)));
    }

    @Override
    protected TypeReference getClassForMapper() {
        return new TypeReference<Response<DsmSimpleResponse>>() {};
    }
}
