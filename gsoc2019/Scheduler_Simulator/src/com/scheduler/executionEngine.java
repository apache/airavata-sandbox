package com.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class executionEngine extends Thread {

    private clusterSimulator clusterObj;
    private List<executingJob> executingJobsList;


    public executionEngine(clusterSimulator obj){

        this.clusterObj = obj;
        this.executingJobsList = new ArrayList<>();

    }


    public void sortExecutingJobs(){
        executingJobsList.sort((job1, job2) -> {
            return job1.getTimeToCompletion() == job2.getTimeToCompletion() ? 0 :
                    job1.getTimeToCompletion() > job2.getTimeToCompletion() ? 1 : -1;
        });
    }

    public void run() {

        Boolean isNewEvent = false;

        //when resources are occupied and the last job was not placed in execution
        //so we try again
        Boolean lastEventIsPending = false;

        job newJob = null;

        while(true) {
            synchronized (this.clusterObj.sharedState) {
                if (!lastEventIsPending && this.clusterObj.sharedState.eventsPresent > 0) {
                    isNewEvent = true;
                    this.clusterObj.sharedState.eventsPresent--;
                    newJob = this.clusterObj.removeJob();
                    this.clusterObj.sharedState.jobsInQueue--;
                    System.out.println("Executor: removed a job: ");
                    newJob.printJob("executor");
                }
            }


            if(isNewEvent && newJob != null) {


                if(this.clusterObj.getNumberOfNodesFree() >= newJob.getNodesRequested()){

                    //puts in to the execution array
                    //sort the array on time to completion
                    System.out.println("Executor: put job in exection: with ID "+ newJob.getJobID());
                    //decrement the resources
                    this.clusterObj.decrementNumberOfNodesFree(newJob.getNodesRequested());

                    long m = System.currentTimeMillis()/1000;
                    executingJob exJob = new executingJob(newJob.getJobID(),newJob.getWallTimeRequested()+m,
                            newJob.getNodesRequested());

                    executingJobsList.add(exJob);

                    this.sortExecutingJobs();


                    newJob = null;
                    isNewEvent = false;

                    //this event is successful
                    lastEventIsPending = false;

                }else {
                    //if not successful then
                    System.out.println("Out of Nodes: need "+newJob.getNodesRequested()+ " have "+this.clusterObj.getNumberOfNodesFree());
                    lastEventIsPending = true;
                }


            }

            //check to see if any job has completed?
            //System.out.println("Executor: Checking completion of any jobs: ");
            //if any job completed then remove that job from executingJobs arary and put signal into a file

            Iterator itr = executingJobsList.iterator();
            while (itr.hasNext())
            {
                long currentTime = System.currentTimeMillis()/1000;
                executingJob x = (executingJob) itr.next();

                if (x.getTimeToCompletion() < currentTime) {
                    //remove job
                    this.clusterObj.incrementNumberOfNodesFree(x.getNodes());
                    itr.remove();
                    //signal the file or the Kafka queue
                }
            }


            if(!executingJobsList.isEmpty()) {
                executingJobsList.forEach(job1 -> {
                    System.out.print(job1.getJobID() + " ");
                });
                System.out.println("");
            }

            //System.out.println("Modified ArrayList : "
            //        + executingJobsList);



        }


    }


}
