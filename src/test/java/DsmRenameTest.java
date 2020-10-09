import clients.DsmFileStationClient;
import exeptions.DsmRenameException;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.action.DsmRenameResponse;

import java.io.File;
import java.io.IOException;

public class DsmRenameTest extends DsmTest{
    private final String ROOT_FOLDER = "/homes/testResource";
    private DsmFileStationClient client;
    private File fileToUpload;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void initTest() throws IOException {
        super.initTest();
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
    public void renameFileAndSuccess() {
        String newNameFile = "newNameFile1.txt";
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        Response<DsmRenameResponse> renameResponse = client.rename(ROOT_FOLDER+"/"+ fileToUpload.getName(), newNameFile)
                .call();

        Assert.assertTrue(renameResponse.isSuccess());
        Assert.assertNotNull(renameResponse.getData());
        Assert.assertNotNull(renameResponse.getData().getFiles());
        Assert.assertEquals(1, renameResponse.getData().getFiles().size());
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ newNameFile));
    }

    @Test
    public void renameFileWithAdditionalAndSuccess() {
        String newNameFile = "newNameFile1.txt";
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        Response<DsmRenameResponse> renameResponse = client.rename(ROOT_FOLDER+"/"+ fileToUpload.getName(), newNameFile)
                .addAdditionalInfo(DsmRequestParameters.Additional.OWNER)
                .addAdditionalInfo(DsmRequestParameters.Additional.REAL_PATH)
                .call();

        Assert.assertTrue(renameResponse.isSuccess());
        Assert.assertNotNull(renameResponse.getData());
        Assert.assertNotNull(renameResponse.getData().getFiles());
        Assert.assertEquals(1, renameResponse.getData().getFiles().size());
        Assert.assertNotNull(renameResponse.getData().getFiles().get(0));
        Assert.assertNotNull(renameResponse.getData().getFiles().get(0).getAdditional());
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ newNameFile));
    }


    @Test
    public void renameMultipleFilesAndSuccess() throws IOException {
        String newNameFile = "newNameFile1.txt";
        String newNameFile2 = "newNameFile2.txt";

        File file = Utils.makeFile(folder, "success", "dummy-upload-file2.txt");

         client.upload(ROOT_FOLDER, file.getAbsolutePath())
        .createParentFolders(true)
        .overwrite(DsmRequestParameters.OverwriteBehaviour.OVERWRITE)
        .call();

        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ file.getName()));

        Response<DsmRenameResponse> renameResponse = client.rename(ROOT_FOLDER+"/"+ fileToUpload.getName(), newNameFile)
                .addFileOrFolderToRename(ROOT_FOLDER+"/"+ file.getName())
                .addNewNames(newNameFile2)
                .call();

        Assert.assertTrue(renameResponse.isSuccess());
        Assert.assertNotNull(renameResponse.getData());
        Assert.assertNotNull(renameResponse.getData().getFiles());
        Assert.assertEquals(2, renameResponse.getData().getFiles().size());
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ newNameFile));
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ newNameFile2));
    }

    @Test(expected = DsmRenameException.class)
    public void renameMultipleWithNoName() {
        String newNameFile = "newNameFile1.txt";
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+ fileToUpload.getName()));

        client.rename(ROOT_FOLDER+"/"+ fileToUpload.getName(), newNameFile)
                .addFileOrFolderToRename(ROOT_FOLDER+"/"+ fileToUpload.getName())
        .call();
    }

    private File createNewFile() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        return Utils.makeFile(folder, content, fileSuccess);
    }
}
