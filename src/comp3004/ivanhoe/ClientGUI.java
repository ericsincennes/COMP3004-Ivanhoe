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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
		opponentPanle = new JPanel[4];
		initialize();
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
						for(Card x : theBoard.hand){
							int a = theBoard.hand.indexOf(x);
							send(a);
						}
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


		//updateHand();

		//TODO REMOVE TEST CODE
		/*
		for(int i=0; i<20; i++){
			BufferedImage ba = null;
			try {
				ba = ImageIO.read(new File(ImageDirectory + "Adapt.bmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			JButton jb = new JButton(new ImageIcon(ba));
			jb.setBorder(BorderFactory.createEmptyBorder());
			jb.setName("Adapt " + i);
			jb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					updateInformationLable("Selected card: " + e.toString());
				}
			});

			handPanel.add(jb);

			handPane.revalidate();
		}
		 */
	}

	private void initializeDisplayPanel(){
		displaysPanel = new JPanel();
		displaysPanel.setBackground(Color.cyan);
		displaysPanel.setLayout(new GridLayout(5, 1));

		displayPane = new JScrollPane(displaysPanel);

		playerDisplayPanel = new JPanel();
		playerDisplayPanel.setLayout(new FlowLayout());
		playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Player"));

		opponentPanle[0] = new JPanel();
		opponentPanle[0].setLayout(new FlowLayout());
		opponentPanle[0].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent"));

		opponentPanle[1] = new JPanel();
		opponentPanle[1].setLayout(new FlowLayout());
		opponentPanle[1].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent"));

		opponentPanle[2] = new JPanel();
		opponentPanle[2].setLayout(new FlowLayout());
		opponentPanle[2].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent"));

		opponentPanle[3] = new JPanel();
		opponentPanle[3].setLayout(new FlowLayout());
		opponentPanle[3].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent"));

		displaysPanel.add(opponentPanle[0]);
		displaysPanel.add(opponentPanle[1]);
		displaysPanel.add(opponentPanle[2]);
		displaysPanel.add(opponentPanle[3]);
		displaysPanel.add(playerDisplayPanel);

		//TODO remove test code
		/*
		for(int i=0; i<4; i++){
			JPanel oponentDisplay = new JPanel();
			oponentDisplay.setLayout(new FlowLayout());
			oponentDisplay.setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + (i+1)));
			for(int b=0; b<10; b++){
				BufferedImage ba = null;
				try {
					ba = ImageIO.read(new File(ImageDirectory + "Adapt.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				//JButton img = new JButton(new ImageIcon(ba));
				JLabel img = new JLabel(new ImageIcon(ba));
				oponentDisplay.add(img);

			}
			displaysPanel.add(oponentDisplay);
		}

		JPanel playerDisplay = new JPanel();
		playerDisplay.setLayout(new FlowLayout());
		playerDisplay.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Display" ));
		for(int b=0; b<10; b++){
			BufferedImage ba = null;
			try {
				ba = ImageIO.read(new File(ImageDirectory + "Adapt.bmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//JButton img = new JButton(new ImageIcon(ba));
			JLabel img = new JLabel(new ImageIcon(ba));
			playerDisplay.add(img);

		}
		displaysPanel.add(playerDisplay);

		displayPane.revalidate();
		 */
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

		//Initialize all GUI Pieces
		initializeActionArea();
		initializeHandPanel();
		initializeInformationPanel();
		initializeDisplayPanel();

		frmMain.getContentPane().add(informationPanel, BorderLayout.NORTH);
		frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
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
		handPane.revalidate();
	}

	public void updateDisplayPanel(){
		for(List<Card> displays : theBoard.boards){
			if(theBoard.boards.indexOf(displays) == 0){
				playerDisplayPanel.removeAll();
				playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Display"));
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

	public void updateHighestScore(String text){
		highestScore.setText("High Score: " + text);
	}

	public void updatePlayerScore(String text){
		playerScore.setText("Your Score: " + text);
	}

	public void updateInformationLable(String text){
		informationLable.removeAll();
		informationLable.setText(text);
		informationLable.revalidate();
	}

	public void updateTournamentColourLable(String text){
		tournamentColourLable.setText("Current tournament colour is: " + text);
	}

	protected void handleGetTournamentColour(){
		super.handleGetTournamentColour();
		/*
		String[] options = {"Purple", "Green", "Red", "Blue", "Yellow"};
		String s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a Tournament Colour","Tournament Colour", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		if ((s != null) && (s.length() > 0)) {
			send(Arrays.asList(options).indexOf(s));
		}
		*/
	}
	
	protected void handleUpdateBoardState(){
		super.handleUpdateBoardState();
		
		updateDisplayPanel();
		updateHand();
	}
}