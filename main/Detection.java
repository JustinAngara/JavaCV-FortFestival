package com.fnfes.main;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Detection implements NativeKeyListener{
	private static Timer t;
	private static int x;
	private static int x1;
	private static int y;
	private static int y1;
	
	private static Robot bot;

	// cv stuff
	static CvSize cvSize;
	static IplImage gry;
	static CvMemStorage storage;
	static CvSeq contours;
	static int noOfContors;
	static CvSeq ptr;
	static int count;
	static CvPoint p1;
	static CvPoint p2;
	static CvScalar color;
	static CvRect sq;
	
	
	public static void releaseInfo(IplImage img) throws AWTException {
		if(img==null) {
			return;
		}
        cvSize = cvSize(img.width(), img.height());
        gry=cvCreateImage(cvSize, img.depth(), 1);
        cvCvtColor(img, gry, CV_BGR2GRAY);
        cvThreshold(gry, gry, 200, 255, CV_THRESH_BINARY);
        cvAdaptiveThreshold(gry, gry, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 5);
        
        
        storage = CvMemStorage.create();
        contours = new CvContour(null);

        noOfContors = cvFindContours(gry, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));

        ptr = new CvSeq();

        count =1;
        p1 = new CvPoint(0,0);
        p2 = new CvPoint(0,0);

        for (ptr = contours; ptr != null; ptr = ptr.h_next()) {

            sq = cvBoundingRect(ptr, 0);     
            if(sq.width() > sq.height()) {
                System.out.println("Contour No ="+count);
                System.out.println("X ="+ sq.x()+" Y="+ sq.y());
                System.out.println("Height ="+sq.height()+" Width ="+sq.width());
            	p1.x(sq.x());
                p2.x(sq.x()+sq.width());
                p1.y(sq.y());
                p2.y(sq.y()+sq.height());
                cvRectangle(img, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
//                cvDrawContours(img, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
                count++;
//                clickButton(sq.x(),sq.y());
            }
        }
        

        cvShowImage("contures",img);
        cvWaitKey(1);
      
        
//        iterate();
	}
	public static void clickButton(int x, int y) throws AWTException {
		if(y > 140) {
			return;
		}
		if(x > 140 && x < 160) {
			s();
		} else {
			k();
		}
		// 287 right
		// 150 left
		
	}
	public static void s() throws AWTException{
		bot.keyPress(KeyEvent.VK_S);
		bot.delay(5);
		bot.keyRelease(KeyEvent.VK_S);
	}
	public static void k() throws AWTException{
		bot.keyPress(KeyEvent.VK_K);
		bot.delay(5);
		bot.keyRelease(KeyEvent.VK_K);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
//	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
//	        t.start();
//	    }
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
//	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
//	        t.stop();
//	    }
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void iterate() {
		try {


            
            Rectangle region = new Rectangle(x, y, x1 - x, y1 - y); // Top-left coordinates, width, height
            
			
			// testing detection
//			int t = 1000;
//            Rectangle region = new Rectangle(t, 0, 2560-t, 1440);
//            
            
            
            // Capture screenshot of the specified region
            BufferedImage screenshot = bot.createScreenCapture(region);

            // Convert to IplImage and process as before
            IplImage img = returnGrayScale(screenshot);
            
            
            
            releaseInfo(img);
        } catch (AWTException e) {
            e.printStackTrace();
        }
	}
	
	public static IplImage returnGrayScale(BufferedImage img) {

 
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);
        // convert to grayscale
        for (int i = 0; i < pixels.length; i++) {
 
            // Here i denotes the index of array of pixels
            // for modifying the pixel value.
            int p = pixels[i];
 
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;
 
            // calculate average
            int avg = (r + g + b) / 3;
 
            // replace RGB value with avg
            p = (a << 24) | (avg << 16) | (avg << 8) | avg;
 
            pixels[i] = p;
        }
        img.setRGB(0, 0, width, height, pixels, 0, width);
        // write image
        
		return IplImage.createFrom(img);
	}
	
	public static void run() throws AWTException {
		GlobalScreen.addNativeKeyListener(new Detection());
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {}
	}
	
	public static void config() throws AWTException {
		x = 890;
        x1 = 1650;
        
        y = 633;
        y1= 1172;
        bot = new Robot();
        color = CvScalar.BLUE;
	}
	

    public static void main(String[] args) throws AWTException {
    	config();
    	t = new Timer(50,(ActionEvent e)->{
        	try {
        		iterate();
        	} catch(Exception e1) {
        		System.err.println("Some sort of error "+e1.getMessage());
        	}
    	});
    	t.start();
    	
    	run();
    }
    /*
    790, 1770 x2-x1
    1060, 1488 x2-x1
    
    
    */
}
