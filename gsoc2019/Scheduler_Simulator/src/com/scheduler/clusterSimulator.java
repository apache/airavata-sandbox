package com.scheduler;


// this class is a discrete event simulator for physcial machine such as a
// cluster that accepts jobs and puts them in the queue and executes them


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


import java.net.*;
import java.io.*;

public class clusterSimulator {


    //jobs queue
    private int globalQueueLimit;
    private int perUserQueueLimit;

    //resources
    private int numberOfNodes;

    private int numberOfNodesFree;


    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public int getNumberOfNodesFree() {
        return numberOfNodesFree;
    }

    public void decrementNumberOfNodesFree(int x) {
        this.numberOfNodesFree -= x;
    }


    public void incrementNumberOfNodesFree(int x) {
        this.numberOfNodesFree += x;
    }

    class Shared {
        public int eventsPresent;

        // for now we are just dealing with a single user case
        // so jobsInQueue is not for all users but simple for a single user
        // we assume that there is only one user Airavata for the cluster
        // and this user has a limit on the queue that is defined by perUserQueueLimit
        // we also ignore globalQueueLimit for now
        public int jobsInQueue;

        public Queue<job> jobsQueue;

        public Shared(){
            this.eventsPresent=0;
            this.jobsQueue = new LinkedList<job>();
            this.jobsInQueue = 0;
        }
    }
    public Shared sharedState;
    private executionEngine executionThread;

    public clusterSimulator(int globalQueueLimit, int perUserQueueLimit, int numberOfNodes) {
        this.globalQueueLimit = globalQueueLimit;
        this.perUserQueueLimit = perUserQueueLimit;
        this.numberOfNodes = numberOfNodes;

        //initially all nodes are free
        this.numberOfNodesFree = this.numberOfNodes;

        sharedState = new Shared();

        this.executionThread = new executionEngine(this);
        this.executionThread.start();

    }

    public void startConnection(String ip, int port) {
        try {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e){
            System.out.println("Exception occured: "+ e);
        }
    }

    //TODO see what this is?
    public void stopConnection() {
        try {
        in.close();
        out.close();
        clientSocket.close();
        } catch (Exception e){
            System.out.println("Exception occured: "+ e);
        }
    }


    public String sendMessage(String msg) {
        String resp = null;
        try {
            out.println(msg);
            resp = in.readLine();

        } catch (Exception e){
            System.out.println("Exception occured: "+ e);
        }
        return resp;
    }


    public String recvMessage() {
        String resp = null;
        try {
            resp = in.readLine();
        } catch (Exception e){
            System.out.println("recvMessage: Exception occured: "+ e);
        }
        return resp;
    }


    /* TODO: learn how to do unit tests, looks like assert needs path to unit test...
    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        clusterSimulator client = new clusterSimulator(500,50);
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        assertEquals("hello client", response);
    }*/

    public void startCluster(){


        // 4 fields id, walltime, nodes, arrivalTime
        int fields[] = new int[4];
        String[] wordsArray;

        System.out.println("Starting Cluster ...");

        //establish connection with the loadGenerator

        //clusterSimulator client = new clusterSimulator(500,50);
        this.startConnection("127.0.0.1", 6666);
        String response = this.sendMessage("cluster_connect");
        System.out.println("response from server: "+response);


        //accepts jobs from loadGenerator
        while(true){
            String jobRecv = recvMessage();
            if(jobRecv.equals("Stop")){
                System.out.println("Stopping the Cluster no new jobs...");
                break;
            }

            //System.out.println("Cluster recv: " +jobRecv);
            //puts the jobs in the queue
            wordsArray = jobRecv.split(",");
            for(int i=0; i<4; i++){
                fields[i] = Integer.parseInt(wordsArray[i]);
            }

            job newJob = new job(fields[0],fields[1],fields[2],fields[3]);
            newJob.printJob("recv");


            //this is like qsub()
            qsub(newJob);

        }

        try {
            this.executionThread.join();
        }catch(Exception e){
            System.out.println("executionThread: Exception occured join: "+ e);
        }

    }

    private void qsub(job newJob){


        //sanity check to see if number of jobs exceeds the limit
        if(this.sharedState.jobsInQueue > this.perUserQueueLimit){
            //signal that can't accept

            //write to a status file TODO
            System.out.println("SIGNAL: perUserQueueLimit exceeds, jobID: "+newJob.getJobID());
            return;
        }

        synchronized (this.sharedState){

            this.sharedState.jobsQueue.add(newJob);
            //this counter represents the logical jobs in queue that are in the jobsQueue or executing
            this.sharedState.jobsInQueue++;
            //put an event to the executor thread
            //new job came
            //sharedState.eventPresent = true;
            this.sharedState.eventsPresent++;
        }

        //The executioner will then remove a job and put it in running state if there is resource

    }

    public job removeJob(){

        return this.sharedState.jobsQueue.remove();

    }



    //write the destructor?


    public static void main(String[] args) {

        clusterSimulator cluster = new clusterSimulator(500,50, 30);
        cluster.startCluster();

        //cluster.removeJob().printJob();
        //cluster.removeJob().printJob();
        //cluster.removeJob().printJob();

    }
}


