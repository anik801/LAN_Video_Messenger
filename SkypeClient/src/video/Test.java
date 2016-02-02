/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;

/**
 *
 * @author John
 */
public class Test {
    public Test(){
        init();        
    }
    
    public static void main(String[] args) {
        new Test();
    }
    
    public void init(){
        try{
            int i=0;
            for(;;){
                //Capture image from camera
                VideoCap videoCap = new VideoCap();
                BufferedImage oneFrame = videoCap.getOneFrame();

                //Convert to byte stream
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(oneFrame, "png", baos);
                byte[] imageByteArray = baos.toByteArray();

                //Convert back to image
                InputStream in = new ByteArrayInputStream(imageByteArray);
                BufferedImage bImageFromConvert = ImageIO.read(in);

                //Output
                ImageIO.write(bImageFromConvert, "jpg", new File("c:/test/testimage"+i+".jpg"));
                i++;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        


    }
    
}
