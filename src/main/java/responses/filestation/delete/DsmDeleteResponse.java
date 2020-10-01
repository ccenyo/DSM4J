package responses.filestation.delete;

public class DsmDeleteResponse {
    //start
    /**
     * A unique ID for the delete task which is
     * gotten from start method.
     */
    private String taskid;

    //status
    /**
     * If accurate_progress parameter is true the
     * number of all deleted files will be accumulated. If
     * false only the number of file you give in path
     * parameter is accumulated.
     */
    private Integer processed_num;
    /**
     * If accurate_progress parameter is true the
     * value indicates how many files including subfolders
     * will be deleted. If false it indicates how many files
     * you give in path parameter. When the total number
     * is calculating, the value is -1.
     */
    private Integer total;
    /**
     * A deletion path which you give in path parameter.
     */
    private String path;
    /**
     * A deletion path which could be located at a
     * subfolder.
     */
    private String processing_path;
    /**
     * Whether or not the deletion task is finished.
     */
    private boolean finished;
    /**
     * Progress value whose range between 0~1 is equal
     * to processed_num parameter divided by total
     * parameter
     */
    private Double progress;

    public String getTaskid() {
        return taskid;
    }

    public Integer getProcessed_num() {
        return processed_num;
    }

    public Integer getTotal() {
        return total;
    }

    public String getPath() {
        return path;
    }

    public String getProcessing_path() {
        return processing_path;
    }

    public boolean isFinished() {
        return finished;
    }

    public Double getProgress() {
        return progress;
    }
}
