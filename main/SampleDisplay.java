package com.fnfes.main;


import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class SampleDisplay {

	private JFrame frame;

	/** 
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SampleDisplay window = new SampleDisplay();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SampleDisplay() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(0, 0, 2560, 1440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
		
		Icon icon;
		icon = new ImageIcon("C:\\Users\\justi\\eclipse-workspace\\JustinProgram\\image\\fnes1.png");
		JLabel lblNewLabel = new JLabel(icon);
		lblNewLabel.setBounds(0, 0, 2560, 1440);
		frame.getContentPane().add(lblNewLabel);
		
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setFocusable(false);
		
		
		
	}
}
