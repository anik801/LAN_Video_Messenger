/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author John
 */

public class VideoIO extends JFrame {
    private JPanel contentPane;

  /**
  * Launch the application.
  */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VideoIO frame = new VideoIO();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

  /**
  * Create the frame.
  */
    public VideoIO() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);        
        
        new VideoRecorderThread().start();
    }
    
    public VideoIO(InetAddress callerIP, int videoPortOut, int videoPortIn) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);        
        
        this.callerIP = callerIP;
        this.videoPortOut = videoPortOut;
        this.videoPortIn = videoPortIn;
        videoCap = new VideoCap();
        
        this.setVisible(true);
        
        new VideoRecorderThread().start();
        new VideoPlayerThread().start();
        
    }
    
    int videoPortOut,videoPortIn;
    InetAddress callerIP;
    
    VideoCap videoCap;
    public BufferedImage outgoingFrame;
    public BufferedImage incomingFrame=null;
    
    
    public void paint(Graphics g){
        try {
            g = contentPane.getGraphics();
            
            outgoingFrame = videoCap.getOneFrame();
            
            //Convert to byte stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outgoingFrame, "jpg", baos);
            byte[] imageByteArray = baos.toByteArray();
            
            //Transmit video
            DatagramSocket clientSocket=new DatagramSocket();
            DatagramPacket sendPacket = new DatagramPacket(imageByteArray, imageByteArray.length, callerIP, videoPortOut);
            clientSocket.send(sendPacket);
            clientSocket.close();
            
                                    
            //display received frame
            g.drawImage(incomingFrame, 0, 0, this);
        } 
        catch (IOException ex) {
            //Logger.getLogger(VideoIO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException in paint: "+ex);
        }
        catch (Exception ex) {
            //Logger.getLogger(VideoIO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception in paint: "+ex);
        }
    }
 
    class VideoRecorderThread extends Thread{
        @Override
        public void run() {
            while(true){
                repaint();
                try { 
                    sleep(30);
                } catch (InterruptedException e) {    
                    System.out.println("InterruptedException in VideoRecorderThread: "+e);
                }
            }  
        } 
    }
    
    class VideoPlayerThread extends Thread{
        @Override
        public void run() {
            try {
                
                DatagramSocket serverSocket = new DatagramSocket(videoPortIn);
                byte[] receiveData = new byte[50000];
                
                while(true){
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        
                        byte videoData[] = receivePacket.getData();
                        //System.out.println(videoData.toString());
                        
                        //Convert back to image
                        InputStream imageStream = new ByteArrayInputStream(videoData);
                        incomingFrame = ImageIO.read(imageStream);
                        
                        //ImageIO.write(incomingFrame, "jpg", new File("c:/test/testImage.jpg"));
                        
                        repaint();
                        sleep(30);
                    } catch (IOException ex) {
                        //Logger.getLogger(VideoIO.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("IOException in VideoPlayerThread: "+ex);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(VideoIO.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("InterruptedException in VideoPlayerThread: "+ex);
                    }
                }
            } catch (SocketException ex) {
                Logger.getLogger(VideoIO.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("SocketException in VideoPlayerThread: "+ex);
            }
        } 
    }
}