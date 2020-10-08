import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmSimpleResponse;
import responses.filestation.search.DsmSearchResultResponse;
import responses.filestation.search.DsmSearchStartResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DsmSearchTest {

    private final String ROOT_FOLDER = "/homes/testResource";
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    public DsmFileStationClient client;

    public List<String> fileNames = new ArrayList<>();

    @Before
    public void initTest() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
        client = DsmFileStationClient.login(DsmAuth.fromResource("env.properties"));

        for(int i =0; i < 30; i++) {
            String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
            String content = "success";
            File file = Utils.makeFile(folder, content, fileSuccess);


            client.upload(ROOT_FOLDER, file.getAbsolutePath())
                    .createParentFolders(true)
                    .call();

            fileNames.add(fileSuccess);
        }
    }

    @After
    public void postTest() {
        client.simpleDelete(ROOT_FOLDER).setRecursive(true).call();
    }


    @Test
    public void searchOneFileAndSuccess(){
        Random random = new Random();
        String selectedFileName = fileNames.get(random.nextInt(fileNames.size() - 1));

        Response<DsmSearchStartResponse> startResponse = client.startSearch(selectedFileName)
                .setRootFolderPath(ROOT_FOLDER)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(Integer.valueOf(1), resultResponse.getData().getTotal());
    }

    @Test
    public void searchAllFilesAndSuccess(){
        Response<DsmSearchStartResponse> startResponse = client.startSearch("*.txt")
                .setRootFolderPath(ROOT_FOLDER)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(Integer.valueOf(30), resultResponse.getData().getTotal());
    }

    @Test
    public void searchFileWithRecursiveAndSuccess(){
        Random random = new Random();
        String selectedFileName = fileNames.get(random.nextInt(fileNames.size() - 1));

        Response<DsmSearchStartResponse> startResponse = client.startSearch(selectedFileName)
                .setRootFolderPath(ROOT_FOLDER)
                .setFileType(DsmRequestParameters.FileType.FILE)
                .setRecursive(true)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(Integer.valueOf(1), resultResponse.getData().getTotal());
    }


    @Test
    public void searchMultipleFoldersAndSuccess(){
        Response<DsmSearchStartResponse> startResponse = client.startSearch("")
                .setRootFolderPath(ROOT_FOLDER)
                .setFileType(DsmRequestParameters.FileType.DIR)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(Integer.valueOf(0), resultResponse.getData().getTotal());
    }

    @Test
    public void searchMultipleFilesWithExtensionAndSuccess(){
        Response<DsmSearchStartResponse> startResponse = client.startSearch("")
                .setRootFolderPath(ROOT_FOLDER)
                .setExtension("txt")
                .setFileType(DsmRequestParameters.FileType.FILE)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());
        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(Integer.valueOf(30), resultResponse.getData().getTotal());
    }

    @Test
    public void searchOneFileWithAdditionalsAndSuccess(){
        Response<DsmSearchStartResponse> startResponse = client.startSearch("")
                .setRootFolderPath(ROOT_FOLDER)
                .setExtension("txt")
                .setFileType(DsmRequestParameters.FileType.FILE)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSearchResultResponse> resultResponse;
        do {
            resultResponse = client.getSearchResult(startResponse.getData().getTaskid())
                    .addAdditionalInformation(DsmRequestParameters.Additional.OWNER)
                    .addSorts(DsmRequestParameters.Sort.NAME)
                    .setLimit(20)
                    .setOffset(3)
                    .call();
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getFiles());
        Assert.assertEquals(20, resultResponse.getData().getFiles().size());
        Assert.assertEquals( Integer.valueOf(3), resultResponse.getData().getOffset());
        Assert.assertNotNull(resultResponse.getData().getFiles().get(0).getAdditional());
    }

    @Test
    public void searchMultipleFilesAnsStopAndSuccess(){
        Response<DsmSearchStartResponse> startResponse = client.startSearch("")
                .setRootFolderPath(ROOT_FOLDER)
                .setExtension("txt")
                .setFileType(DsmRequestParameters.FileType.FILE)
                .call();

        Assert.assertNotNull(startResponse);
        Assert.assertTrue(startResponse.isSuccess());
        Assert.assertNotNull(startResponse.getData());
        Assert.assertNotNull(startResponse.getData().getTaskid());

        Response<DsmSimpleResponse> stopResponse= client.stopSearch(startResponse.getData().getTaskid()).call();

        Assert.assertNotNull(stopResponse);
        Assert.assertTrue(stopResponse.isSuccess());
    }
}
