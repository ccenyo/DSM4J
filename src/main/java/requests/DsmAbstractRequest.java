package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exeptions.DsmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import responses.Response;
import utils.JacksonFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

public abstract class DsmAbstractRequest<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DsmAbstractRequest.class.getName());

    private static final ObjectMapper mapper = JacksonFactory.getMapper();

    protected DsmAuth auth;


    private HashMap<String, String> params = new HashMap<>();

    public DsmAbstractRequest(DsmAuth auth) {
        this.auth = auth;
    }

    public  abstract String getAPIName();

    public abstract Integer getVersion();

    public abstract String getPath();

    public abstract String getMethod();

    protected String build() {
        addParameter("format", "sid");
        StringBuilder request = new StringBuilder();
        request.append(auth.getHost())
                .append(auth.getPort() == null? "" : ":"+auth.getPort())
                .append("/")
                .append(getPath())
                .append("?")
                .append("api=")
                .append(getAPIName())
                .append("&")
                .append("version=")
                .append(getVersion())
                .append("&")
                .append("method=")
                .append(getMethod());

        Optional.ofNullable(auth.getSid()).ifPresent( sid -> request.append("&").append("_sid").append("=").append(sid));

        params
                .forEach((key, value) -> request.append("&")
                        .append(key)
                        .append("=")
                        .append(value));

        return request.toString();
    }

    protected String escape(String source) {
       source = source.replace(",", "\\");
       source = source.replace("\\", "\\\\");
       source = source.replace("/", "%2F");
       return source;
    }

    protected Response<T> deserialize(String resp) throws JsonProcessingException {
        return (Response<T>) mapper.readValue(resp , getClassForMapper());
    }

    protected abstract TypeReference<T> getClassForMapper();


    public Response<T> call() {
        try {

            String url = build();
            HttpURLConnection conn = handleRequest(url);
            int responseCode = conn.getResponseCode();

            LOG.info("POST Response Code : {}", responseCode );
            LOG.info("POST Response Message : {}", conn.getResponseMessage() );

            StringBuilder respBuf = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                respBuf.append(line);
            }

            String resp = respBuf.toString();
            LOG.trace("Response received: {}", resp);

            return deserialize(resp);
        }
        catch (IOException e) {
            throw new DsmException(e);
        }
    }

    private HttpURLConnection handleRequest(String url) throws IOException {
        LOG.debug("Calling URL: {}", url);
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();

        conn.setRequestMethod("GET");

        return conn;
    }

    protected void addParameter(String key, String value) {
        params.put(key, value);
    }
}
