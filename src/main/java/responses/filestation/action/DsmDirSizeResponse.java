package responses.filestation.action;

public class DsmDirSizeResponse {

    private String taskid;

    /**
     * If the task is finished or not.
     */
    private boolean finished;
    /**
     * Number of directories in the queried path(s)
     */
    private Long num_dir;
    /**
     * Number of files in the queried path(s).
     */
    private Long num_file;
    /**
     * Accumulated byte size of the queried path(s).
     */
    private Long total_size;

    public String getTaskid() {
        return taskid;
    }

    public boolean isFinished() {
        return finished;
    }

    public Long getNum_dir() {
        return num_dir;
    }

    public Long getNum_file() {
        return num_file;
    }

    public Long getTotal_size() {
        return total_size;
    }
}
