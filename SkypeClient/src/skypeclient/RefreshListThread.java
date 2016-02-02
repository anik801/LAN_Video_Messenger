/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skypeclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author John
 */
public class RefreshListThread extends Thread{
    OnlineUserList obj;
    public RefreshListThread(OnlineUserList obj){
        this.obj=obj;
        
        start();
    }
    
    
    public void run(){
        try {
            while(true){
                sleep(10);                
                obj.readServerCommand();
                //obj.showOnlineUserList();
            }
        } catch (Exception ex) {
            System.out.println("User List Refresh Exception: "+ex);
        }
    }
}
