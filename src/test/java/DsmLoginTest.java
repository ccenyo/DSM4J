import Exeptions.DsmLoginException;
import Requests.DsmAuth;
import org.junit.Assert;
import org.junit.Test;

public class DsmLoginTest {

    @Test
    public void testLoginFromResourceSuccess() {
        DsmClient client = DsmClient.login(DsmAuth.fromResource("env.properties"));

        Assert.assertNotNull(client);
        Assert.assertNotNull(client.getSID());
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginWithAuthNull() {
        DsmClient.login(null);
    }

    @Test(expected = DsmLoginException.class)
    public void testLoginAuthenticationFailed() {
        DsmClient.login(DsmAuth.fromResource("env-login-failed.properties"));

    }

}
