import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmCopyMoveRequest;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmCopyMoveResponse;

import java.io.File;
import java.io.IOException;

public class DsmCopyMoveTest {

    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;
    private File fileToUpload;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);

        fileToUpload = createNewFile();

        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

        client.upload(ROOT_FOLDER, fileToUpload.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
    }

    @After
    public void postTest() {
        client.simpleDelete(ROOT_FOLDER).setRecursive(true).call();
    }

    @Test
    public void copyFileToNewDestination() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();
        
        Response<DsmCopyMoveResponse> copyOrMoveResponse = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .start();

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/copied/"+ fileToUpload.getName()));

        Assert.assertTrue(copyOrMoveResponse.isSuccess());
        Assert.assertNotNull(copyOrMoveResponse.getData());
    }

    @Test
    public void copyFileToNewDestinationFileAlreadyExist() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();
        client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .start();
        Response<DsmCopyMoveResponse> copyOrMoveResponse = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .start();

        Assert.assertFalse(copyOrMoveResponse.isSuccess());
        Assert.assertNull(copyOrMoveResponse.getData());
        Assert.assertNotNull(copyOrMoveResponse.getError());
        Assert.assertEquals("1000", copyOrMoveResponse.getError().getCode());
        Assert.assertNotNull(copyOrMoveResponse.getError().getErrors());
        Assert.assertEquals("1003", copyOrMoveResponse.getError().getErrors().get(0).getCode());
    }

    @Test
    public void copyFileToNewDestinationFileAlreadyExistOverwrite() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();
        client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .start();
        Response<DsmCopyMoveResponse> copyOrMoveResponse = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .setOverwriteBehaviour(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .start();

        Assert.assertTrue(copyOrMoveResponse.isSuccess());
        Assert.assertNotNull(copyOrMoveResponse.getData());
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/copied/"+ fileToUpload.getName()));
    }

    @Test
    public void copyFileToNewDestinationFileAlreadyExistSkip() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();
        client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .start();
        Response<DsmCopyMoveResponse> copyOrMoveResponse = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied")
                .setOverwriteBehaviour(DsmRequestParameters.OverwriteBehaviour.SKIP)
                .start();

        Assert.assertTrue(copyOrMoveResponse.isSuccess());
        Assert.assertNotNull(copyOrMoveResponse.getData());
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/copied/"+ fileToUpload.getName()));
    }

    @Test
    public void copyFileToNewDestinationStatus() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();

        DsmCopyMoveRequest dsmCopyMoveRequest = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied");

        Response<DsmCopyMoveResponse> copyOrMoveResponse =   dsmCopyMoveRequest.start();

        Assert.assertTrue(copyOrMoveResponse.isSuccess());
        Assert.assertNotNull(copyOrMoveResponse.getData());
        Assert.assertNotNull(copyOrMoveResponse.getData().getTaskid());

        dsmCopyMoveRequest.setTaskId(copyOrMoveResponse.getData().getTaskid());

        Response<DsmCopyMoveResponse> copyOrMoveStatus = dsmCopyMoveRequest.status();
        Assert.assertTrue(copyOrMoveStatus.isSuccess());
        Assert.assertNotNull(copyOrMoveStatus.getData());
        Assert.assertNotNull(copyOrMoveStatus.getData().getDest_folder_path());
        Assert.assertNotNull(copyOrMoveStatus.getData().getFinished());
        Assert.assertNotNull(copyOrMoveStatus.getData().getProcessed_size());
        Assert.assertNotNull(copyOrMoveStatus.getData().getProgress());
        Assert.assertNotNull(copyOrMoveStatus.getData().getTotal());

        Response<DsmCopyMoveResponse> copyOrMoveStop = dsmCopyMoveRequest.stop();
        Assert.assertTrue(copyOrMoveStop.isSuccess());

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/copied/"+ fileToUpload.getName()));
    }

    @Test
    public void moveFileToNewDestinationStatus() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.createFolder(ROOT_FOLDER, "copied").call();

        DsmCopyMoveRequest dsmCopyMoveRequest = client.copyOrMove(ROOT_FOLDER+"/"+ fileToUpload.getName(), ROOT_FOLDER+"/copied");

        Response<DsmCopyMoveResponse> copyOrMoveResponse =   dsmCopyMoveRequest.setRemoveSrc(true).start();

        Assert.assertTrue(copyOrMoveResponse.isSuccess());
        Assert.assertNotNull(copyOrMoveResponse.getData());
        Assert.assertNotNull(copyOrMoveResponse.getData().getTaskid());

        dsmCopyMoveRequest.setTaskId(copyOrMoveResponse.getData().getTaskid());

        Response<DsmCopyMoveResponse> copyOrMoveStatus = dsmCopyMoveRequest.status();
        Assert.assertTrue(copyOrMoveStatus.isSuccess());
        Assert.assertNotNull(copyOrMoveStatus.getData());
        Assert.assertNotNull(copyOrMoveStatus.getData().getDest_folder_path());
        Assert.assertNotNull(copyOrMoveStatus.getData().getFinished());
        Assert.assertNotNull(copyOrMoveStatus.getData().getProcessed_size());
        Assert.assertNotNull(copyOrMoveStatus.getData().getProgress());
        Assert.assertNotNull(copyOrMoveStatus.getData().getTotal());

        Response<DsmCopyMoveResponse> copyOrMoveStop = dsmCopyMoveRequest.stop();
        Assert.assertTrue(copyOrMoveStop.isSuccess());

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/copied/"+ fileToUpload.getName()));
        Assert.assertFalse(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));
    }

    private File createNewFile() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        return Utils.makeFile(folder, content, fileSuccess);
    }
}
