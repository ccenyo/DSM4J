import Exeptions.DsmException;
import Exeptions.DsmLoginException;
import Requests.DsmAuth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DsmLoginTest {

    DsmAuth auth;

    @Before
    public void initTest() {
        auth = DsmAuth.fromResource("env.properties");
    }

    @Test
    public void testLoginFromResourceSuccess() {
        DsmClient client = DsmClient.login(auth);

        Assert.assertNotNull(client);
        Assert.assertNotNull(client.getSID());
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginWithAuthNull() {
        DsmClient.login(null);
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginAuthenticationWrongUserName() {
        DsmAuth dsmAuth = auth
                .setUserName("username")
                .setPassword("password");
        DsmClient.login(dsmAuth);
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginAuthenticationWrongPassword() {
        DsmAuth dsmAuth = auth
                .setPassword("password");
        DsmClient.login(dsmAuth);
    }

    @Test(expected = DsmException.class)
    public void testLoginAuthenticationWrongPort() {
        DsmAuth dsmAuth = auth
                .setPort(5444);
        DsmClient.login(dsmAuth);
    }

    @Test(expected = DsmException.class)
    public void testLoginAuthenticationWrongHost() {
        DsmAuth dsmAuth = auth
                .setHost("host");
        DsmClient.login(dsmAuth);
    }
}
