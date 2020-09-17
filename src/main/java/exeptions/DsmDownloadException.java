package exeptions;

public class DsmDownloadException extends DsmException{
    public DsmDownloadException(String errorMessage) {
        super(errorMessage);
    }

    public DsmDownloadException(Exception e) {
        super(e);
    }
}
