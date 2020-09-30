import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmCreateFolderResponse;

public class DsmCreateFolderTest {


    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);

        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

    }

    @After
    public void postTest() {
        client.simpleDelete(ROOT_FOLDER).setRecursive(true).call();
    }

    @Test
    public void createNewFolderForceParentAndSuccess() {

        Response<DsmCreateFolderResponse> createFolderResponse = client.createFolder(ROOT_FOLDER, "newFolder")
                .forceCreateParentFolder(true)
                .call();

        Assert.assertTrue(createFolderResponse.isSuccess());
        Assert.assertNotNull(createFolderResponse.getData());
        Assert.assertNotNull(createFolderResponse.getData().getFolders());
        Assert.assertEquals(1, createFolderResponse.getData().getFolders().size());
    }

    @Test
    public void createNewFolderAndSuccess() {
        client.createFolder(ROOT_FOLDER, "newFolder")
                .forceCreateParentFolder(true)
                .call();

        Response<DsmCreateFolderResponse> createFolderResponse = client.createFolder(ROOT_FOLDER, "newFolder2")
                .call();

        Assert.assertTrue(createFolderResponse.isSuccess());
        Assert.assertNotNull(createFolderResponse.getData());
        Assert.assertNotNull(createFolderResponse.getData().getFolders());
        Assert.assertEquals(1, createFolderResponse.getData().getFolders().size());
    }

    @Test
    public void createNewFolderAndWithAdditionalsSuccess() {

        Response<DsmCreateFolderResponse> createFolderResponse = client.createFolder(ROOT_FOLDER, "newFolder")
                .forceCreateParentFolder(true)
                .addAdditional(DsmRequestParameters.Additional.OWNER)
                .call();

        Assert.assertTrue(createFolderResponse.isSuccess());
        Assert.assertNotNull(createFolderResponse.getData());
        Assert.assertNotNull(createFolderResponse.getData().getFolders());
        Assert.assertEquals(1, createFolderResponse.getData().getFolders().size());
        Assert.assertNotNull(createFolderResponse.getData().getFolders().get(0).getAdditional());
    }

    @Test
    public void createMultipleNewFoldersAndSuccess() {

        Response<DsmCreateFolderResponse> createFolderResponse = client.createFolder(ROOT_FOLDER, "newFolder")
                .addNewFolder(ROOT_FOLDER,"newFolder2")
                .addNewFolder(ROOT_FOLDER,"newFolder3")
                .forceCreateParentFolder(true)
                .call();

        Assert.assertTrue(createFolderResponse.isSuccess());
        Assert.assertNotNull(createFolderResponse.getData());
        Assert.assertNotNull(createFolderResponse.getData().getFolders());
        Assert.assertEquals(3, createFolderResponse.getData().getFolders().size());
    }

    @Test
    public void createNewFolderParentNotExistAndFailed() {

        Response<DsmCreateFolderResponse> createFolderResponse = client.createFolder(ROOT_FOLDER, "newFolder")
                .call();

        Assert.assertFalse(createFolderResponse.isSuccess());
        Assert.assertNull(createFolderResponse.getData());
        Assert.assertNotNull(createFolderResponse.getError());
        Assert.assertEquals("1100", createFolderResponse.getError().getCode());
        Assert.assertEquals("408", createFolderResponse.getError().getErrors().get(0).getCode());
    }

}
