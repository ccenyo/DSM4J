import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import exeptions.DsmDownloadException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmDownloadResponse;

import java.io.File;
import java.io.IOException;

public class DsmDownloadTest {

    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;
    private File fileToDownload;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();


    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);

        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        fileToDownload = Utils.makeFile(folder, content, fileSuccess);
        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

        client.upload(ROOT_FOLDER, fileToDownload.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();
    }


    @Test
    public void downloadOneFileAndSuccess() {

       Response<DsmDownloadResponse> response = client.download(ROOT_FOLDER+"/"+fileToDownload.getName(), folder.getRoot().getAbsolutePath()).call();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData());
        Assert.assertNotNull(response.getData().getFile());
        Assert.assertTrue(response.getData().getFile().exists());
    }


    @Test(expected = DsmDownloadException.class)
    public void downloadWithoutDestinationPathAndFail() {
        client.download(ROOT_FOLDER+"/"+fileToDownload.getName(), null).call();
    }

    @Test(expected = DsmDownloadException.class)
    public void downloadWithoutSourcePathAndFail() {
        client.download(null, folder.getRoot().getAbsolutePath()).call();
    }

    @Test(expected = DsmDownloadException.class)
    public void downloadFileDoesNotExistShouldFail() {

        client.download(ROOT_FOLDER+"/file55252.txt", folder.getRoot().getAbsolutePath()).call();
    }

    @Test(expected = DsmDownloadException.class)
    public void downloadFileDestinationFolderDoesNotExistShouldFail() {
        client.download(ROOT_FOLDER+"/"+fileToDownload.getName(), "Z:\\").call();
    }
}
