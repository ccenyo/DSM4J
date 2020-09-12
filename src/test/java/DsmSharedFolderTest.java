import Requests.DsmAuth;
import Responses.DsmSharedFolderResponse;
import Responses.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DsmSharedFolderTest {
    DsmAuth auth;

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void testGetSharedFoldersAndSuccess() {
        DsmClient client = DsmClient.login(auth);

       Response<DsmSharedFolderResponse> response = client.getAllSharedFolders().call();

       Assert.assertTrue(response.isSuccess());
       Assert.assertNotNull(response.getData().getOffset());
       Assert.assertNotNull(response.getData().getTotal());
       Assert.assertNotNull(response.getData().getShares());
    }

}
