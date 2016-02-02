/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class ThreadTest {    
    public static void main(String[] args) {
        ThreadTest ins = new ThreadTest();
        ins.init(ins);
     
    }
    
    public void init(ThreadTest obj){
        
         thrd = new MyThread(obj);
    }
    
    MyThread thrd;
    int i=0;
    public void callMain(){
        System.out.println("callMain: "+i);
        i++;
        thrd.stop();
        
    }
}


class MyThread extends Thread{
    ThreadTest obj;
    public MyThread(ThreadTest obj){
        this.obj = obj;
        start();
    }
    
    public void run(){
        int i=0;
        while(true){
            try {
                sleep(10);
                System.out.println("MyThread: "+i);                
                
                obj.callMain();
                i++;
            } catch (InterruptedException ex) {
                Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}