package Requests;

public class DsmAuth {

    private String host;
    private Integer port;
    private String userName;
    private String password;

    public String getHost() {
        return host;
    }

    public DsmAuth setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public DsmAuth setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public DsmAuth setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DsmAuth setPassword(String password) {
        this.password = password;
        return this;
    }

    public static DsmAuth of(String host, Integer port, String userName, String password) {
        return new DsmAuth().setHost(host).setPort(port).setUserName(userName).setPassword(password);
    }
}
