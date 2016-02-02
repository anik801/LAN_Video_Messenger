/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
public class VideoPlayer extends JFrame{
    int videoPortIn;
    private JPanel contentPane;
    
    public VideoPlayer(int videoPortIn){
        this.videoPortIn = videoPortIn;          
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
               
        
        setVisible(true);
        new VideoPlayerThread().start();
    }
    
    public void paint(Graphics g){
        g = contentPane.getGraphics();
        //g.drawImage(videoCap.getOneFrame(), 0, 0, this);
        g.drawImage(bImageFromConvert, 0, 0, this);
    }
 
    BufferedImage bImageFromConvert;
    
    class VideoPlayerThread extends Thread{
        @Override
        public void run() {
            
            try {
                DatagramSocket serverSocket = new DatagramSocket(videoPortIn);
                byte[] receiveData = new byte[50000];

                while(true){
                    sleep(200);
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    byte videoData[] = receivePacket.getData();
                    //System.out.println(videoData.toString());

                    //Convert back to image
                    InputStream imageStream = new ByteArrayInputStream(videoData);
                    bImageFromConvert = ImageIO.read(imageStream);
                    //System.out.println(imageStream);
                    //ImageIO.write(bImageFromConvert, "jpg", new File("c:/test/testImage.jpg"));
                    
                    repaint();
                }
            } catch (SocketException ex) {
                Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception in VideoPlayerThread: "+ex);
            } catch (IOException ex) {
                Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception in VideoPlayerThread(2): "+ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception in VideoPlayerThread(3): "+ex);
            } 
        } 
    }
    
    

}
