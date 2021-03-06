/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skypeclient;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author John
 */
public class OnlineUserList extends javax.swing.JFrame implements ActionListener {

    /**
     * Creates new form OnlineUserList
     */
    public OnlineUserList(PrintStream serverWriter, BufferedReader serverReader, String username) {        
        initComponents();   
        
        setServerIO(serverWriter, serverReader,username);
        readServerCommandThreadObj = new ReadServerCommandThread();
        readServerCommandThreadObj.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        refreshListBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        refreshListBtn.setText("Refresh List");
        refreshListBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshListBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(301, Short.MAX_VALUE)
                .addComponent(refreshListBtn)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(refreshListBtn)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshListBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshListBtnActionPerformed
        // TODO add your handling code here:
        //readServerCommandThreadObj.suspend();
        //showOnlineUserList();
        System.out.println("Refresh List Buttun Pressed");
        serverWriter.println("showUserList");
        //readServerCommandThreadObj.resume();
        
    }//GEN-LAST:event_refreshListBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OnlineUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OnlineUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OnlineUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OnlineUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //OnlineUserList currentPage = new OnlineUserList();
                //currentPage.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton refreshListBtn;
    // End of variables declaration//GEN-END:variables

    int udpPortOut,udpPortIn;
    String btnText = "";
    InetAddress ownIP;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //readServerCommandThreadObj.stop();
            
            //System.out.println(e.getID());
            Object obj = e.getSource();
            JButton b = null;
            
            
            if(obj instanceof JButton)
                b = (JButton)obj;
            
            if(b != null)
                btnText = b.getText();
            
            System.out.println("knockUser");
            //System.out.println(btnText);
            
            serverWriter.println("knockUser");            
            serverWriter.println(btnText);
            

            udpPortOut = randInt(3000,4000); 
            udpPortIn = udpPortOut+1;
            
            serverWriter.println(udpPortOut);
            
            ownIP = InetAddress.getLocalHost();
            String ownIPString = ownIP.getHostAddress();            
            System.out.println("Own IP: "+ownIPString);
            serverWriter.println(ownIPString);
            
        } catch (Exception ex) {
            //Logger.getLogger(OnlineUserList.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception sending knockUser command: "+ex);
        }
    }

    
    public void readServerCommand(){
        try{
            String command = serverReader.readLine();
            System.out.println("Server: "+command);
            
            if(command.equals("incomingKnock")){
                String caller = serverReader.readLine();
                int udpPortIn = Integer.parseInt(serverReader.readLine());
                int udpPortOut = udpPortIn+1;
                
                String callerIPString = serverReader.readLine();
                InetAddress callerIP = InetAddress.getByName(callerIPString);
                                
                InetAddress ownIP = InetAddress.getLocalHost();
                String ownIPString  = ownIP.getHostAddress();                              
                
                System.out.println("Own IP: "+ownIPString);
                System.out.println("Receiver IP: "+callerIPString);
                
                serverWriter.println("incomingKnock");
                serverWriter.println(caller);
                serverWriter.println(udpPortIn);
                serverWriter.println(ownIPString);
                
                int videoPortOut = udpPortOut+2;
                int videoPortIn = udpPortIn+2;
                ChatWindow chtWin = new ChatWindow(serverReader, serverWriter, username, caller,udpPortOut,udpPortIn,ownIP,callerIP,videoPortOut,videoPortIn);
                
                
                chtWin.setVisible(true);
                this.setVisible(false);
                readServerCommandThreadObj.stopThread();
            }else if(command.equals("userList")){
                showOnlineUserList();
            }else if(command.equals("receiverIP")){
                String receiverIPString = serverReader.readLine();
                InetAddress receiverIP = InetAddress.getByName(receiverIPString);
                System.out.println("Receiver IP: "+receiverIPString);

                
                int videoPortOut = udpPortOut+2;
                int videoPortIn = udpPortOut+3;
                ChatWindow chtWin = new ChatWindow(serverReader, serverWriter, username, btnText,udpPortOut,udpPortIn,ownIP,receiverIP,videoPortOut,videoPortIn);
                
                chtWin.setVisible(true);
                this.setVisible(false);
                readServerCommandThreadObj.stopThread();
            }
            
        }catch(Exception e){
            System.out.println("Exception reading server command: "+e);
        }
    }
    
    public void showOnlineUserList(){
        try{
            getContentPane().removeAll();
            initComponents();  
            repaint();
        }catch(Exception e){
            System.out.println("Exception in showOnlineUserList: "+e);
        }
        
        
        String usernames[] = new String[100];
        int userIndex  = 0;
    
        try {            
            setLayout(null);
            int yAxis = 10;
            
            String str = serverReader.readLine();            
            while(!str.equals("-1")){
                System.out.println("SERVER USER LIST: "+str);
                if(!str.equals(username)){
                    usernames[userIndex] = str;
                    //System.out.println(usernames[userIndex++]);
                    userIndex++;

                    JButton btn = new JButton(str);
                    btn.setBounds(50, yAxis, 80, 25);
                    yAxis+=30;

                    add(btn);
                    btn.addActionListener(this);
                    
                }
                str = serverReader.readLine();
            }
            setTitle("User List");
            //setSize(300, 250);
            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //setLocationRelativeTo(null);
            
            revalidate();
            repaint();
            
        }catch (IOException ex) {
            System.out.println("Exception fetching userlist: "+ex);
        }catch( Exception ex){
            System.out.println("Exception fetching userlist2: "+ex);
        }
    }
    
    PrintStream serverWriter;
    BufferedReader serverReader;
    String username;
    ReadServerCommandThread readServerCommandThreadObj;
    
    public void setServerIO(PrintStream serverWriter, BufferedReader serverReader, String username){
        this.serverWriter = serverWriter;
        this.serverReader =  serverReader;
        this.username = username;
    }
    
    public int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    
    //////////////////////////////////////////////////////////////////////
    public class ReadServerCommandThread extends Thread{ 
        public void run(){
            try {
                int i=0;
                while(true){
                    //sleep(10);                
                    System.out.println(i++);
                    readServerCommand();
                    //obj.showOnlineUserList();
                }
            } catch (Exception ex) {
                System.out.println("User List Refresh Exception: "+ex);
            }
        }
        
        public void stopThread(){
            this.stop();
            this.destroy();
        }
    }
    //////////////////////////////////////////////////////////////////////
}