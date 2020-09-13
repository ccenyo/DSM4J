package requests;

import exeptions.DsmException;
import exeptions.DsmLoginException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DsmAuth {

    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String sid;

    public String getHost() {
        return host;
    }

    public DsmAuth setHost(String host) {
        this.host = host;
        return this;
    }

    public String getSid() {
        return sid;
    }

    public DsmAuth setSid(String sid) {
        this.sid = sid;
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
        DsmAuth dsmAuth = new DsmAuth()
                .setHost(Optional.ofNullable(host).orElseThrow(() -> new DsmLoginException("Unable to find property : host")))
                .setUserName(Optional.ofNullable(userName).orElseThrow(() -> new DsmLoginException("Unable to find property : userName")))
                .setPassword(Optional.ofNullable(password).orElseThrow(() -> new DsmLoginException("Unable to find property : password")));

        Optional.ofNullable(port).ifPresent(dsmAuth::setPort);
        return dsmAuth;
    }

    public static DsmAuth fromResource(String fileName) {
        try {
            Map<String, String> properties = getPropertiesFromResource(fileName);

            validate(properties);

            return DsmAuth.of(properties.get(HOST_KEY), properties.get(PORT_KEY) == null ? null : Integer.valueOf(properties.get(PORT_KEY)), properties.get(USERNAME_KEY), properties.get(PASSWORD_KEY));
        } catch (Exception exception) {
            throw new DsmException(exception);
        }
    }

    public static DsmAuth fromFile(File file) {

        try {
            Map<String, String> properties = getPropertiesFromFile(file);

            validate(properties);

            return DsmAuth.of(properties.get(HOST_KEY), Integer.valueOf(properties.get(PORT_KEY)), properties.get(USERNAME_KEY), properties.get(PASSWORD_KEY));
        } catch (Exception exception) {
            throw new DsmLoginException(exception);
        }
    }

    public static  Map<String, String> getPropertiesFromFile(File file) throws IOException {
        return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).stream()
                .filter(l -> !l.isEmpty())
                .collect(Collectors.
                        toMap(
                                l -> l.substring(0,l.indexOf('=')),
                                l -> l.substring(l.indexOf('=')+1)
                        )
                );
    }


    private static Map<String, String> getPropertiesFromResource(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = DsmAuth.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);

        File file = new File(Optional.ofNullable(resource).orElseThrow(() -> new IllegalArgumentException("file not found! " + fileName)).toURI());
        return getPropertiesFromFile(file);
    }

    private static void validate(Map<String, String> properties) {

        String host = properties.get(HOST_KEY) != null && !properties.get(HOST_KEY).isEmpty() ? properties.get(HOST_KEY) : null;
        Integer port = properties.get(PORT_KEY) != null && !properties.get(PORT_KEY).isEmpty() ? Integer.valueOf(Optional.ofNullable(properties.get(PORT_KEY)).orElse(null)) : null;
        String userName = properties.get(USERNAME_KEY) != null && !properties.get(USERNAME_KEY).isEmpty() ? properties.get(USERNAME_KEY) : null;
        String password = properties.get(PASSWORD_KEY) != null && !properties.get(PASSWORD_KEY).isEmpty() ? properties.get(PASSWORD_KEY) : null;

        if(host != null  && userName != null && password != null) {
            return;
        }

        throw new DsmLoginException("None of the properties can't be empty : host="+ host +", port="+ port + ", username=" +userName+ ", password="+password);
    }
}
