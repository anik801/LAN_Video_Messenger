/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skypeserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author John
 */
public class SkypeServer {

    /**
     * @param args the command line arguments
     */
    
    //declaring global variables
    int socketNumber;    
    
    ServerSocket serverSocket;
    Socket clientSocket;
    
    //class constructor
    public SkypeServer(int port){
        socketNumber=port;   
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        SkypeServer currentServer = new SkypeServer(2020);  
        currentServer.initializeServer();
      
        currentServer.waitForUser();
    }
    
    private void waitForUser(){
        try{
            while(true){
                clientSocket = serverSocket.accept();
                
                //BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //PrintStream clientWriter = new PrintStream(clientSocket.getOutputStream());   
                
                //new ClientActivityThread(clientReader, clientWriter);
                new ClientActivityThread(clientSocket);
            }
            
        }catch(Exception e){
            System.out.println("Exception in wait for user: "+e);
        }
    }
    
    private void initializeServer(){
        try {
            serverSocket = new ServerSocket(socketNumber);    //Creating server
            System.out.println("Server started at socket: "+socketNumber);
        } catch (Exception ex) {
            System.err.println("Server Initialization exception: "+ex);
        }        
    }
    
    
    
    public static String usernames[] = new String[100];
    public static PrintStream printStreams[] = new PrintStream[100];
    public static BufferedReader bufferedReaders[] = new BufferedReader[100];
    public static int[] isBusy = new int[100];
    public static int clientIndex = 0;
    
    public static void setUserInfo(String un, PrintStream ps, BufferedReader br){
        int i,flag=0;
        for(i=0; i<clientIndex;i++){
            if(un.equals(usernames[i])){
                printStreams[i] = ps;
                bufferedReaders[i] = br;
                isBusy[i]=0;
                flag=1;
                break;
            }
        }
        
        if(flag==0){
            usernames[clientIndex] = un;
            printStreams[clientIndex] = ps;
            bufferedReaders[clientIndex] = br;
            clientIndex++;
        }
    }
    
    public static PrintStream getPrintStream(String username){
        int i;
        for(i=0; i<clientIndex;i++){
            if(username.equals(usernames[i])){
                break;
            }
        }
        
        PrintStream res = printStreams[i];
        return res;
    }
    
    public static BufferedReader getBufferedReader(String username){
        int i;
        for(i=0; i<clientIndex;i++){
            if(username.equals(usernames[i])){
                break;
            }
        }
        
        BufferedReader res = bufferedReaders[i];
        return res;
    }
    
    public static void setBusyStatus(String username){
        int i;
        for(i=0; i<clientIndex;i++){
            if(username.equals(usernames[i])){
                break;
            }
        }
        
        isBusy[i] = 1;
    }
    
    
}
