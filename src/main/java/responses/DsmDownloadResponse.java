package responses;

import java.io.File;

public class DsmDownloadResponse {
    private final File file;

    public DsmDownloadResponse(File downloadedFile) {
        file = downloadedFile;
    }

    public File getFile() {
        return file;
    }
}
