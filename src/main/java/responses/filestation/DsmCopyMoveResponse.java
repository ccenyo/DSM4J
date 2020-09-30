package responses.filestation;

public class DsmCopyMoveResponse {

    //start
    /**
     * A unique ID for the delete task which is
     * gotten from start method.
     */
    private String taskid;

    /**
     * If accurate_progress parameter is true, byte
     * sizes of all copied/moved files will be accumulated. If
     * false, only byte sizes of the file you give in path
     * parameter is accumulated.
     */
    private Integer processed_size;

    /**
     * If accurate_progress parameter is true, the
     * value indicates total byte sizes of files including
     * subfolders will be copied/moved. If false, it
     * indicates total byte sizes of files you give in path
     * parameter excluding files within subfolders.
     * Otherwise, when the total number is calculating,
     * the value is -1.
     */
    private Integer total;

    /**
     * A copying/moving path which you give in path
     * parameter.
     */
    private String path;

    /**
     * If the copy/move task is finished or not.
     */
    private Boolean finished;

    /**
     * A progress value is between 0~1. It is equal to
     * processed_size parameter divided by total
     * parameter.
     */
    private Double progress;

    /**
     * A desitination folder path where files/folders are
     * copied/moved.
     */
    private String dest_folder_path;

    public String getTaskId() {
        return taskid;
    }

    public Integer getProcessed_size() {
        return processed_size;
    }

    public Integer getTotal() {
        return total;
    }

    public String getPath() {
        return path;
    }

    public Boolean getFinished() {
        return finished;
    }

    public Double getProgress() {
        return progress;
    }

    public String getDest_folder_path() {
        return dest_folder_path;
    }
}
