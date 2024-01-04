package com.fnfes.main;

import javax.swing.*;
import java.awt.*;

public class Display {

	
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // Remove window decorations
        frame.setBackground(new Color(0, 0, 0, 0));
        
        frame.setAlwaysOnTop(true); // Make it always on top

        // Create a custom panel for drawing
        DrawingPanel panel = new DrawingPanel();
        frame.getContentPane().add(panel);

        // Set frame size and make it visible
        frame.setSize(2560, 1440);
        frame.setVisible(true);
    }

    static class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Enable anti-aliasing for smoother edges
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set transparent background
            setBackground(new Color(0, 0, 0, 0)); // Alpha value of 0 for transparency

            // Fill the left triangle (upside down)
            int x1 = 800;
            int y1 = 633; // Base at bottom
            int x2 = 1070;
            int y2 = 1440; // Vertex at top
            g2d.setColor(Color.BLACK); // Choose your desired color
            g2d.fillPolygon(new int[] {x1, x2, x1}, new int[] {y1, y1, y2}, 3);

         // Fill the second triangle (blue, now drawn first)
            int base = 200; // Adjust base size as needed
            int x3 = getWidth() / 2;
            int y3 = getHeight() - base; // Base at bottom
            g2d.setColor(Color.BLACK); // Choose your desired color
            g2d.fillPolygon(new int[] {1487,1770,1770}, new int[] {y1,y1, y2}, 3);

            /*
             x values
			1770
			1482	
			
            
            
            */
        }
    }
}
