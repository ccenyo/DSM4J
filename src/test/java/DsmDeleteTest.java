import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import exeptions.DsmDeleteException;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmDeleteResponse;
import utils.DsmUtils;

import java.io.File;
import java.io.IOException;

public class DsmDeleteTest {

    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;
    private File fileToDownload;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);

        fileToDownload = createNewFile();

        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

        client.upload(ROOT_FOLDER, fileToDownload.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
    }

    @After
    public void postTest() {
        client.simpleDelete(ROOT_FOLDER).setRecursive(true).call();
    }

    @Test
    public void deleteOneFileAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmDeleteResponse> deleteResponse = client.simpleDelete(ROOT_FOLDER+"/"+fileToDownload.getName())
                .call();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertFalse(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));
    }

    @Test
    public void deleteAdvancedOneFileAndSuccess() {

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmDeleteResponse> deleteResponse = client.advancedDelete()
                .addFileToDelete(ROOT_FOLDER+"/"+fileToDownload.getName())
                .start();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertNotNull(deleteResponse.getData());
        Assert.assertNotNull(deleteResponse.getData().getTaskid());

        Response<DsmDeleteResponse> statusResponse = client.advancedDelete()
                .taskId(deleteResponse.getData().getTaskid())
                .status();

        Assert.assertTrue(statusResponse.isSuccess());
        Assert.assertNotNull(statusResponse.getData());
        Assert.assertNotNull(statusResponse.getData().getPath());
        Assert.assertNotNull(statusResponse.getData().getProcessed_num());
        Assert.assertNotNull(statusResponse.getData().getProcessing_path());
        Assert.assertNotNull(statusResponse.getData().getTotal());
        Assert.assertNotNull(statusResponse.getData().getProgress());
    }

    @Test
    public void deleteFileNotExistAndFail() throws IOException {
        File fileToUpload = createNewFile();

        Response<DsmDeleteResponse> deleteResponse = client.advancedDelete()
                .addFileToDelete(ROOT_FOLDER+"/"+fileToUpload.getName())
                .start();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertNotNull(deleteResponse.getData());
        Assert.assertNotNull(deleteResponse.getData().getTaskid());

        Response<DsmDeleteResponse> statusResponse = client.advancedDelete()
                .taskId(deleteResponse.getData().getTaskid())
                .status();

        Assert.assertTrue(statusResponse.isSuccess());
        Assert.assertNotNull(statusResponse.getData());
        Assert.assertEquals("",statusResponse.getData().getPath());
        Assert.assertNotNull(statusResponse.getData().getProcessed_num());
        Assert.assertEquals("",statusResponse.getData().getProcessing_path());
        Assert.assertEquals(Integer.valueOf("-1"), statusResponse.getData().getTotal());
        Assert.assertNotNull(statusResponse.getData().getProgress());
    }

    @Test(expected = DsmDeleteException.class)
    public void deleteFileNotSetAndFail() {
        client.advancedDelete()
                .start();
    }

    @Test(expected = DsmDeleteException.class)
    public void getStatusWithoutTakId() {
                client.advancedDelete()
                .status();
    }

    @Test(expected = DsmDeleteException.class)
    public void getStopWithoutTakId() {
        client.advancedDelete()
                .status();
    }

    @Test
    public void getStatusWithDummyTaskId() {
        Response<DsmDeleteResponse> statusResponse = client.advancedDelete()
                .taskId("TaskId")
                .status();

        Assert.assertFalse(statusResponse.isSuccess());
        Assert.assertNull(statusResponse.getData());
        Assert.assertNotNull(statusResponse.getError());
        Assert.assertNotNull(statusResponse.getError().getCode());
        Assert.assertEquals("No such task of the file operation", DsmUtils.manageErrorMessage(Integer.valueOf(statusResponse.getError().getCode())));
    }

    @Test
    public void deleteAdvancedMultipleFilesAndSuccess() throws IOException {
        File fileToDownload2 = createNewFile();
        client.upload(ROOT_FOLDER, fileToDownload2.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload2.getName()));

        Response<DsmDeleteResponse> deleteResponse = client.advancedDelete()
                .addFileToDelete(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileToDelete(ROOT_FOLDER+"/"+fileToDownload2.getName())
                .start();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertNotNull(deleteResponse.getData());
        Assert.assertNotNull(deleteResponse.getData().getTaskid());

        Response<DsmDeleteResponse> statusResponse = client.advancedDelete()
                .taskId(deleteResponse.getData().getTaskid())
                .status();

        Assert.assertTrue(statusResponse.isSuccess());
        Assert.assertNotNull(statusResponse.getData());
        Assert.assertNotNull(statusResponse.getData().getPath());
        Assert.assertNotNull(statusResponse.getData().getProcessed_num());
        Assert.assertNotNull(statusResponse.getData().getProcessing_path());
        Assert.assertNotNull(statusResponse.getData().getTotal());
        Assert.assertNotNull(statusResponse.getData().getProgress());
    }

    @Test
    public void deleteFolderAndSuccess() throws IOException {
        File fileToUpload = createNewFile();
        File fileToUpload2 = createNewFile();
        File fileToUpload3 = createNewFile();
        client.upload(ROOT_FOLDER+"/folder1", fileToUpload.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
        client.upload(ROOT_FOLDER+"/folder1", fileToUpload2.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
        client.upload(ROOT_FOLDER+"/folder1", fileToUpload3.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/folder1/"+fileToUpload.getName()));
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/folder1/"+fileToUpload2.getName()));
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/folder1/"+fileToUpload3.getName()));

        Response<DsmDeleteResponse> deleteResponse = client.advancedDelete()
                .addFileToDelete(ROOT_FOLDER+"/folder1")
                .start();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertNotNull(deleteResponse.getData());
        Assert.assertNotNull(deleteResponse.getData().getTaskid());

        Response<DsmDeleteResponse> statusResponse = client.advancedDelete()
                .taskId(deleteResponse.getData().getTaskid())
                .status();

        Assert.assertTrue(statusResponse.isSuccess());
        Assert.assertNotNull(statusResponse.getData());
        Assert.assertNotNull(statusResponse.getData().getPath());
        Assert.assertNotNull(statusResponse.getData().getProcessed_num());
        Assert.assertNotNull(statusResponse.getData().getProcessing_path());
        Assert.assertNotNull(statusResponse.getData().getTotal());
        Assert.assertNotNull(statusResponse.getData().getProgress());
    }

    @Test
    public void checkFileExist() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));
    }

    @Test
    public void checkFileNotExist() {
        Assert.assertFalse(client.exists(ROOT_FOLDER+"/not"+fileToDownload.getName()));
    }

    private File createNewFile() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        return Utils.makeFile(folder, content, fileSuccess);
    }
}
