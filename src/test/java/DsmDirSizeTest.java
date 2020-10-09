import clients.DsmFileStationClient;
import exeptions.DsmDirSizeException;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import requests.DsmAuth;
import requests.filestation.action.DsmDirSizeRequest;
import responses.Response;
import responses.filestation.action.DsmDirSizeResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DsmDirSizeTest extends DsmTest{

    private final String ROOT_FOLDER = "/homes/testResource";
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    public DsmFileStationClient client;

    public List<String> fileNames = new ArrayList<>();

    @Before
    public void initTest() throws IOException {
        super.initTest();
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
    public void getMainDirSizeAndSuccess() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER);
       Response<DsmDirSizeResponse> dirSizeResponse = dsmDirSizeRequest.start();
        Assert.assertNotNull(dirSizeResponse);
        Assert.assertTrue(dirSizeResponse.isSuccess());
        Assert.assertNotNull(dirSizeResponse.getData());
        Assert.assertNotNull(dirSizeResponse.getData().getTaskid());
        Assert.assertNull(dirSizeResponse.getData().getNum_dir());
        Assert.assertNull(dirSizeResponse.getData().getNum_file());
        Assert.assertNull(dirSizeResponse.getData().getTotal_size());

        Response<DsmDirSizeResponse> resultResponse;
        do {
            resultResponse = dsmDirSizeRequest.status(dirSizeResponse.getData().getTaskid());
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertNotNull(resultResponse.getData().getNum_dir());
        Assert.assertNotNull(resultResponse.getData().getNum_file());
        Assert.assertNotNull(resultResponse.getData().getTotal_size());
    }

    @Test
    public void getMultipleAndFolderFileSizeAndSuccess() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER+"/"+fileNames.get(0))
                .addPath(ROOT_FOLDER)
                .addPath(ROOT_FOLDER+"/"+fileNames.get(1));
        Response<DsmDirSizeResponse> dirSizeResponse = dsmDirSizeRequest.start();
        Assert.assertNotNull(dirSizeResponse);
        Assert.assertTrue(dirSizeResponse.isSuccess());
        Assert.assertNotNull(dirSizeResponse.getData());
        Assert.assertNotNull(dirSizeResponse.getData().getTaskid());
        Assert.assertNull(dirSizeResponse.getData().getNum_dir());
        Assert.assertNull(dirSizeResponse.getData().getNum_file());
        Assert.assertNull(dirSizeResponse.getData().getTotal_size());

        Response<DsmDirSizeResponse> resultResponse;
        do {
            resultResponse = dsmDirSizeRequest.status(dirSizeResponse.getData().getTaskid());
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertEquals(Long.valueOf(1), resultResponse.getData().getNum_dir());
        Assert.assertEquals(Long.valueOf(32), resultResponse.getData().getNum_file());
        Assert.assertEquals(Long.valueOf(288), resultResponse.getData().getTotal_size());
    }

    @Test
    public void getFileSizeAndSuccess() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER+"/"+fileNames.get(0));
        Response<DsmDirSizeResponse> dirSizeResponse = dsmDirSizeRequest.start();
        Assert.assertNotNull(dirSizeResponse);
        Assert.assertTrue(dirSizeResponse.isSuccess());
        Assert.assertNotNull(dirSizeResponse.getData());
        Assert.assertNotNull(dirSizeResponse.getData().getTaskid());
        Assert.assertNull(dirSizeResponse.getData().getNum_dir());
        Assert.assertNull(dirSizeResponse.getData().getNum_file());
        Assert.assertNull(dirSizeResponse.getData().getTotal_size());

        Response<DsmDirSizeResponse> resultResponse;
        do {
            resultResponse = dsmDirSizeRequest.status(dirSizeResponse.getData().getTaskid());
        } while (!resultResponse.getData().isFinished());

        Assert.assertNotNull(resultResponse);
        Assert.assertTrue(resultResponse.isSuccess());
        Assert.assertNotNull(resultResponse.getData());
        Assert.assertEquals(Long.valueOf(0), resultResponse.getData().getNum_dir());
        Assert.assertEquals(Long.valueOf(1), resultResponse.getData().getNum_file());
        Assert.assertEquals(Long.valueOf(9), resultResponse.getData().getTotal_size());
    }

    @Test
    public void stopGetFileSizeAndSuccess() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER+"/"+fileNames.get(0));
        Response<DsmDirSizeResponse> dirSizeResponse = dsmDirSizeRequest.start();
        Assert.assertNotNull(dirSizeResponse);
        Assert.assertTrue(dirSizeResponse.isSuccess());
        Assert.assertNotNull(dirSizeResponse.getData());
        Assert.assertNotNull(dirSizeResponse.getData().getTaskid());
        Assert.assertNull(dirSizeResponse.getData().getNum_dir());
        Assert.assertNull(dirSizeResponse.getData().getNum_file());
        Assert.assertNull(dirSizeResponse.getData().getTotal_size());

        Response<DsmDirSizeResponse> stopResponse = dsmDirSizeRequest.stop(dirSizeResponse.getData().getTaskid());

        Assert.assertNotNull(stopResponse);
        Assert.assertTrue(stopResponse.isSuccess());
    }

    @Test(expected = DsmDirSizeException.class)
    public void stopWithoutTaskId() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER+"/"+fileNames.get(0));
        dsmDirSizeRequest.stop();
    }

    @Test(expected = DsmDirSizeException.class)
    public void statusWithoutTaskId() {
        DsmDirSizeRequest dsmDirSizeRequest = client.getSize(ROOT_FOLDER+"/"+fileNames.get(0));
        dsmDirSizeRequest.status();
    }
}
