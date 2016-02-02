/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skypeserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class ClientActivityThread extends Thread {

    //declaring global variables    
    String[][] userdata = new String[100][2];
    int row, col;    //Initializing array index
    String trueFlag, falseFlag;

    Socket clientSocket;
    PrintStream clientWriter;
    BufferedReader clientReader;

    String username, password;
    String udpPortString;

    public ClientActivityThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientWriter = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Exception establishing I/O link: " + ex);
        }

        row = 0;
        col = 0;    //Initializing array index
        trueFlag = "1";
        falseFlag = "0";

        start();
    }

    public ClientActivityThread(BufferedReader clientReader, PrintStream clientWriter) {
        this.clientReader = clientReader;
        this.clientWriter = clientWriter;

        row = 0;
        col = 0;    //Initializing array index
        trueFlag = "1";
        falseFlag = "0";

        start();
    }

    public void run() {
        try {
            readUserDatabase();
            if (authenticateClient()) {
                clientActivityLoop();
            } else {
                this.stop();
            }

        } catch (Exception e) {
            System.out.println("Exception in client thread run: " + e);
        }
    }

    private boolean authenticateClient() {
        boolean res = false;
        try {
            clientWriter.println(trueFlag);    //1.  Connection open

            username = clientReader.readLine();
            System.out.println("Client >> Username: " + username);    // Server output
            clientWriter.println(trueFlag);    //2.  Username

            password = clientReader.readLine();
            System.out.println("Client >> Password: " + password);    // Server output
            clientWriter.println(trueFlag);    //3.  Password

            if (trueFlag.equals(checkUser(username, password))) {      //4.  Authentication
                SkypeServer.setUserInfo(username, clientWriter, clientReader);

                clientWriter.println(trueFlag);
                res = true;
            } else {
                clientWriter.println(falseFlag);
                res = false;
            }
        } catch (Exception e) {
            System.out.println("Exception authenticating client: " + e);
        }

        return res;
    }

    private void clientActivityLoop() {
        try {
            while (true) {
                sleep(10);

                String command = clientReader.readLine();
                if (command.equals("showUserList")) {
                    System.out.println("showUserList Command called by "+username);
                    sendActiveUserList();
                } else if (command.equals("knockUser")) {
                    receiver = clientReader.readLine();
                    System.out.println("knockUser Command called by " + username + " to: " + receiver);
                    
                    udpPortString = clientReader.readLine();
                    String ownIP = clientReader.readLine();

                    sendKnockRequest(receiver, ownIP);
                } else if (command.equals("incomingKnock")) {
                    receiver = clientReader.readLine();
                    System.out.println("incomingKnock Command from " + receiver + " to: " + username);
                    
                    udpPortString = clientReader.readLine();
                    String ownIP = clientReader.readLine();

                    incomingKnockRequest(receiver, ownIP);
                } else if (command.equals("sendMessage")) {
                    String message = clientReader.readLine();
                    sendMessage(message);
                }
                //clientWriter.println("-1");
            }

        } catch (IOException ex) {
            System.out.println("Client activity I/O exception: " + ex);
        } catch (Exception ex) {
            System.out.println("Client activity loop interrupted exception: " + ex);
        }
    }

    private String checkUser(String username, String password) {
        for (int i = 0; i < row; i++) {
            if (username.equals(userdata[i][0]) && password.equals(userdata[i][1])) {
                System.out.println("Access Granted.");  // Server output
                return trueFlag;
            }
        }
        System.out.println("Access Denied!");       // Server output
        return falseFlag;
    }

    private void readUserDatabase() {

        String str;
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File("user_accounts.txt"));

            while (fileScanner.hasNext()) {
                str = fileScanner.next();
                userdata[row][col] = str;     //username
                col++;
                str = fileScanner.next();
                userdata[row][col] = str;     //password
                row++;
                col = 0;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found exception: " + e);
        }

        fileScanner.close();
        //showUserDatabase();
    }

    private void showUserDatabase() {
        //Testing array input
        for (int i = 0; i < row; i++) {
            System.out.println(userdata[i][0] + "\t" + userdata[i][1]);
        }

    }

    private void sendActiveUserList() {
        clientWriter.println("userList");
        for (int i = 0; i < SkypeServer.clientIndex; i++) {
            if(SkypeServer.isBusy[i]==0){
                clientWriter.println(SkypeServer.usernames[i]);
                //System.out.println(SkypeServer.usernames[i]);
            }
            
        }
        clientWriter.println("-1");
    }

    String receiver;
    BufferedReader receiverReader;
    PrintStream receiverWriter;

    private void sendKnockRequest(String receiver, String ownIP) {
        receiverReader = SkypeServer.getBufferedReader(receiver);
        receiverWriter = SkypeServer.getPrintStream(receiver);

        //System.out.println(receiverWriter);
        //System.out.println(clientWriter);
        receiverWriter.println("incomingKnock");
        receiverWriter.println(username);
        receiverWriter.println(udpPortString);
        receiverWriter.println(ownIP);
        
        SkypeServer.setBusyStatus(username);
        SkypeServer.setBusyStatus(receiver);
    }

    private void incomingKnockRequest(String receiver, String ownIP) {
        receiverReader = SkypeServer.getBufferedReader(receiver);
        receiverWriter = SkypeServer.getPrintStream(receiver);

        receiverWriter.println("receiverIP");
        receiverWriter.println(ownIP);

    }

    private void sendMessage(String message) {
        try {
            System.out.println("Message from " + username + " to " + receiver + ": " + message);
            //receiverWriter.println(message);
            receiverWriter.println(message);    //solving the 1st message problem
        } catch (Exception ex) {
            System.out.println("Exception forwarding message(2): " + ex);
        }
    }

}
