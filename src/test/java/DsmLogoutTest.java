import requests.DsmAuth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DsmLogoutTest {

    DsmAuth auth;

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void logoutAnsSuccess() {
        DsmClient client = DsmClient.login(auth);
        Assert.assertTrue(client.logout());
    }
}
