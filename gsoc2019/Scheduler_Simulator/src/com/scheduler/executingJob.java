package com.scheduler;

public class executingJob {

    private int jobID;
    private long timeToCompletion;
    private int nodes;


    public executingJob(int ID, long time, int nodes){
        this.jobID = ID;
        this.timeToCompletion = time;
        this.nodes = nodes;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public long getTimeToCompletion() {
        return timeToCompletion;
    }

    public void setTimeToCompletion(long timeToCompletion) {
        this.timeToCompletion = timeToCompletion;
    }
}
