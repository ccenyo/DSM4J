import clients.DsmFileStationClient;
import exeptions.DsmException;
import exeptions.DsmLoginException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.DsmAuth;

import java.io.IOException;

public class DsmLoginTest extends DsmTest{

    DsmAuth auth;

    @Before
    public void initTest() throws IOException {
        super.initTest();
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void testLoginFromResourceSuccess() {
        DsmFileStationClient client = DsmFileStationClient.login(auth);

        Assert.assertNotNull(client);
        Assert.assertNotNull(client.getDsmAuth());
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginWithAuthNull() {
        DsmFileStationClient.login(null);
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginAuthenticationWrongUserName() {
        DsmAuth dsmAuth = auth
                .setUserName("username")
                .setPassword("password");
        DsmFileStationClient.login(dsmAuth);
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginAuthenticationWrongPassword() {
        DsmAuth dsmAuth = auth
                .setPassword("password");
        DsmFileStationClient.login(dsmAuth);
    }

    @Test(expected = DsmException.class)
    public void testLoginAuthenticationWrongPort() {
        DsmAuth dsmAuth = auth
                .setPort(5444);
        DsmFileStationClient.login(dsmAuth);
    }

    @Test(expected = DsmException.class)
    public void testLoginAuthenticationWrongHost() {
        DsmAuth dsmAuth = auth
                .setHost("host");
        DsmFileStationClient.login(dsmAuth);
    }
}
