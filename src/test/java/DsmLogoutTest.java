import clients.DsmFileStationClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.DsmAuth;

import java.io.IOException;

public class DsmLogoutTest extends DsmTest{

    DsmAuth auth;

    @Before
    public void initTest() throws IOException {
        super.initTest();
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void logoutAnsSuccess() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);
        Assert.assertTrue(client.logout());
    }
}
