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
import com.hbm.AimTrainer;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Detection implements NativeKeyListener{
	
	
	
	public static void releaseInfo(IplImage img) {
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

                System.out.println("Contour No ="+count);
                System.out.println("X ="+ sq.x()+" Y="+ sq.y());
                System.out.println("Height ="+sq.height()+" Width ="+sq.width());
                System.out.println("");
                
                
//                if(sq.width() > sq.height()) {
	                
	                p1.x(sq.x());
	                p2.x(sq.x()+sq.width());
	                p1.y(sq.y());
	                p2.y(sq.y()+sq.height());
	                cvRectangle(img, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
	                cvDrawContours(img, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
	                count++;
//                }
        }

        cvShowImage("contures",img);
        cvWaitKey(0);
	}


	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
	        try {
	            Robot robot = new Robot();

	            // Specify the desired screenshot region
	            // 1073 633 | 867 842
	            int x = 890;
	            int x1 = 1650;
	            
	            int y = 633;
	            int y1= 1200;
	            
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
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
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
    public static void main(String[] args) throws AWTException {
        run();
    }
/*
 * bounds 
 * 1073 633 | 867 842
 * 1680 1213
 * */
}
