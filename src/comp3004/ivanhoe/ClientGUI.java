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
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;

public class ClientGUI extends Client{

	private JFrame frmMain;

	private JPanel informationPanel;
	private JPanel displaysPanel;
	private JPanel handPanel;
	private JPanel actionArea;
	private JPanel playerDisplayPanel;
	private JPanel[] opponentPanel;

	private JLabel informationLabel = new JLabel();
	private JLabel tournamentColourLabel = new JLabel();
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
		opponentPanel = new JPanel[4];
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

		
		
		for(int i=0; i<numplayers-1; i++){
			opponentPanel[i] = new JPanel();
			opponentPanel[i].setLayout(new FlowLayout());
			opponentPanel[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + i));
			opponentPanel[i].setName("Opponent " + i);
			displaysPanel.add(opponentPanel[i]);
		}
		
		displaysPanel.add(playerDisplayPanel);
	}
	
	private void initializeInformationPanel(){
		informationPanel = new JPanel();
		informationPanel.setBackground(Color.orange);
		informationPanel.setLayout(new GridLayout(2, 1));

		informationLabel.setText("no card selected");
		informationLabel.setHorizontalAlignment(JLabel.CENTER);

		tournamentColourLabel.setText("Tournament colour is: Tournament Colour");
		tournamentColourLabel.setHorizontalAlignment(JLabel.CENTER);

		informationPanel.add(informationLabel);
		informationPanel.add(tournamentColourLabel);
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
				opponentPanel[theBoard.boards.indexOf(displays) -1].removeAll();
				opponentPanel[theBoard.boards.indexOf(displays) -1].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent ID: " + theBoard.players.get(theBoard.boards.indexOf(displays)) ));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLable = new JLabel(new ImageIcon(img));
					imgLable.setToolTipText(x.getCardName());
					opponentPanel[theBoard.boards.indexOf(displays) -1].add(imgLable);

				}
				opponentPanel[theBoard.boards.indexOf(displays) -1].revalidate();
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
		if(theBoard.currColour == null){
			tournamentColourLabel.setText("Current tournament is being decided");
		} else {
			tournamentColourLabel.setText("Current tournament colour is: " +  theBoard.currColour);
		}
		
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
		informationLabel.removeAll();
		informationLabel.setText(text);
		informationLabel.revalidate();
	}
	
	@Override
	protected void sendCardsToBePlayed(){
		if(selectedCard.getCardType() == CardType.Action){
			actionCardHelper(selectedCard.getCardName());
		} else {
			send(theBoard.hand.indexOf(selectedCard));
		}
	}
	
	private void actionCardHelper(String name){
		String[] choices;
		String s = null;
		int x = 0;
		List<Object> targets;
		boolean cancelClicked = false;
		
		switch(name){
		case "Unhorse": //target: CardColour
			//color changes from purple to red, blue or yellow
			if(theBoard.currColour == CardColour.Purple){
				choices = new String[]{"Red", "Blue", "Yellow"};
				while (!cancelClicked){
					s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a Tournament Colour","Tournament Colour", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					
					try {
						x = Integer.parseInt(s);
					} catch (NumberFormatException e){ }
					
					if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
					if ((s != null) && (s.length() > 0)) { break; }
				}
				
				if(!cancelClicked){
					for(CardColour cc : CardColour.values()){
						if(cc.name().equals(s)){
							send(theBoard.hand.indexOf(selectedCard));
							targets = new ArrayList<Object>();
							targets.add(cc);
							send(targets);
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "Current Tournament colour is not Purple", "Card Cannot be played", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "Change Weapon": //target: CardColour
			// color changes from red, blue or yellow to a different one of these colors
			if(theBoard.currColour == CardColour.Red || 
			theBoard.currColour == CardColour.Blue ||
			theBoard.currColour == CardColour.Yellow){ 
				choices = new String[]{"Red", "Blue", "Yellow"};
				
				while (true){
					s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a Tournament Colour.","Tournament Colour", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					
					try {
						x = Integer.parseInt(s);
					} catch (NumberFormatException e){ }
					
					if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
					//Cannot change colour to current tournament colour
					if(s.equals(theBoard.currColour.name())){ 
						JOptionPane.showMessageDialog(frmMain.getContentPane(), "Cannot change colour to current tournament colour", "Card Cannot be played", JOptionPane.ERROR_MESSAGE);
						continue; 
					} 
					if ((s != null) && (s.length() > 0)) { break; }
				}
				if(!cancelClicked){
					for(CardColour cc : CardColour.values()){
						if(cc.name().equals(s)){
							send(theBoard.hand.indexOf(selectedCard));
							targets = new ArrayList<Object>();
							targets.add(cc);
							send(targets);
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "Current Tournament colour is not red, blue or yellow.", "Card Cannot be played", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "Drop Weapon":
			//color changes from red, blue or yellow to green
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			targets.add(CardColour.Green);
			send(targets);
			break;
		case "Break Lance": //target: Player
			//Force one opponent to discard all purple cards from his display
			choices = new String[theBoard.players.size()-1];
			for(int i=0; i< choices.length; i++){
				choices[i] = "Opponent " + i;
			}
			while (!cancelClicked){
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove all Purple", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				if ((s != null) && (s.length() > 0)) { break; }
			}
			
			if(!cancelClicked){
				int c = Arrays.asList(choices).indexOf(s) + 1;
				send(theBoard.hand.indexOf(selectedCard));
				targets = new ArrayList<Object>();
				targets.add(c);
				send(targets);
			}

			break;
		case "Riposte": //target Player, Card (to be taken)
			//Take the last card played on any one opponent’s 
			//display and add it to your own display.
			choices = new String[theBoard.players.size()-1];
			for(int i=0; i< choices.length; i++){
				choices[i] = "Opponent " + i;
			}
			targets = new ArrayList<Object>();
			
			while (!cancelClicked){
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove last card played", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) { 
					int c = Arrays.asList(choices).indexOf(s) + 1; //in theboard.players index 0 = you
					List<Card> li = theBoard.boards.get(c); //get cards of opponent
					
					targets.add(theBoard.players.get(c)); //add opponent to targets
					targets.add(li.get(li.size() -1)); //add opponent card to targets
					break;
				}
			}
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			
			break;
		case "Dodge": //target: Player, String (cardname)
			//Discard any one card from any one opponent’s display.
			choices = new String[theBoard.players.size()-1];
			for(int i=0; i< choices.length; i++){
				choices[i] = "Opponent " + i;
			}
			targets = new ArrayList<Object>();
			while (!cancelClicked){
				//Get target player
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) { 
					int c = Arrays.asList(choices).indexOf(s) + 1; //in theboard.players index 0 = you
					targets.add(theBoard.players.get(c)); //add opponent to targets
					List<Card> li = theBoard.boards.get(c); //get cards of opponent
					
					List<String> cardnames = new ArrayList<String>();
					choices = cardnames.toArray(choices);
					
					while(!cancelClicked){
						//get target card from target player
						s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target card.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
						
						try {
							x = Integer.parseInt(s);
						} catch (NumberFormatException e){ }
						
						if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
						
						if (!cancelClicked && (s != null) && (s.length() > 0)) {
							targets.add(s);
							break;
						}
					}
					break;
				}
			}
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			
			break;
		case "Retreat": //target: String (cardname)
			//Take any one card from your own display back into your hand
			List<Card> playerBoard = theBoard.boards.get(0);
			choices = null;
			choices = playerBoard.toArray(choices);
			targets = new ArrayList<Object>();
			while(!cancelClicked){
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose card to add to hand.","Take back card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) { 
					targets.add(s);
					break;
				}
			}
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			
			break;
		case "Knock Down": //target: Player
			//Draw at random one card from any one opponent’s hand and 
			//add it to your hand, without revealing the card to other opponents.
			choices = new String[theBoard.players.size()-1];
			for(int i=0; i< choices.length; i++){
				choices[i] = "Opponent " + i;
			}
			targets = new ArrayList<Object>();
			while (!cancelClicked){
				//Get target player
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Take card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) { 
					x = Arrays.asList(choices).indexOf(s) + 1;
					targets.add(theBoard.players.get(x));
					break;
				}
			}
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			
			break;
		case "Outmaneuver": //target: none
			//All opponents must remove the last card played on their displays
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			send(targets);
			break;
		case "Charge": //target: none
			//Identify the lowest value card throughout all displays. 
			//All players must discard all cards of this value from their displays.
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			send(targets);
			break;
		case "Countercharge": //target: none
			//Identify the highest value card throughout all displays.
			//All players must discard all cards of this value from their displays.
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			send(targets);
			break;
		case "Disgrace":
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			send(targets);
			break;
		case "Adapt":
			break;
		case "Outwit": //target: Player, Card (yours), Card (opp's card) 
			//Place one of your faceup cards in front of an opponent, 
			//and take one faceup card from this opponent 
			//and place it face up in front of yourself. 
			//This may include the SHIELD and STUNNED cards.
			
			
			break;
		case "Shield":
			send(theBoard.hand.indexOf(selectedCard));
			targets = new ArrayList<Object>();
			send(targets);
			break;
		case "Stunned": //target: none
			//Place this card separately face up in front of any one opponent.
			//As long as a player has the STUNNED card in front of him, 
			//he may add only one new card to his display each turn.
			choices = new String[theBoard.players.size()-1];
			for(int i=0; i< choices.length; i++){
				choices[i] = "Opponent " + i;
			}
			
			while (true){ 
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }
				
				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) { 
					x = Arrays.asList(choices).indexOf(s) + 1;
					targets = new ArrayList<Object>();
					targets.add(theBoard.players.get(x));
					break;
				}
			}
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			break;
			
		}
	}
	
	protected void handleTokenChoice(){
		String[] options = {"Purple", "Green", "Red", "Blue", "Yellow"};
		int x = 0;
		String s;
		
		if(theBoard.currColour == CardColour.Purple){
			while (true){
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"You won a Purple tournament! Choose a token of your choice","Get a Token", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
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
			//display to user
			JOptionPane.showMessageDialog(frmMain.getContentPane(), "Token received of colour " + s, "Token Received", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			
			x = Arrays.asList(options).indexOf(theBoard.currColour.name());
			send(x+1);
			JOptionPane.showMessageDialog(frmMain.getContentPane(), "You won a round! Token received of colour " + theBoard.currColour.name(), "Token Received", JOptionPane.INFORMATION_MESSAGE);
		}
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