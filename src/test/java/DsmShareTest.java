import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import clients.DsmFileStationClient;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.DsmResponseFields;
import responses.filestation.DsmSimpleResponse;
import responses.filestation.share.DsmShareCreateResponse;
import responses.filestation.share.DsmShareListResponse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class DsmShareTest {


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
    public void shareOneFileAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .call();

        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0));
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getUrl());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getPath());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getQrcode());
    }

    @Test
    public void shareOneFileWithAvailableDateAndExpireDateAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .call();
        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0));
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getUrl());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getPath());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getQrcode());

        Response<DsmShareCreateResponse> editLinkResponse = client.editShareLink(createLinkResponse.getData().getLinks().get(0).getId())
                .setDateAvailable(LocalDateTime.now().plusDays(2))
                .setDateExpired(LocalDateTime.now().plusDays(3))
                .call();

        Assert.assertTrue(editLinkResponse.isSuccess());

        Response<DsmResponseFields.SharingLink> shareLinkResponse = client.getShareLinkInfo(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertTrue(shareLinkResponse.isSuccess());
        Assert.assertNotNull(shareLinkResponse.getData());
        Assert.assertNotNull(shareLinkResponse.getData().getDate_available());
        Assert.assertNotNull(shareLinkResponse.getData().getDate_expired());
        Assert.assertNotNull(shareLinkResponse.getData().getIsFolder());
        Assert.assertNotNull(shareLinkResponse.getData().getLink_owner());
        Assert.assertEquals(DsmResponseFields.SharingLink.Status.valid, shareLinkResponse.getData().getStatus());
        Assert.assertNotNull(shareLinkResponse.getData().getUrl());
        Assert.assertNotNull(shareLinkResponse.getData().getPath());
    }

    @Test
    public void shareOneFileWithPasswordAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .call();
        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0));
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getUrl());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getPath());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getQrcode());

        Response<DsmShareCreateResponse> editLinkResponse = client.editShareLink(createLinkResponse.getData().getLinks().get(0).getId())
                .setPassword("password")
                .call();

        Assert.assertTrue(editLinkResponse.isSuccess());

        Response<DsmResponseFields.SharingLink> shareLinkResponse = client.getShareLinkInfo(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertTrue(shareLinkResponse.isSuccess());
        Assert.assertNotNull(shareLinkResponse.getData());
        Assert.assertNotNull(shareLinkResponse.getData().getIsFolder());
        Assert.assertNotNull(shareLinkResponse.getData().getLink_owner());
        Assert.assertEquals(DsmResponseFields.SharingLink.Status.valid, shareLinkResponse.getData().getStatus());
        Assert.assertNotNull(shareLinkResponse.getData().getUrl());
        Assert.assertNotNull(shareLinkResponse.getData().getPath());
    }

    @Test
    public void shareMultipleOneFilesAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        client.createFolder(ROOT_FOLDER,"toBeShared").call();
        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared")
                .call();

        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertEquals(2, createLinkResponse.getData().getLinks().size());
    }


    @Test
    public void getAllShareLinksAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        client.createFolder(ROOT_FOLDER,"toBeShared").call();
        client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared")
                .call();

        Response<DsmShareListResponse>  shareListResponseResponse = client.getAllShareLinks().call();

        Assert.assertTrue(shareListResponseResponse.isSuccess());
        Assert.assertNotNull(shareListResponseResponse.getData());
        Assert.assertNotNull(shareListResponseResponse.getData().getLinks());
        Assert.assertTrue( shareListResponseResponse.getData().getLinks().size() != 0);
    }

    @Test
    public void getAllShareLinksLimitAndOffsetAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        client.createFolder(ROOT_FOLDER,"toBeShared").call();
        client.createFolder(ROOT_FOLDER,"toBeShared2").call();
        client.createFolder(ROOT_FOLDER,"toBeShared3").call();
        client.createFolder(ROOT_FOLDER,"toBeShared4").call();
        client.createFolder(ROOT_FOLDER,"toBeShared5").call();
        client.createFolder(ROOT_FOLDER,"toBeShared6").call();
        client.createFolder(ROOT_FOLDER,"toBeShared7").call();
        client.createFolder(ROOT_FOLDER,"toBeShared8").call();
        client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared2")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared3")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared4")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared5")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared6")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared7")
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared8")
                .call();

        Response<DsmShareListResponse>  shareListResponseResponse = client.getAllShareLinks()
                .setLimit(6)
                .setOffset(2)
                .setForceClean(true)
                .addSort(DsmRequestParameters.ShareSort.name)
                .setDirection(DsmRequestParameters.SortDirection.ASC)
                .call();

        Assert.assertTrue(shareListResponseResponse.isSuccess());
        Assert.assertNotNull(shareListResponseResponse.getData());
        Assert.assertNotNull(shareListResponseResponse.getData().getLinks());
        Assert.assertEquals(6, shareListResponseResponse.getData().getLinks().size());
    }


    @Test
    public void deleteShareLinkAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        client.createFolder(ROOT_FOLDER,"toBeShared").call();
        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared")
                .call();

        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0));
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getId());

        Response<DsmSimpleResponse>  dsmSimpleResponseResponse = client.deleteShareLink(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertTrue(dsmSimpleResponseResponse.isSuccess());


        Response<DsmResponseFields.SharingLink> shareLinkResponse = client.getShareLinkInfo(createLinkResponse.getData().getLinks().get(0).getId());
        Assert.assertTrue(shareLinkResponse.isSuccess());
        Assert.assertNull(shareLinkResponse.getData());
    }

    @Test
    public void clearInvalidShareLinkAndSuccess() {
        Assert.assertTrue(client.exists(ROOT_FOLDER+"/"+fileToDownload.getName()));

        client.createFolder(ROOT_FOLDER,"toBeShared").call();
        Response<DsmShareCreateResponse> createLinkResponse = client.createShareLink(ROOT_FOLDER+"/"+fileToDownload.getName())
                .addFileOrFolder(ROOT_FOLDER+"/toBeShared")
                .call();

        Assert.assertTrue(createLinkResponse.isSuccess());
        Assert.assertNotNull(createLinkResponse.getData());
        Assert.assertNotNull(createLinkResponse.getData().getLinks());
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0));
        Assert.assertNotNull(createLinkResponse.getData().getLinks().get(0).getId());

        Response<DsmSimpleResponse>  dsmSimpleResponseResponse = client.clearInvalidShareLinks();
        Assert.assertTrue(dsmSimpleResponseResponse.isSuccess());
    }

    private File createNewFile() throws IOException {
        String fileSuccess = "dummy-upload-file"+System.currentTimeMillis()+".txt";
        String content = "success content";

        return Utils.makeFile(folder, content, fileSuccess);
    }
}
