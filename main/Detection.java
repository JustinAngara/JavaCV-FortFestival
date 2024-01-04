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
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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

	
	public static void releaseInfo(IplImage img) throws AWTException {
		if(img==null) {
			return;
		}
        CvSize cvSize = cvSize(img.width(), img.height());
        IplImage gry=cvCreateImage(cvSize, img.depth(), 1);
        cvCvtColor(img, gry, CV_BGR2GRAY);
        cvThreshold(gry, gry, 200, 255, CV_THRESH_BINARY);
        cvAdaptiveThreshold(gry, gry, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 5);

        CvMemStorage storage = CvMemStorage.create();
        CvSeq contours = new CvContour(null);

        int noOfContors = cvFindContours(gry, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));

        CvSeq ptr = new CvSeq();

        int count =1;
        CvPoint p1 = new CvPoint(0,0),p2 = new CvPoint(0,0);

        for (ptr = contours; ptr != null; ptr = ptr.h_next()) {

            CvScalar color = CvScalar.BLUE;
            CvRect sq = cvBoundingRect(ptr, 0);


                
                
            if(sq.width() > sq.height()) {
                System.out.println("Contour No ="+count);
                System.out.println("X ="+ sq.x()+" Y="+ sq.y());
                System.out.println("Height ="+sq.height()+" Width ="+sq.width());
            	p1.x(sq.x());
                p2.x(sq.x()+sq.width());
                p1.y(sq.y());
                p2.y(sq.y()+sq.height());
                cvRectangle(img, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
                cvDrawContours(img, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
                count++;
                clickButton(sq.x(),sq.y());
            }
        }
        

        cvShowImage("contures",img);
        cvWaitKey(0);
        iterate();
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
	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
	        t.start();
	    }
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
	        t.stop();
	    }
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void iterate() {
		try {
            Robot robot = new Robot();

            
            Rectangle region = new Rectangle(x, y, x1 - x, y1 - y); // Top-left coordinates, width, height

            // Capture screenshot of the specified region
            BufferedImage screenshot = robot.createScreenCapture(region);

            // Convert to IplImage and process as before
            IplImage img = IplImage.createFrom(screenshot);
            releaseInfo(img);
        } catch (AWTException e) {
            e.printStackTrace();
        }
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
	}
    public static void main(String[] args) throws AWTException {
    	config();
    	t = new Timer(250,(ActionEvent e)->{
        	
    		iterate();
    	});
    	run();
    }
    /*
    790, 1770 x2-x1
    1060, 1488 x2-x1
    
    
    */
}
