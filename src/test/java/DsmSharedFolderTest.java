import clients.DsmFileStationClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.DsmAuth;
import requests.filestation.DsmRequestParameters;
import responses.Response;
import responses.filestation.lists.DsmSharedFolderResponse;

public class DsmSharedFolderTest {
    DsmAuth auth;

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void testGetSharedFoldersAndSuccess() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

       Response<DsmSharedFolderResponse> response = client.getAllSharedFolders().call();

       Assert.assertTrue(response.isSuccess());
       Assert.assertNotNull(response.getData().getOffset());
       Assert.assertNotNull(response.getData().getTotal());
       Assert.assertNotNull(response.getData().getShares());
       Assert.assertTrue(response.getData().getShares().size() !=0 );
        Assert.assertNull(response.getData().getShares().get(0).getAdditional());
    }

    @Test
    public void testGetSharedFoldersSort() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmSharedFolderResponse> response = client.getAllSharedFolders()
                                                        .addSort(DsmRequestParameters.Sort.ATIME)
                                                        .addSort(DsmRequestParameters.Sort.NAME)
                                                        .setSortDirection(DsmRequestParameters.SortDirection.DESC)
                                                        .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getShares());
        Assert.assertTrue(response.getData().getShares().size() !=0 );
        Assert.assertNull(response.getData().getShares().get(0).getAdditional());
    }

    @Test
    public void testGetSharedFoldersAdditionalInformation() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmSharedFolderResponse> response = client.getAllSharedFolders()
                .addAdditionalInfo(DsmRequestParameters.Additional.REAL_PATH)
                .addAdditionalInfo(DsmRequestParameters.Additional.OWNER)
                .addAdditionalInfo(DsmRequestParameters.Additional.MOUNT_POINT_TYPE)
                .addAdditionalInfo(DsmRequestParameters.Additional.PERM)
                .addAdditionalInfo(DsmRequestParameters.Additional.TIME)
                .addAdditionalInfo(DsmRequestParameters.Additional.VOLUME_STATUS)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getShares());
        Assert.assertTrue(response.getData().getShares().size() !=0 );
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getReal_path() == null));
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getOwner() == null));
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getMount_point_type() == null));
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getPerm() == null));
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getTime() == null));
        Assert.assertTrue(response.getData().getShares().stream().noneMatch(r -> r.getAdditional().getVolume_status() == null));
    }

    @Test
    public void testGetSharedFoldersLimit() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmSharedFolderResponse> response = client.getAllSharedFolders()
                .setLimit(2)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getShares());
        Assert.assertEquals(2, response.getData().getShares().size());
    }

    @Test
    public void testGetSharedFoldersOnlyWritable() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Response<DsmSharedFolderResponse> response = client.getAllSharedFolders()
                .setOnlyWritable(true)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getShares());
    }

}
