package com.fnfes.main;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

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

public class AltDetection implements NativeKeyListener{
	private static Timer t, a,s,k,l;
//	private static List<Boolean> shouldDelayTest = new ArrayList<Boolean>();
	private static boolean shouldDelayTest;
	private static int x;
	private static int x1;
	private static int y;
	private static int y1;
	
	private static Robot bot;
	
	private static JFrame frame;
	private static JLabel imageLabel;
	
	private static Rectangle region;
	private static BufferedImage screenshot;
	
	private static Detection d = new Detection();
	
	private static int targetRed, targetBlue, targetGreen, tolerance;
	private static int red,green,blue;
	private static List<Point> whitePointsList = new ArrayList<>();

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
//	    if (arg0.getKeyCode() == NativeKeyEvent.VC_Z) {
//	        t.stop();
//	    }
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void iterate() throws AWTException {

		region = new Rectangle(x, y, x1 - x, y1 - y); // Top-left coordinates, width, height
//        region = new Rectangle(1076, 545, 1475-1076, 995-645);  

		screenshot = returnGrayScale(bot.createScreenCapture(region));
//		screenshot = bot.createScreenCapture(region);
		
		
		//y value 530 
/*		
		for (int y = 0; y < screenshot.getHeight(); y++) {
		    for (int x = 0; x < screenshot.getWidth(); x++) {
		        int pixel = screenshot.getRGB(x, y);

		        red = (pixel >> 16) & 0xFF;
		        green = (pixel >> 8) & 0xFF;
		        blue = pixel & 0xFF;

		        // Check if pixel values are within tolerance of target color
		        if (Math.abs(red - targetRed) <= tolerance &&
		            Math.abs(green - targetGreen) <= tolerance &&
		            Math.abs(blue - targetBlue) <= tolerance) {
		            screenshot.setRGB(x, y, 0xFFFFFF); // Set red pixel (or any other color you want)
		            System.out.println(screenshot.getHeight());
		            System.out.println("Y VAlUE AT DETECTION: "+y);
		        } else {
		        	screenshot.setRGB(x, y, 0x000000);
		        }
		        

		    }
		}
*/
		
		// creates the javacv instance
		imageLabel.setIcon(new ImageIcon(screenshot));
        d.releaseInfo(IplImage.createFrom(screenshot));
	    
	}
	
 
	
	public static BufferedImage returnGrayScale(BufferedImage img) {

 
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
        
		return img;
	}
	
	public static void run() throws AWTException {
		GlobalScreen.addNativeKeyListener(new AltDetection());
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
		//            Rectangle region = new Rectangle(1076, 545, 1475-1076, 995-645);  
		x = 890;
        x1 = 1650;
        
        y = 633;
        y1= 1172;

        int width = x1 - x;
        int height = y1 - y;

        frame = new JFrame("Image Display");
        frame.setBounds(x, y, width, height);
        
        bot = new Robot();
       
		targetRed = 250;  
		targetGreen = 250;
		targetBlue = 250;
		tolerance = 25;
        
	}
	
	public static void createFrame() {
	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setAlwaysOnTop(true);
	    imageLabel = new JLabel();
	    frame.add(imageLabel);
	    
	    frame.pack();
	    frame.setVisible(true);
	    // going to start it up
	    t = new Timer(20,(ActionEvent e)->{
        	try {
        		iterate();
        	} catch(Exception e1) {
        		System.err.println("Some sort of error "+e1.getMessage());
        	}
    	});
	    
	}
    public static void main(String[] args) throws AWTException {
    	config();
    	createFrame();
    	run();
    	
    	
    	
    	shouldDelayTest = false;

		Timer test = new Timer(250, (ActionEvent e) -> {
		    if (!shouldDelayTest) {
		        // do stuff
		    	System.out.println("test");
		    } else {
		        try {
		            TimeUnit.SECONDS.sleep(1); // Delay for 1 second
		        } catch (InterruptedException e1) {
		            e1.printStackTrace();
		        }
		        shouldDelayTest = false; // Reset the flag for next time
		    }
		});
//		test.start();
		
    }
}
