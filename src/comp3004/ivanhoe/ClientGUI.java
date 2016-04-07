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
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	private JPanel[] opponentActionPanel;
	private JPanel actionCardPanel;
	private JPanel playerActionPanel;
	
	private JLabel informationLabel = new JLabel();
	private JLabel tournamentColourLabel = new JLabel();
	private JLabel highestScore = new JLabel();
	private JLabel playerScore = new JLabel();
	private JLabel tokens = new JLabel();
	
	private JScrollPane handPane;
	private JScrollPane displayPane;
	private JScrollPane actionCardPane;
	
	private Card selectedCard = null;
	
	private static final String ImageDirectory = (System.getProperty("user.dir") + "/Images/");

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
		opponentActionPanel = new JPanel[4];
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
		
		//get player id's
		Long[] playerid = new Long[numplayers];
		playerid = theBoard.players.toArray(playerid);
		
		displaysPanel = new JPanel();
		displaysPanel.setBackground(Color.gray);
		displaysPanel.setLayout(new GridLayout(0, 1));

		displayPane = new JScrollPane(displaysPanel);

		playerDisplayPanel = new JPanel();
		playerDisplayPanel.setLayout(new FlowLayout());
		playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "You " + playerid[0]));

		
		
		for(int i=0; i<numplayers-1; i++){
			opponentPanel[i] = new JPanel();
			opponentPanel[i].setLayout(new FlowLayout());
			opponentPanel[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + playerid[i+1]));
			opponentPanel[i].setName("Opponent " + playerid[i+1]);
			displaysPanel.add(opponentPanel[i]);
		}
		
		displaysPanel.add(playerDisplayPanel);
	}
	
	private void initializeActionCardPanel(){
		int numplayers = theBoard.players.size();
		
		//get player id's
		Long[] playerid = new Long[numplayers];
		playerid = theBoard.players.toArray(playerid);
				
		actionCardPanel = new JPanel();
		actionCardPanel.setBackground(Color.gray);
		actionCardPanel.setLayout(new GridLayout(0, 1));
		
		actionCardPane = new JScrollPane(actionCardPanel);
		actionCardPane.setPreferredSize(new Dimension(205, 145));
		
		playerActionPanel = new JPanel();
		playerActionPanel.setLayout(new FlowLayout());
		playerActionPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Action Cards"));
		
		for(int i=0; i<numplayers-1; i++){
			opponentActionPanel[i] = new JPanel();
			opponentActionPanel[i].setLayout(new FlowLayout());
			opponentActionPanel[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + playerid[i+1] +"'s Action Cards"));
			opponentActionPanel[i].setName("Opponent " + playerid[i+1]);
			actionCardPanel.add(opponentActionPanel[i]);
		}
		actionCardPanel.add(playerActionPanel);
	}
	
	private void initializeInformationPanel(){
		informationPanel = new JPanel();
		informationPanel.setBackground(Color.orange);
		informationPanel.setLayout(new GridLayout(3, 1));

		informationLabel.setText("no card selected");
		informationLabel.setHorizontalAlignment(JLabel.CENTER);

		tournamentColourLabel.setText("Tournament colour is being selected");
		tournamentColourLabel.setHorizontalAlignment(JLabel.CENTER);

		tokens.setText("You have no Tokens");
		tokens.setHorizontalAlignment(JLabel.CENTER);
		
		informationPanel.add(informationLabel);
		informationPanel.add(tournamentColourLabel);
		informationPanel.add(tokens);
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
							updateInformationLabel("Selected card: " + x.getCardName());
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
				//playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Board"));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					playerDisplayPanel.add(imgLabel);
				}
				playerDisplayPanel.revalidate();
				playerDisplayPanel.repaint();
				displaysPanel.revalidate();
				displaysPanel.repaint();;

			} else {
				//opponent displays
				opponentPanel[theBoard.boards.indexOf(displays) -1].removeAll();
				//opponentPanel[theBoard.boards.indexOf(displays) -1].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent ID: " + theBoard.players.get(theBoard.boards.indexOf(displays)) ));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					opponentPanel[theBoard.boards.indexOf(displays) -1].add(imgLabel);

				}
				opponentPanel[theBoard.boards.indexOf(displays) -1].revalidate();
				displaysPanel.repaint();
				displaysPanel.revalidate();
			}
		}
		displayPane.repaint();
		displayPane.revalidate();
	}
	
	public void updateActionCardPanel(){
		for(List<Card> actionBoard : theBoard.actionBoards){
			if(theBoard.actionBoards.indexOf(actionBoard) == 0){
				playerActionPanel.removeAll();
				//playerActionPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Action Cards"));
				for(Card x: actionBoard){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					playerActionPanel.add(imgLabel);
				}
				playerActionPanel.revalidate();
				actionCardPanel.revalidate();
			} else {
				opponentActionPanel[theBoard.actionBoards.indexOf(actionBoard) -1].removeAll();
				for(Card x: actionBoard){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					opponentActionPanel[theBoard.actionBoards.indexOf(actionBoard)-1].add(imgLabel);
				}
				opponentActionPanel[theBoard.actionBoards.indexOf(actionBoard)-1].revalidate();
				actionCardPanel.revalidate();
			}
		}
		actionCardPane.revalidate();
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

	public void updateTokenLabel(){
		String playerTokens = "Your tokens: ";
		String[] opponentTokens = new String[theBoard.tokens.size()];
		
		//get player id's
		Long[] playerid = new Long[theBoard.tokens.size()];
		playerid = theBoard.players.toArray(playerid);
		
		for(int i =0; i<opponentTokens.length-1; i++){
			opponentTokens[i] = "- Opponent " + playerid[i+1] + ": ";
		}
		
		for(List<CardColour> tokensList : theBoard.tokens){
			if(theBoard.tokens.indexOf(tokensList) == 0){
				for(CardColour c : tokensList){
					playerTokens += c.name() +" ";
				}
			} else {
				for(CardColour c : tokensList){
					opponentTokens[theBoard.tokens.indexOf(tokensList) -1] += c.name() +" ";
				}	
			}
		}
		//concat opponents tokens into one string
		StringBuilder builder = new StringBuilder();
		for(String s : opponentTokens) {
			if(s != null){
				builder.append(s);
			}
		}
		String opponents =  builder.toString();
		
		tokens.setText(playerTokens + opponents);
	}
	
	public void updateInformationLabel(String text){
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
		case "Adapt":
			send(new ArrayList<Object>());
			break;
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
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
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
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
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
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
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
					for(Card card: li){
						cardnames.add(card.getCardName());
					}
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
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
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
		case "Outwit": //target: Player, Card (yours), Card (opp's card) 
			//Place one of your faceup cards in front of an opponent, 
			//and take one faceup card from this opponent 
			//and place it face up in front of yourself. 
			//This may include the SHIELD and STUNNED cards.
			
			//get list of opponents for JOptionPane
			choices =  new String[theBoard.players.size()-1];
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
			}
			
			targets = new ArrayList<Object>();
			cancelClicked = false;
			while(!cancelClicked){
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			
				try {
					x = Integer.parseInt(s);
				} catch (NumberFormatException e){ }

				if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
				
				if (!cancelClicked && (s != null) && (s.length() > 0)) {
					//if opponent selected get index of opponent
					x = theBoard.players.indexOf(Arrays.asList(choices).indexOf(s) + 1);
					
					//add to targets
					targets.add(theBoard.players.get(x));
					
					for(List<Card> l : theBoard.boards){
						if(theBoard.boards.indexOf(l) == x){
							choices = new String[l.size()];
							for(Card a : l){
								choices[l.indexOf(a)] = a.getCardName();
							}
						}
					}
					s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target Card.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					
					try {
						x = Integer.parseInt(s);
					} catch (NumberFormatException e){ }

					if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }
					
					if (!cancelClicked && (s != null) && (s.length() > 0)) {
						//if card was selected add card to targets
						targets.add(s);
						
						//get your hand as choices
						choices = new String[theBoard.boards.get(0).size()];
						for(Card a : theBoard.boards.get(0)){
							choices[theBoard.boards.get(0).indexOf(a)] = a.getCardName();
						}
						
						s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a card to give.","Card to give", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
						
						try {
							x = Integer.parseInt(s);
						} catch (NumberFormatException e){ }

						if(x == JOptionPane.CANCEL_OPTION){ cancelClicked = true; }

						if (!cancelClicked && (s != null) && (s.length() > 0)) { 
							//if card from hand selected get index of card
							x = Arrays.asList(choices).indexOf(s);
							
							//add index to targets
							targets.add(x);
							break;
						}
					}
				}
			}
			
			if(!cancelClicked){
				send(theBoard.hand.indexOf(selectedCard));
				send(targets);
			}
			
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
			for(Long l : theBoard.players){
				if(theBoard.players.indexOf(l) != 0){
					choices[theBoard.players.indexOf(l)-1] = "Opponent " + l;
				}
			}
			
			targets = new ArrayList<Object>();
			cancelClicked = false;
			
			while (!cancelClicked){ 
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() ,"Choose a target opponent.","Remove card", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				
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
		}
	}
	
	protected void handleTokenChoice(boolean win){
		String[] options = {"Purple", "Green", "Red", "Blue", "Yellow"};
		int x = 0;
		String s;
		
		if(theBoard.currColour == CardColour.Purple){
			while (true){
				String msg = (win) ? "You won a Purple tournament! Choose a token of your choice" : "You have withdrawn with a maiden. Choose a token to lose.";
				s = (String) JOptionPane.showInputDialog(frmMain.getContentPane() , msg,"Get a Token", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
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
	
	public void handleLoseTournament() {
		String winner = (String) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "The winner was Player: " + winner, "Tournament Winner", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void handleLoseGame() {
		String winner = (String) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "The winner was Player: " + winner + "\nYou have Lost. \n", "Game Winner", JOptionPane.WARNING_MESSAGE);
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
			initializeActionCardPanel();
			
			frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
			frmMain.getContentPane().revalidate();
			
			frmMain.getContentPane().add( actionCardPane, BorderLayout.EAST);
			frmMain.getContentPane().revalidate();
		}
		
		updateDisplayPanel();
		updateActionCardPanel();
		updateHand();
		UpdateInformationPanels();
		updateTokenLabel();
		
		frmMain.revalidate();
		frmMain.repaint();
	}
	private void handleClientFailStartTournament(){
		String playerID = (String) get();
		List<Card> hand = (List<Card>) get();
		
		String msg = "Player "+ playerID + " cannot start a tournament";
		JPanel hp = new JPanel();
		hp.setLayout(new FlowLayout());
		
		for(Card x : hand){
			BufferedImage img = null;
			try{
				img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".bmp"));
			} catch (IOException e){
				e.printStackTrace();
			}
			JLabel imgLabel = new JLabel(new ImageIcon(img));
			hp.add(imgLabel);
		}
		hp.revalidate();
		
		JOptionPane.showMessageDialog(frmMain.getContentPane(), hp, msg, JOptionPane.PLAIN_MESSAGE);
	}
	
	public void handleOppActionCardPlayed() {
		String message = (String) get();
		
		//display message of what action card has been played
		JOptionPane.showMessageDialog(frmMain.getContentPane(), message, "Action card played", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void handleGameWinner(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "You have won the game!" , "Game Winner", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * msg to notify client when others' turn are over
	 * @param mode 1 if they ended, 2 if they withdrew
	 */
	private void handleOppTurnOver(int mode) {
		Long pid = (Long) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), 
				"Player " + pid + " has " + ((mode%2 == 0) ? "withdrawn." : "ended their turn."),
				"Turn Changed", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void handleGettingIvanhoed() {
		JOptionPane.showMessageDialog(frmMain.getContentPane(), 
				"Your action card just got countered by an Ivanhoe!" , "Ivanhoe'd!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void handleGetIvanhoeChoice() {
		String message = (String) get();
		String[] msgArray = new String(message).split(" ");
		String title = "Player " + msgArray[1] + " played an action card";
		
		int x = JOptionPane.showConfirmDialog(frmMain.getContentPane(), message + "\nWould you like to use Ivanhoe?\n Answer in 10s.", title, JOptionPane.YES_NO_OPTION);
		
		if(x == JOptionPane.YES_OPTION){
			send(true);
		} else if(x == JOptionPane.CLOSED_OPTION || x == JOptionPane.NO_OPTION){
			send(false);
		}
	}
	
	private void handleClientGetAdapt(){
		//Each player may only keep one card of each value in his display. All other cards
		//with the same value are discarded. Each player decides which of the matching-value
		//cards he will discard.
		List<Integer> keepingCards = new ArrayList<Integer>();
		
		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new GridLayout(0, 1));
		
		List<String> cardNames = new ArrayList<String>();
		for(Card x : theBoard.hand){
			cardNames.add(x.getCardName());
		}
		
		for(Card x : theBoard.hand){			
			BufferedImage ba = null;
			try{
				ba = ImageIO.read(new File(ImageDirectory + x.getCardName()+ ".bmp"));
			} catch (IOException e){
				e.printStackTrace();
			}
			JCheckBox jcb = new JCheckBox(new ImageIcon(ba), false);
			jcb.setName(x.getCardName());
			
			jcb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					JCheckBox source = (JCheckBox) e.getItemSelectable();
					if (e.getStateChange() == ItemEvent.SELECTED){
						int x = cardNames.indexOf(source.getName());
						keepingCards.add(x);
					} else if(e.getStateChange() == ItemEvent.DESELECTED){
						int x = cardNames.indexOf(source.getName());
						keepingCards.remove(x);
					}
				}
			});
			checkPanel.add(jcb);
		}
		checkPanel.revalidate();
		
		JOptionPane.showMessageDialog(frmMain.getContentPane(), checkPanel, "You may only keep one card of each Value", JOptionPane.PLAIN_MESSAGE);
		
		send(keepingCards);
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
			case Optcodes.ClientWinTokenChoice:
				handleTokenChoice(true);
				break;
			case Optcodes.ClientLoseTokenChoice:
				handleTokenChoice(false);
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
			case Optcodes.LoseTournament:
				handleLoseTournament();
				break;
			case Optcodes.GameOver:
				handleLoseGame();
				break;
			case Optcodes.ClientFailStartTournament:
				handleClientFailStartTournament();
				break;
			case Optcodes.ClientActionCardPlayed:
				handleOppActionCardPlayed();
				break;
			case Optcodes.ClientGetIvanhoeChoice:
				handleGetIvanhoeChoice();
				break;
			case Optcodes.ClientGetAdapt:
				handleClientGetAdapt();
				break;
			case Optcodes.GameWinner:
				handleGameWinner();
				break;
			case Optcodes.Ivanhoe:
				handleGettingIvanhoed();
				break;
			case Optcodes.OppEndTurn:
				handleOppTurnOver(1);
				break;
			case Optcodes.OppWithdraw:
				handleOppTurnOver(2);
				break;
			default: 
				new Exception("Unexpected Value");
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