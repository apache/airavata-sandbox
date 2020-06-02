package com.scheduler;

public class job {

    private int jobID;
    private int arivalTime;
    private int wallTimeRequested;
    private int nodesRequested;


    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getArivalTime() {
        return arivalTime;
    }

    public void setArivalTime(int arivalTime) {
        this.arivalTime = arivalTime;
    }

    public int getWallTimeRequested() {
        return wallTimeRequested;
    }

    public void setWallTimeRequested(int wallTimeRequested) {
        this.wallTimeRequested = wallTimeRequested;
    }

    public int getNodesRequested() {
        return nodesRequested;
    }

    public void setNodesRequested(int nodesRequested) {
        this.nodesRequested = nodesRequested;
    }

    public job(int jobID,  int wallTimeRequested, int nodesRequested, int arivalTime) {
        this.jobID = jobID;
        this.arivalTime = arivalTime;
        this.wallTimeRequested = wallTimeRequested;
        this.nodesRequested = nodesRequested;
    }

    public void printJob(String from){
        System.out.println(from+" "+jobID+"\t"+wallTimeRequested+"\t"+nodesRequested+"\t"+arivalTime);
    }


}
