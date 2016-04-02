package comp3004.ivanhoe;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ClientGUI extends Client{

	private JFrame frmMain;

	private JPanel informationPanel;
	private JPanel displaysPanel;
	private JPanel handPanel;
	private JPanel actionArea;
	private JPanel playerDisplayPanel;
	private JPanel[] opponentPanle;

	private JLabel informationLable = new JLabel();
	private JLabel tournamentColourLable = new JLabel();
	private JLabel highestScore = new JLabel();
	private JLabel playerScore = new JLabel();

	private JScrollPane handPane;
	private JScrollPane displayPane;

	private Card selectedCard = null;
	
	private static final String ImageDirectory = (System.getProperty("user.dir") + "/src/Images/");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ClientGUI window = null;
				try {
					window = new ClientGUI();
					window.initialize();
					window.connect();
					window.frmMain.setVisible(true);
					
					Thread t = new Thread(window.new Updater());
					t.start();
					
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
		opponentPanle = new JPanel[4];
		//initialize();
	}

	protected boolean connect(){
		InetAddress adr = null;
		String add = null;
		int po = 0;
		
		while(true){
			add = (String) JOptionPane.showInputDialog(frmMain.getContentPane(), "Enter a IP address to connect to","Connect", JOptionPane.QUESTION_MESSAGE );
			
			if( add == null || add.length() == 0){
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "invalid IP", "IP Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			
			try {
				adr = InetAddress.getByName(add);
				break;
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "invalid IP", "IP Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while(true){
			String p = (String) JOptionPane.showInputDialog(frmMain.getContentPane(), "Enter a port to connect to","Connect", JOptionPane.QUESTION_MESSAGE );
			
			try{
				po = Integer.parseInt(p);
				break;
			} catch(NumberFormatException err){
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "Invalid port", "Port Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return super.connect(add, po);
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
				if(isActiveTurn){
					if(selectedCard != null){
						sendCardsToBePlayed();
					} else {
						//no card selected
						JOptionPane.showMessageDialog(actionArea, "Select a card to play or withdraw", "Cannot play nothing", JOptionPane.ERROR_MESSAGE);
						}
				} else {
					//not players turn
					JOptionPane.showMessageDialog(actionArea, "Cannot play card when it is not your turn", "Playing card error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		//End turn button pressed
		endTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isActiveTurn){
					//if player's turn
					send(Optcodes.ClientEndTurn);
					isActiveTurn = false;
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
				if(theBoard.hand != null && !theBoard.hand.isEmpty()){
					for(Card x : theBoard.hand){
						//player has ivanhoe
						if(x.getCardName() == "Ivanhoe"){
							send(Optcodes.Ivanhoe);
						} else {
							//player does not have ivanhoe
							JOptionPane.showMessageDialog(actionArea, "You do not have the card Ivanhoe to play", "Ivanhoe Error", JOptionPane.ERROR_MESSAGE);
							break;
						}
					}
				}
			}
		});

		//Withdraw Button Pressed
		withdrawButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isActiveTurn){
					//is players turn
					send(Optcodes.ClientWithdraw);
					isActiveTurn = false;
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
		handPane.setPreferredSize(new Dimension(0, 220));
	}

	private void initializeDisplayPanel(){
		int numplayers = theBoard.players.size();
		
		displaysPanel = new JPanel();
		displaysPanel.setBackground(Color.gray);
		displaysPanel.setLayout(new GridLayout(0, 1));

		displayPane = new JScrollPane(displaysPanel);

		playerDisplayPanel = new JPanel();
		playerDisplayPanel.setLayout(new FlowLayout());
		playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Player"));

		
		
		for(int i=0; i<numplayers; i++){
			opponentPanle[i] = new JPanel();
			opponentPanle[i].setLayout(new FlowLayout());
			opponentPanle[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent"));
		
			displaysPanel.add(opponentPanle[i]);
		}
		
		displaysPanel.add(playerDisplayPanel);
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
		frmMain.setBounds(100, 100, 1300, 810);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Initialize all GUI Pieces
		initializeActionArea();
		initializeHandPanel();
		initializeInformationPanel();
		//initializeDisplayPanel();

		frmMain.getContentPane().add(informationPanel, BorderLayout.NORTH);
		//frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
		frmMain.getContentPane().add(handPane, BorderLayout.SOUTH);
		frmMain.getContentPane().add(actionArea, BorderLayout.WEST);

	}

	//Update functions

	public void updateHand(){
		//clear panel
		handPanel.removeAll();
		
		//add all cards from hand
		for(Card x: theBoard.hand){
			BufferedImage ba = null;
			try{
				ba = ImageIO.read(new File(ImageDirectory + x.getCardName()+ ".bmp"));
			} catch (IOException e){
				e.printStackTrace();
			}

			JButton button = new JButton(new ImageIcon(ba));
			button.setName(x.getCardName());
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setToolTipText(x.getCardName());

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for(Card x: theBoard.hand){
						if(x.getCardName() == button.getName()){
							selectedCard = x;
							updateInformationLable("Selected card: " + x.getCardName());
							break;
						}
					}
				}
			});

			handPanel.add(button);

		}
		//repaint
		handPanel.revalidate();
		//handPanel.repaint();
		
		handPane.validate();
		handPane.repaint();
	}

	public void updateDisplayPanel(){
		for(List<Card> displays : theBoard.boards){
			if(theBoard.boards.indexOf(displays) == 0){
				playerDisplayPanel.removeAll();
				playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Display, Player number: " + playerNum));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLable = new JLabel(new ImageIcon(img));
					imgLable.setToolTipText(x.getCardName());
					playerDisplayPanel.add(imgLable);
				}
				playerDisplayPanel.revalidate();
				displaysPanel.revalidate();

			} else {
				//opponent displays
				opponentPanle[theBoard.boards.indexOf(displays) -1].removeAll();
				opponentPanle[theBoard.boards.indexOf(displays) -1].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + theBoard.boards.indexOf(displays)));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLable = new JLabel(new ImageIcon(img));
					imgLable.setToolTipText(x.getCardName());
					opponentPanle[theBoard.boards.indexOf(displays) -1].add(imgLable);

				}
				opponentPanle[theBoard.boards.indexOf(displays) -1].revalidate();
				displaysPanel.revalidate();
			}
		}
		displayPane.revalidate();
	}
	public void UpdateInformationPanels(){
		int highest = theBoard.points.get(0);
		
		for(Integer x: theBoard.points){
			if(x > highest){
				highest = x;
			}
		}
		
		highestScore.setText("High Score: " + highest );
		playerScore.setText("Your Score: " + theBoard.points.get(0));
		tournamentColourLable.setText("Current tournament colour is: " +  theBoard.currColour);
	}
	
	public void handleClientWithdraw(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "You have withdrawn from the game", "GG", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void handleInvalidCard(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "Card Cannot be played", "Card Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void handleSuccessfulCardPlay(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "Card Played", "Card Played", JOptionPane.INFORMATION_MESSAGE);
	}

	public void updateInformationLable(String text){
		informationLable.removeAll();
		informationLable.setText(text);
		informationLable.revalidate();
	}
	
	@Override
	protected void sendCardsToBePlayed(){
		send(theBoard.hand.indexOf(selectedCard));
	}
	
	@Override
	public void handleActiveTurn(){
		if(!isActiveTurn){
			JOptionPane.showMessageDialog(frmMain.getContentPane(), "It is your turn", "Your Turn", JOptionPane.INFORMATION_MESSAGE);
			isActiveTurn = true;
		}
	}
	
	@Override
	protected void handleGetTournamentColour(){
		String[] options = {"Purple", "Green", "Red", "Blue", "Yellow"};
		int x = 0;
		String s;
		while (true){
			s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a Tournament Colour","Tournament Colour", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			try {
				x = Integer.parseInt(s);
			} catch (NumberFormatException e){ }
			
			if(x == JOptionPane.CANCEL_OPTION){
				continue;
			}
			if ((s != null) && (s.length() > 0)) {
				break;
			}
		}
		
		send(Arrays.asList(options).indexOf(s) + 1);
		
	}
	
	@Override
	protected void handleUpdateBoardState(){
		boolean firstdraw = false;
		if(theBoard == null){
			firstdraw = true;
		}
		super.handleUpdateBoardState();
		
		if(firstdraw){
			initializeDisplayPanel();
			frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
			frmMain.getContentPane().revalidate();
		}
		
		updateDisplayPanel();
		updateHand();
		UpdateInformationPanels();
		
	}

	@Override
	protected void mainLoop(){
		playerNum = (int) get();	//get player number from server

		while(true){
			Object o = get();
			print("getting optcode from server " + o.getClass().getName() + " " + o.toString());
			int optcode = (int) o;

			switch(optcode) {
			case Optcodes.ClientGetColourChoice:
				handleGetTournamentColour();
				break;
			case Optcodes.ClientUpdateBoardState:
				handleUpdateBoardState();
				break;
			case Optcodes.ClientGetCardsToBePlayed:
				//sendCardsToBePlayed();
				break;
			case Optcodes.InvalidCard:
				handleInvalidCard();
				break;
			case Optcodes.SuccessfulCardPlay:
				handleSuccessfulCardPlay();
				break;
			case Optcodes.ClientWithdraw:
				handleClientWithdraw();
				break;
			case Optcodes.ClientGetTokenChoice:
				handleTokenChoice();
				break;
			case Optcodes.ClientGetActionCardTarget:
				getActionCardTargets();
				break; 
			case Optcodes.ClientActiveTurn:
				handleActiveTurn();
				break;
			case Optcodes.ClientNotActiveTurn:
				isActiveTurn = false;
				break;
			default: new Exception("Unexpected Value");
				break;
			}
		}
	}
	
	private class Updater implements Runnable{

		@Override
		public void run() {
			ClientGUI.this.mainLoop();
		}
	}
}