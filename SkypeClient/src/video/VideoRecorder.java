/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author John
 */
public class VideoRecorder extends Thread{
    int videoPortOut;
    InetAddress callerIP;
    public VideoRecorder(InetAddress callerIP, int videoPortOut) {
        this.callerIP = callerIP;
        this.videoPortOut = videoPortOut;
        
        start();

    }
    
    public void run(){
        while(true){
            try {
                captureVideo();
                /*
                byte[] text = "hello".getBytes();
                System.out.println(callerIP);
                System.out.println(videoOutPort);
                
                DatagramPacket sendPacket = new DatagramPacket(text, text.length, callerIP, videoOutPort);
                DatagramSocket clientSocket = new DatagramSocket();
                clientSocket.send(sendPacket);
                */
            } catch (Exception ex) {
                Logger.getLogger(VideoRecorder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    int i=0;
    public void captureVideo(){   
        DatagramSocket clientSocket=null;
        try {
            clientSocket = new DatagramSocket();
            sleep(200);
            //Capture image from camera
            VideoCap videoCap = new VideoCap();
            BufferedImage oneFrame = videoCap.getOneFrame();
            
            //Convert to byte stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(oneFrame, "jpg", baos);
            byte[] imageByteArray = baos.toByteArray();
            
            //Transmit video
            DatagramPacket sendPacket = new DatagramPacket(imageByteArray, imageByteArray.length, callerIP, videoPortOut);
            
            clientSocket.send(sendPacket);
            
            
        } catch (IOException ex) {
            Logger.getLogger(VideoRecorder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception in VideoRecorder: "+ex);
        } catch (Exception ex) {
            Logger.getLogger(VideoRecorder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception in VideoRecorder(2): "+ex);
        }finally{
            try{
                clientSocket.close();
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
    }
}
