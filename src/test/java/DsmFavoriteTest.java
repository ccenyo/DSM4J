import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import exeptions.DsmFavoriteException;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmSimpleResponse;
import responses.filestation.favorite.DsmListFavoriteResponse;

import java.io.File;
import java.io.IOException;

public class DsmFavoriteTest {
    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;
    private File uploadedFile;
    private File uploadedFile2;
    private String favorite;
    private String favorite2;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
        favorite = "favorite"+System.currentTimeMillis();
        favorite2 = "favorite"+System.currentTimeMillis();

        uploadedFile = createNewFile();
        uploadedFile2 = createNewFile();

        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

        client.upload(ROOT_FOLDER, uploadedFile.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

        client.upload(ROOT_FOLDER, uploadedFile2.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
    }

    @After
    public void postTest() {
        client.simpleDelete(ROOT_FOLDER).setRecursive(true).call();
        client.favoriteManager().clear();
    }


    @Test
    public void addFavoriteToFile() {
        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite).add();
        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        Response<DsmListFavoriteResponse> dsmListFavoriteResponse = client.listFavorite()
                .addAdditionalInformation(DsmRequestParameters.Additional.OWNER).call();
        Assert.assertNotNull(dsmListFavoriteResponse);
        Assert.assertTrue(dsmListFavoriteResponse.isSuccess());
        Assert.assertNotNull(dsmListFavoriteResponse.getData());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites().get(0).getAdditional());
        Assert.assertTrue( dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite)));
    }

    @Test
    public void addMultipleFavoritesToFile() {
        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite)
                .addPath(ROOT_FOLDER+"/"+uploadedFile2.getName(), favorite2).add();
        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        Response<DsmListFavoriteResponse> dsmListFavoriteResponse = client.listFavorite().call();
        Assert.assertNotNull(dsmListFavoriteResponse);
        Assert.assertTrue(dsmListFavoriteResponse.isSuccess());
        Assert.assertNotNull(dsmListFavoriteResponse.getData());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites());
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite)));
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(favorite -> favorite.getName().equals(favorite2)));
    }

    @Test
    public void deleteFavoriteToFile() {
        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite).add();

        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        Response<DsmSimpleResponse> dsmSimpleResponse2 = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite).delete();
        Assert.assertNotNull(dsmSimpleResponse2);
        Assert.assertTrue(dsmSimpleResponse2.isSuccess());

        Response<DsmListFavoriteResponse> dsmListFavoriteResponse = client.listFavorite()
                .addAdditionalInformation(DsmRequestParameters.Additional.OWNER).call();
        Assert.assertNotNull(dsmListFavoriteResponse);
        Assert.assertTrue(dsmListFavoriteResponse.isSuccess());
        Assert.assertNotNull(dsmListFavoriteResponse.getData());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites());
        Assert.assertFalse(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite)));
    }


    @Test
    public void editFavoriteToFile() {

        String favorite3 = "favorite"+System.currentTimeMillis();
        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite).add();

        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        Response<DsmSimpleResponse> dsmSimpleResponse2 = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite3).edit();
        Assert.assertNotNull(dsmSimpleResponse2);
        Assert.assertTrue(dsmSimpleResponse2.isSuccess());

        Response<DsmListFavoriteResponse> dsmListFavoriteResponse = client.listFavorite().call();
        Assert.assertNotNull(dsmListFavoriteResponse);
        Assert.assertTrue(dsmListFavoriteResponse.isSuccess());
        Assert.assertNotNull(dsmListFavoriteResponse.getData());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites());
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().noneMatch(f -> f.getName().equals(favorite)));
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite3)));
    }

    @Test
    public void replaceAllFavoriteToFile() {

        String favorite3 = "favorite"+System.currentTimeMillis();
        String favorite4 = "favorite2"+System.currentTimeMillis();
        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite)
                .addPath(ROOT_FOLDER+"/"+uploadedFile2.getName(), favorite2)
                .add();

        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        Response<DsmSimpleResponse> dsmSimpleResponse2 = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite3)
                .addPath(ROOT_FOLDER+"/"+uploadedFile2.getName(), favorite4)
                .replaceAll();
        Assert.assertNotNull(dsmSimpleResponse2);
        Assert.assertTrue(dsmSimpleResponse2.isSuccess());

        Response<DsmListFavoriteResponse> dsmListFavoriteResponse = client.listFavorite().call();
        Assert.assertNotNull(dsmListFavoriteResponse);
        Assert.assertTrue(dsmListFavoriteResponse.isSuccess());
        Assert.assertNotNull(dsmListFavoriteResponse.getData());
        Assert.assertNotNull(dsmListFavoriteResponse.getData().getFavorites());
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().noneMatch(f -> f.getName().equals(favorite)));
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite3)));
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().noneMatch(f -> f.getName().equals(favorite2)));
        Assert.assertTrue(dsmListFavoriteResponse.getData().getFavorites().stream().anyMatch(f -> f.getName().equals(favorite4)));
    }

    @Test(expected = DsmFavoriteException.class)
    public void editFavoriteToFileWithNoPath() {

        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite)
                .addPath(ROOT_FOLDER+"/"+uploadedFile2.getName(), favorite2)
                .add();

        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        client.favoriteManager()
                .replaceAll();
    }

    @Test(expected = DsmFavoriteException.class)
    public void deleteFavoriteToFileWithNoPath() {

        Response<DsmSimpleResponse> dsmSimpleResponse = client.favoriteManager(ROOT_FOLDER+"/"+uploadedFile.getName(), favorite)
                .add();

        Assert.assertNotNull(dsmSimpleResponse);
        Assert.assertTrue(dsmSimpleResponse.isSuccess());

        client.favoriteManager()
                .delete();
    }

    private File createNewFile() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        return Utils.makeFile(folder, content, fileSuccess);
    }
}
