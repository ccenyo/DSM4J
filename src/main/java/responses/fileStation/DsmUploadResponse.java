package responses.fileStation;

public class DsmUploadResponse {
    private boolean blSki;
    private String file;
    private Long pid;
    protected Long progress;

    public boolean isBlSki() {
        return blSki;
    }

    public String getFile() {
        return file;
    }

    public Long getPid() {
        return pid;
    }

    public Long getProgress() {
        return progress;
    }
}
