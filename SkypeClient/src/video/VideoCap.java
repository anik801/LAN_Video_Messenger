/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.VideoCapture;

public class VideoCap {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
    } 
 
    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
}