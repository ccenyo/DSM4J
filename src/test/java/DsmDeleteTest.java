import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.DsmRequestParameters;
import responses.DsmSimpleDeleteResponse;
import responses.Response;

import java.io.File;
import java.io.IOException;

public class DsmDeleteTest {

    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmClient client;
    private File fileToDownload;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);

        fileToDownload = createNewFile();

        client = DsmClient.login(DsmAuth.fromResource("env.properties"));

        client.upload(ROOT_FOLDER, fileToDownload.getAbsolutePath())
                .createParentFolders(true)
                .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
                .call();

    }

    @Test
    public void deleteOneFileAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmSimpleDeleteResponse> deleteResponse = client.simpleDelete(ROOT_FOLDER+"/"+fileToDownload.getName())
                .call();

        Assert.assertTrue(deleteResponse.isSuccess());
        Assert.assertFalse(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));
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
