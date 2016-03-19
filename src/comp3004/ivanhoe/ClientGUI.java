package comp3004.ivanhoe;

import java.awt.EventQueue;

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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;

public class ClientGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
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
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 816, 465);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{114, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 44, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel Opponent1 = new JPanel();
		GridBagConstraints gbc_Opponent1 = new GridBagConstraints();
		Opponent1.setBorder(BorderFactory.createLineBorder(Color.black));
		gbc_Opponent1.gridwidth = 6;
		gbc_Opponent1.gridheight = 4;
		gbc_Opponent1.insets = new Insets(0, 0, 5, 5);
		gbc_Opponent1.fill = GridBagConstraints.BOTH;
		gbc_Opponent1.gridx = 0;
		gbc_Opponent1.gridy = 0;
		frame.getContentPane().add(Opponent1, gbc_Opponent1);
		GridBagLayout gbl_Opponent1 = new GridBagLayout();
		gbl_Opponent1.columnWidths = new int[]{30, 0, 11, 0};
		gbl_Opponent1.rowHeights = new int[]{0, 0, 17, 0, 0};
		gbl_Opponent1.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_Opponent1.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		Opponent1.setLayout(gbl_Opponent1);
		
		JLabel lblPlayer = new JLabel("Opponent 1");
		GridBagConstraints gbc_lblPlayer = new GridBagConstraints();
		gbc_lblPlayer.gridwidth = 3;
		gbc_lblPlayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayer.gridx = 0;
		gbc_lblPlayer.gridy = 0;
		Opponent1.add(lblPlayer, gbc_lblPlayer);
		
		JLayeredPane layeredPane = new JLayeredPane();
		GridBagConstraints gbc_layeredPane = new GridBagConstraints();
		gbc_layeredPane.gridheight = 2;
		gbc_layeredPane.gridwidth = 3;
		gbc_layeredPane.fill = GridBagConstraints.BOTH;
		gbc_layeredPane.gridx = 0;
		gbc_layeredPane.gridy = 2;
		Opponent1.add(layeredPane, gbc_layeredPane);
		
		JPanel Opponent3 = new JPanel();
		GridBagConstraints gbc_Opponent3 = new GridBagConstraints();
		gbc_Opponent3.gridwidth = 6;
		gbc_Opponent3.gridheight = 4;
		gbc_Opponent3.insets = new Insets(0, 0, 5, 5);
		gbc_Opponent3.fill = GridBagConstraints.BOTH;
		gbc_Opponent3.gridx = 0;
		gbc_Opponent3.gridy = 4;
		frame.getContentPane().add(Opponent3, gbc_Opponent3);
		Opponent3.setLayout(new CardLayout(0, 0));
		
		JPanel Opponent4 = new JPanel();
		GridBagConstraints gbc_Opponent4 = new GridBagConstraints();
		gbc_Opponent4.gridheight = 4;
		gbc_Opponent4.gridwidth = 8;
		gbc_Opponent4.insets = new Insets(0, 0, 5, 5);
		gbc_Opponent4.fill = GridBagConstraints.BOTH;
		gbc_Opponent4.gridx = 6;
		gbc_Opponent4.gridy = 4;
		frame.getContentPane().add(Opponent4, gbc_Opponent4);
		GridBagLayout gbl_Opponent4 = new GridBagLayout();
		gbl_Opponent4.columnWidths = new int[]{0};
		gbl_Opponent4.rowHeights = new int[]{0};
		gbl_Opponent4.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_Opponent4.rowWeights = new double[]{Double.MIN_VALUE};
		Opponent4.setLayout(gbl_Opponent4);
		
		JPanel UserPanel = new JPanel();
		GridBagConstraints gbc_UserPanel = new GridBagConstraints();
		gbc_UserPanel.gridheight = 3;
		gbc_UserPanel.gridwidth = 14;
		gbc_UserPanel.insets = new Insets(0, 0, 0, 5);
		gbc_UserPanel.fill = GridBagConstraints.BOTH;
		gbc_UserPanel.gridx = 0;
		gbc_UserPanel.gridy = 8;
		frame.getContentPane().add(UserPanel, gbc_UserPanel);
		GridBagLayout gbl_UserPanel = new GridBagLayout();
		gbl_UserPanel.columnWidths = new int[]{0};
		gbl_UserPanel.rowHeights = new int[]{0};
		gbl_UserPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_UserPanel.rowWeights = new double[]{Double.MIN_VALUE};
		UserPanel.setLayout(gbl_UserPanel);
		
		TextArea EventsTextArea = new TextArea();
		EventsTextArea.setBackground(Color.WHITE);
		EventsTextArea.setForeground(Color.BLACK);
		EventsTextArea.setEditable(false);
		GridBagConstraints gbc_EventsTextArea = new GridBagConstraints();
		gbc_EventsTextArea.gridheight = 2;
		gbc_EventsTextArea.insets = new Insets(0, 0, 5, 0);
		gbc_EventsTextArea.gridx = 14;
		gbc_EventsTextArea.gridy = 8;
		frame.getContentPane().add(EventsTextArea, gbc_EventsTextArea);
	}
}