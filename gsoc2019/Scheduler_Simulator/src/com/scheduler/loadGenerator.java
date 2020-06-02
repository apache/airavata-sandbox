package com.scheduler;



import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

public class loadGenerator {

    private String jobLoadFile;// = null;
    private int totalJobs;// = 0;


    //for comm
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public String getJobLoadFile() {
        return jobLoadFile;
    }

    public void setJobLoadFile(String jobLoadFile) {
        this.jobLoadFile = jobLoadFile;
    }

    public loadGenerator(String jobLoadFile) {
        this.jobLoadFile = jobLoadFile;
    }

    public void establishConnection(int port){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();
            if ("cluster_connect".equals(greeting)) {
                out.println("Cluster Connected...");
            } else {
                out.println("Problem Connecting Cluster!");
            }
        } catch (Exception e){
            System.out.println("establishConnection: Exception occured: "+ e);
        }
    }

    public String sendMessage(String msg) {
        String resp = null;
        try {
            out.println(msg);
            //resp = in.readLine();

        } catch (Exception e){
            System.out.println("sendMessage: Exception occured: "+ e);
        }
        return resp;
    }

    public void start(int port)  {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();
            if ("hello server".equals(greeting)) {
                out.println("hello client");
            } else {
                out.println("unrecognised greeting");
            }
        } catch (Exception e){
            System.out.println("Exception occured: "+ e);
        }
    }

    public void stop() {
        try {

            //TODO see if this makes any sense
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e){
            System.out.println("Exception occured: "+ e);
        }
    }



    public void startGenerator(){



        //read jobs from file
        //code borrowed from:
        //https://stackoverflow.com/questions/19575308/read-a-file-separated-by-tab-and-put-the-words-in-an-arraylist

        try{
            BufferedReader buf = new BufferedReader(new FileReader(this.jobLoadFile));
            ArrayList<String> words = new ArrayList<>();
            String lineJustFetched = null;
            String[] wordsArray;

            //read the header
            lineJustFetched = buf.readLine();
            System.out.println("lineJustFetched = "+lineJustFetched);
            if(lineJustFetched == null){
                //this.sendMessage("Stop");
                return;
            }

            while(true){
                lineJustFetched = buf.readLine();
                System.out.println("lineJustFetched = "+lineJustFetched);
                if(lineJustFetched == null){
                    this.sendMessage("Stop");
                    break;
                }else{

                    //send to the cluster...
                    this.sendMessage(lineJustFetched);
                    //wordsArray = lineJustFetched.split("\t");

                    /*for(String each : wordsArray){
                        //System.out.println("in for "+each);
                        //so as not to have a blank line
                        if(!"".equals(each)){
                            words.add(each);
                        }
                    }*/
                }
            }

            /*for(String each : words){
                System.out.println(each);
            }*/

            buf.close();

        }catch(Exception e){
            e.printStackTrace();
        }



    }

    public static void main(String[] args) {
	// write your code here

        loadGenerator generator = new loadGenerator("/Users/bibrakqamar/work/GSoC/gitrops/Scheduler_Simulator/data/jobs.dat");
        //generator.startGenerator();
        generator.establishConnection(6666);
        generator.startGenerator();

    }
}
