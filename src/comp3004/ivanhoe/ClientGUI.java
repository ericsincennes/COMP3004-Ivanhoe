package comp3004.ivanhoe;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.border.LineBorder;

public class ClientGUI {

	private JFrame frmMain;
	
	private JPanel informationPanel;
	private JPanel displaysPanel;
	private JPanel handPanel;
	private JPanel actionArea;
	
	private JLabel informationLable = new JLabel();
	private JLabel tournamentColourLable = new JLabel();
	private JLabel highestScore = new JLabel();
	private JLabel playerScore = new JLabel();
	
	private JScrollPane handPane;
	
	//TODO Add selected Card holder
	//once card is selected play card button is activated
	//else disabled
	
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
		
	}
	
	/**
	 * Everything relating to the action area JPanel goes in here
	 */
	private void initializeActionArea(){
		actionArea = new JPanel();
		actionArea.setBackground(Color.gray);
		actionArea.setLayout(new GridLayout(6,1));
		
		JButton endTurnButton = new JButton("End Turn");
		JButton ivanhoeButton = new JButton("Ivanhoe");
		JButton withdrawButton = new JButton("Withdraw");
		JButton playCardButton = new JButton("PlayCard");
		
		highestScore.setText("High Score: ");
		highestScore.setVerticalAlignment(JLabel.CENTER);
		
		playerScore.setText("Your Score: ");
		playerScore.setVerticalAlignment(JLabel.CENTER);
		
		playCardButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//if players turn
					//if card selected
						//send card to be played
					//else if no card selected
						//JOptionPane.showMessageDialog(actionArea, "Select a card to play or withdraw", "Cannot play nothing", JOptionPane.ERROR_MESSAGE);
				//else not players turn
					//JOptionPane.showMessageDialog(actionArea, "Cannot play card when it is not your turn", "Playing card error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		//End turn button pressed
		endTurnButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(false){
					//if player's turn
				} else {
					//if not players turn
					JOptionPane.showMessageDialog(actionArea, "Cannot end turn when it is not your turn", "End Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//Ivanhoe Button Pressed
		ivanhoeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//if player had ivanhoe in hand
					//play ivanhoe
				//else
					//JOptionPane.showMessageDialog(actionArea, "You do not have the card Ivanhoe to play", "Ivanhoe Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		//Withdraw Button Pressed
		withdrawButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(false){
					//if player's turn
				} else {
					//if not player's turn
					JOptionPane.showMessageDialog(actionArea, "Cannot withdraw when it is not your turn", "Withdraw Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		actionArea.add(highestScore);
		actionArea.add(playerScore);
		actionArea.add(endTurnButton);
		actionArea.add(withdrawButton);
		actionArea.add(ivanhoeButton);
		actionArea.add(playCardButton);
	}
	
	/**
	 * Everything relating to the hand area JPanel goes in here
	 */
	private void initializeHandPanel(){
		handPanel = new JPanel();
		handPanel.setBackground(Color.gray);
		handPanel.setLayout(new FlowLayout());
		
		handPane = new JScrollPane(handPanel);
		handPane.setVerticalScrollBarPolicy(ScrollPaneLayout.VERTICAL_SCROLLBAR_NEVER);
		//Test adding cards
		for(int i=0; i<20; i++){
			BufferedImage ba = null;
			try {
				ba = ImageIO.read(new File(ImageDirectory + "Adapt.bmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			JButton jb = new JButton(new ImageIcon(ba));
			jb.setBorder(BorderFactory.createEmptyBorder());
			handPanel.add(jb);
			
			handPane.revalidate();
		}
		
		
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
		frmMain.getContentPane().add(handPane, BorderLayout.SOUTH);
		frmMain.getContentPane().add(actionArea, BorderLayout.WEST);
	}
	
	public void updateHighestScore(String text){
		highestScore.setText("High Score: " + text);
	}
	
	public void updatePlayerScore(String text){
		playerScore.setText("Your Score: " + text);
	}
	
	public void updateInformationLable(String text){
		informationLable.setText(text);
	}
	
	public void updateTournamentColourLable(String text){
		tournamentColourLable.setText("Current tournament colour is: " + text);
	}
}