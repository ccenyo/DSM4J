import requests.DsmAuth;
import requests.DsmRequestParameters;
import responses.DsmListFolderResponse;
import responses.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DsmListFolderTest {
    DsmAuth auth;

    private static final String ROOT_HOME = "/homes";
    private static final String NOT_ROOT_HOME = "/NotExist";
    private static final String NOT_ROOT_HOME_ERROR_CODE = "408";

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void testDefaultFolderList() {
        DsmClient client = DsmClient.login(auth);

        Response<DsmListFolderResponse> response = client.ls(ROOT_HOME).call();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getError());
        Assert.assertNotNull(response.getData());
    }


    @Test
    public void testGetListFoldersSort() {
        DsmClient client = DsmClient.login(auth);

        Response<DsmListFolderResponse> response = client.ls(ROOT_HOME)
                .addSort(DsmRequestParameters.Sort.atime)
                .addSort(DsmRequestParameters.Sort.name)
                .setSortDirection(DsmRequestParameters.SortDirection.desc)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getFiles());
        Assert.assertTrue(response.getData().getFiles().size() !=0 );
        Assert.assertNull(response.getData().getFiles().get(0).getAdditional());
    }

    @Test
    public void testGetListFoldersAdditionalInformation() {
        DsmClient client = DsmClient.login(auth);

        Response<DsmListFolderResponse> response = client.ls(ROOT_HOME)
                .addAdditionalInfo(DsmRequestParameters.Additional.real_path)
                .addAdditionalInfo(DsmRequestParameters.Additional.owner)
                .addAdditionalInfo(DsmRequestParameters.Additional.mount_point_type)
                .addAdditionalInfo(DsmRequestParameters.Additional.perm)
                .addAdditionalInfo(DsmRequestParameters.Additional.time)
                .addAdditionalInfo(DsmRequestParameters.Additional.type)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getFiles());
        Assert.assertTrue(response.getData().getFiles().size() !=0 );
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getReal_path() == null));
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getOwner() == null));
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getMount_point_type() == null));
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getPerm() == null));
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getTime() == null));
        Assert.assertTrue(response.getData().getFiles().stream().noneMatch(r -> r.getAdditional().getType() == null));
    }

    @Test
    public void testGetListFoldersLimit() {
        DsmClient client = DsmClient.login(auth);

        Response<DsmListFolderResponse> response = client.ls(ROOT_HOME)
                .setLimit(2)
                .call();

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getData().getOffset());
        Assert.assertNotNull(response.getData().getTotal());
        Assert.assertNotNull(response.getData().getFiles());
        Assert.assertEquals(2, response.getData().getFiles().size());
    }

    @Test
    public void testGetListFolderRootNotExist() {
        DsmClient client = DsmClient.login(auth);

        Response<DsmListFolderResponse> response = client.ls(NOT_ROOT_HOME).call();

        Assert.assertNotNull(response);
        Assert.assertFalse(response.isSuccess());
        Assert.assertNull(response.getData());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(NOT_ROOT_HOME_ERROR_CODE, response.getError().getCode());

    }


}
