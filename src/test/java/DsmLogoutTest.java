import clients.DsmFileStationClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.DsmAuth;

public class DsmLogoutTest {

    DsmAuth auth;

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void logoutAnsSuccess() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);
        Assert.assertTrue(client.logout());
    }
}
