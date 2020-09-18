import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import exeptions.DsmUploadException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.fileStation.DsmRequestParameters;
import responses.Response;
import responses.fileStation.DsmListFolderResponse;
import responses.fileStation.DsmResponseFields;
import responses.fileStation.DsmUploadResponse;
import utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class DsmUploadTest {

    private final String ROOT_FOLDER = "/homes";
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    DsmAuth auth;

    @Before
    public void initTest() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void uploadFileSuccess() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success";
        File file = Utils.makeFile(folder, content, fileSuccess);
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmUploadResponse> response =  client.
                upload(ROOT_FOLDER, file.getAbsolutePath()).
                call();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData());
    }

    @Test
    public void uploadFileWithOverwriteSuccess() throws IOException {
        String fileSuccess = "dummy-upload-file.txt";
        String content = "success";
        File file = Utils.makeFile(folder, content, fileSuccess);
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        client.upload(ROOT_FOLDER, file.getAbsolutePath()).
                overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE).
                call();
        Response<DsmUploadResponse> response2 =
                client.upload(ROOT_FOLDER, file.getAbsolutePath())
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

        Assert.assertNotNull(response2);
        Assert.assertTrue(response2.isSuccess());
        Assert.assertNotNull(response2.getData());
    }

    @Test
    public void uploadFileWithCreateFolderIfNotExistSuccess() throws IOException {
        String fileSuccess = "dummy-upload-file.txt";
        String content = "success";
        File file = Utils.makeFile(folder, content, fileSuccess);
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmUploadResponse> response =  client.
                upload(ROOT_FOLDER+"/testResources", file.getAbsolutePath()).
                createParentFolders(true).
                overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData());
    }

    @Test(expected = DsmUploadException.class)
    public void uploadFileWithSourceFileNotExist() {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        File file = new File(fileSuccess);
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        client.upload(ROOT_FOLDER+"/test", file.getAbsolutePath()).call();
    }

    @Test
    public void uploadFileWithDatesShouldSuccess() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success";
        File file = Utils.makeFile(folder, content, fileSuccess);
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmUploadResponse> response =  client.
                upload(ROOT_FOLDER, file.getAbsolutePath())
                .createParentFolders(true)
                .setCreatedTime(LocalDateTime.of(2019,3,5,4,5,6,1))
                .setLastModifiedTime(LocalDateTime.of(2020,3,5,4,5,6,1))
                .setLastAccessedTime(LocalDateTime.of(2020,9,5,4,5,6,1))
                .call();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData());

        DsmResponseFields.Files dsmFiles = getFile(client, fileSuccess);

        Assert.assertEquals(dsmFiles.getAdditional().getTime().getAtime().longValue(), DateUtils.convertLocalDateTimeToUnixTimestamp(LocalDateTime.of(2020,9,5,4,5,6,1)) / 1000);
        Assert.assertEquals(dsmFiles.getAdditional().getTime().getCrtime().longValue(), DateUtils.convertLocalDateTimeToUnixTimestamp(LocalDateTime.of(2019,3,5,4,5,6,1)) / 1000);
        Assert.assertEquals(dsmFiles.getAdditional().getTime().getMtime().longValue(), DateUtils.convertLocalDateTimeToUnixTimestamp(LocalDateTime.of(2020,3,5,4,5,6,1)) / 1000);

    }

    private DsmResponseFields.Files getFile(DsmFileStationClient client , String fileName) {
        Response<DsmListFolderResponse> listFolderResponseResponse = client.ls(ROOT_FOLDER)
                .addAdditionalInfo(DsmRequestParameters.Additional.TIME)
                .call();
        Assert.assertNotNull(listFolderResponseResponse);
        Assert.assertTrue(listFolderResponseResponse.isSuccess());
        Assert.assertNotNull(listFolderResponseResponse.getData());

        Optional<DsmResponseFields.Files> filesOptional =listFolderResponseResponse.getData().getFiles().stream().filter(f -> f.getName().equals(fileName)).findAny();
        Assert.assertTrue(filesOptional.isPresent());

        return filesOptional.get();
    }
    
}
