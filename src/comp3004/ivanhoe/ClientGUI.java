package comp3004.ivanhoe;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.JSeparator;
import javax.swing.JList;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.Color;
import java.awt.Panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.awt.BorderLayout;

public class ClientGUI{

	private JFrame frmMain;
	private JPanel currentTournamentColourPanel;
	private JPanel opponentDisplayPanels;
	private JPanel handPanel;
	private JPanel actionArea;
	
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMain = new JFrame();
		frmMain.setTitle("Ivanhoe");
		frmMain.setBounds(100, 100, 957, 761);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		currentTournamentColourPanel = new JPanel();
		currentTournamentColourPanel.setBackground(Color.orange);
		currentTournamentColourPanel.setLayout(new FlowLayout());
		currentTournamentColourPanel.add(new JLabel("Info + Current Tournament Colour", JLabel.CENTER));
		
		handPanel = new JPanel();
		handPanel.setBackground(Color.gray);
		handPanel.setLayout(new FlowLayout());
		handPanel.add(new JLabel("HAND DISPLAY AREA", JLabel.CENTER));
		
		actionArea = new JPanel();
		actionArea.setBackground(Color.green);
		actionArea.setLayout(new FlowLayout());
		actionArea.add(new JLabel("Display Area", JLabel.CENTER));
		
		frmMain.getContentPane().add(currentTournamentColourPanel, BorderLayout.NORTH);
		
		opponentDisplayPanels = new JPanel();
		opponentDisplayPanels.setBackground(Color.cyan);
		opponentDisplayPanels.setLayout(new FlowLayout());
		opponentDisplayPanels.add(new JLabel("Board Display Area", JLabel.CENTER));
		frmMain.getContentPane().add(opponentDisplayPanels, BorderLayout.CENTER);
		frmMain.getContentPane().add(handPanel, BorderLayout.SOUTH);
		frmMain.getContentPane().add(actionArea, BorderLayout.WEST);
	}
}