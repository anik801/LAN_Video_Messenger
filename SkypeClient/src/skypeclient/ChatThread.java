/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skypeclient;

import java.io.BufferedReader;
import java.io.PrintStream;
import javax.swing.JTextArea;

/**
 *
 * @author John
 */
public class ChatThread extends Thread{
    BufferedReader serverReader;
    PrintStream serverWriter;
    JTextArea outputTextArea;
    String sender;
    public ChatThread(BufferedReader serverReader, PrintStream serverWriter, JTextArea outputTextArea, String sender){
        this.serverReader = serverReader;
        this.serverWriter = serverWriter;
        this.outputTextArea = outputTextArea;
        this.sender = sender;
        
        start();
    }
    
    public void run(){
        try{
            while(true){
                sleep(10);
                String message = serverReader.readLine();
                //message = serverReader.readLine();  //solving the 1st message problem
                //serverWriter.println("messageReceived");
                System.out.println("Message: "+message);
                outputTextArea.append(sender+">>"+message+"\n");
                outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());

                
            }
        }catch(Exception e){
            System.out.println("Chat thread exception: "+e);
        }
    }
}
