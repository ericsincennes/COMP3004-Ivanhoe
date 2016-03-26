package comp3004.ivanhoe;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class ClientGUI {

	private JFrame frmMain;
	private JPanel informationPanel;
	private JPanel displaysPanel;
	private JPanel handPanel;
	private JPanel actionArea;
	
	private JLabel informationLable = new JLabel();
	private JLabel tournamentColourLable = new JLabel();
	
	private ArrayList<BufferedImage> cardImages;
	
	private static final String ImageDirectory = (System.getProperty("user.dir") + "/src/Images/");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	private void getImages(){
		File dir = new File(ImageDirectory);
		for(File x: dir.listFiles()){
			
		}
	}
	
	/**
	 * Everything relating to the action area JPanel goes in here
	 */
	private void initializeActionArea(){
		actionArea = new JPanel();
		actionArea.setBackground(Color.gray);
		actionArea.setLayout(new GridLayout(5,1));
		
		JButton endTurnButton = new JButton("End Turn");
		JButton ivanhoeButton = new JButton("Ivanhoe");
		JButton withdrawButton = new JButton("Withdraw");
		
		//End turn button pressed
		endTurnButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(false){
					
				} else {
					JOptionPane.showMessageDialog(actionArea, "Cannot end turn when it is not your turn", "End Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//Ivanhoe Button Pressed
		ivanhoeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(false){
					
				} else {
					JOptionPane.showMessageDialog(actionArea, "You do not have the card Ivanhoe to play", "Ivanhoe Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		//Withdraw Button Pressed
		withdrawButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(false){
					
				} else {
					JOptionPane.showMessageDialog(actionArea, "Cannot withdraw when it is not your turn", "Withdraw Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		actionArea.add(new JLabel("", JLabel.CENTER));
		actionArea.add(endTurnButton);
		actionArea.add(withdrawButton);
		actionArea.add(ivanhoeButton);
	}
	
	/**
	 * Everything relating to the hand area JPanel goes in here
	 */
	private void initializeHandPanel(){
		handPanel = new JPanel();
		handPanel.setBackground(Color.gray);
		handPanel.setLayout(new FlowLayout());
		handPanel.add(new JLabel("Your Hand", JLabel.CENTER));
	
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File(ImageDirectory + "Adapt.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel image = new JLabel(new ImageIcon(img));
		handPanel.add(image);
	}
	
	private void initializeDisplayPanel(){
		displaysPanel = new JPanel();
		displaysPanel.setBackground(Color.cyan);
		displaysPanel.setLayout(new FlowLayout());
		displaysPanel.add(new JLabel("Board Display Area", JLabel.CENTER));
	}
	
	private void initializeInformationPanel(){
		informationPanel = new JPanel();
		informationPanel.setBackground(Color.orange);
		informationPanel.setLayout(new GridLayout(2, 1));
		
		informationLable.setText("Information Lable");
		informationLable.setHorizontalAlignment(JLabel.CENTER);
		
		tournamentColourLable.setText("Tournament colour is: Tournament Colour");
		tournamentColourLable.setHorizontalAlignment(JLabel.CENTER);
		
		informationPanel.add(informationLable);
		informationPanel.add(tournamentColourLable);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMain = new JFrame();
		frmMain.setTitle("Ivanhoe");
		frmMain.setBounds(100, 100, 957, 761);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Get all images to file
		getImages();
		
		//Initialize all GUI Pieces
		initializeActionArea();
		initializeHandPanel();
		initializeInformationPanel();
		initializeDisplayPanel();
		
		frmMain.getContentPane().add(informationPanel, BorderLayout.NORTH);
		frmMain.getContentPane().add(displaysPanel, BorderLayout.CENTER);
		frmMain.getContentPane().add(handPanel, BorderLayout.SOUTH);
		frmMain.getContentPane().add(actionArea, BorderLayout.WEST);
	}
	
	public void updateInformationLable(String text){
		informationLable.setText(text);
	}
	
	public void updateTournamentColourLable(String text){
		tournamentColourLable.setText("Current tournament colour is: " + text);
	}
}