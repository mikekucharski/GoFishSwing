/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class GUIWelcomeScreen extends JFrame implements ActionListener 
{
	private JButton rulesButton, startButton, btnHowToPlay;
	private Color blue, black; 
	private Border loweredbevel;
	private JLabel greetings, gameTitle, fishLabel, fish2Label;
	private JTextArea creatorLabel;
	private ImageIcon fishImage, fish2Image;
	private JMenuBar mbBar;
	private JMenu mnMenu;
	private JMenuItem mnitmEasy, mnitmHard;

	
	public GUIWelcomeScreen()
	{
		// Colors
		blue = new Color(0, 162, 255);
		black = new Color(0, 0, 0);
		// Borders!
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		
		// Welcome screen properties
		setSize(603, 450);
		setVisible(true);
		setBackground(blue);
		setTitle("Welcome to Go Fish!");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// Create components
		greetings = new JLabel("Welcome To");
		greetings.setHorizontalAlignment(SwingConstants.CENTER);
		greetings.setBounds(0, 123, 597, 43);
		greetings.setFont(new Font("Verdana", Font.PLAIN, 44));
		
		gameTitle = new JLabel("Go Fish!");
		gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		gameTitle.setBounds(0, 177, 597, 111);
		gameTitle.setFont(new Font("Jokerman", Font.BOLD, 99));
		
		creatorLabel = new JTextArea();
		creatorLabel.setEditable(false);
		creatorLabel.setWrapStyleWord(true);
		creatorLabel.setBounds(0, 367, 262, 34);
		creatorLabel.setDisabledTextColor(black);
		creatorLabel.setBorder(loweredbevel);
		creatorLabel.setAlignmentX(RIGHT_ALIGNMENT);
		creatorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		creatorLabel.setText("Created by: Ashley Packard & Mike Kucharski\r\nCOMP 285 Final Project, Fall 2012");
		creatorLabel.setLineWrap(true);
		creatorLabel.setEnabled(false);
		creatorLabel.setBackground(blue);
		
		// Fish!!!!
		fishImage = new ImageIcon("res/welcomeScreen/flippedgoldfish.jpg");
		fishLabel = new JLabel(fishImage);
		fishLabel.setSize(179, 146);
		fishLabel.setLocation(408, 11);
		
		fish2Image = new ImageIcon("res/welcomeScreen/goldfish2.jpg");
		fish2Label = new JLabel(fish2Image);
		fish2Label.setSize(179, 146);
		fish2Label.setLocation(10, 11);
		
		// Buttons!
		rulesButton = new JButton("Game Rules");
		rulesButton.setBounds(29, 312, 160, 27);
		rulesButton.setFont(new Font("Verdana", Font.PLAIN, 14));
		rulesButton.setActionCommand("rules");
		rulesButton.setMnemonic(KeyEvent.VK_G);
		rulesButton.setToolTipText("The game rules can be found here.");
		// Check for button clicking (listen for action)
		rulesButton.addActionListener(this);
		
		startButton = new JButton("Start");
		startButton.setBounds(406, 312, 160, 27);
		startButton.setFont(new Font("Verdana", Font.PLAIN, 15));
		startButton.setActionCommand("start");		
		startButton.setMnemonic(KeyEvent.VK_S);	
		startButton.setToolTipText("Click to begin the game!");
		// Check for button clicking (listen for action)
		startButton.addActionListener(this);
		
		btnHowToPlay = new JButton("How To Play");
		btnHowToPlay.setBounds(215, 312, 160, 27);
		btnHowToPlay.setFont(new Font("Verdana", Font.PLAIN, 15));
		btnHowToPlay.setActionCommand("howTo");		
		btnHowToPlay.setMnemonic(KeyEvent.VK_H);	
		btnHowToPlay.setToolTipText("Find out how to play.");
		// Check for button clicking (listen for action)
		btnHowToPlay.addActionListener(this);
		
		mbBar = new JMenuBar();
		
		mnMenu = new JMenu("Choose Difficulty");
		mnMenu.setForeground(Color.BLACK);
		mnMenu.setBounds(10, 44, 170, 40);
		mbBar.add(mnMenu);
		
		mnitmEasy = new JRadioButtonMenuItem("Easy", true);
		mnMenu.add(mnitmEasy);
		mnitmEasy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mnitmEasy.setSelected(true);
				mnitmHard.setSelected(false);
			}}
		);
		
		mnitmHard = new JRadioButtonMenuItem("Hard");
		mnMenu.add(mnitmHard);
		mnitmHard.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mnitmHard.setSelected(true);
				mnitmEasy.setSelected(false);
			}}
		);
		setJMenuBar(mbBar);
		
		// Create window
		getContentPane().setBackground(blue);
		getContentPane().setLayout(null);
		getContentPane().add(greetings);
		getContentPane().add(gameTitle);
		getContentPane().add(rulesButton);
		getContentPane().add(startButton);
		getContentPane().add(btnHowToPlay);
		getContentPane().add(creatorLabel);
		getContentPane().add(fishLabel);
		getContentPane().add(fish2Label);		
		
	}
	
	public void actionPerformed(ActionEvent event) 
	{
	    if((event.getActionCommand()).equals("rules")) 
	    {
	    	new GUIGameRules();
	    }
	    else if((event.getActionCommand()).equals("start")) 
	    {
	    	dispose();
	    	
	    	GUIGameBoard d = new GUIGameBoard();
	    	
	    	int difficulty = 0;
	    	
	    	if(mnitmEasy.isSelected()) {difficulty = 1;}
	    	else if(mnitmHard.isSelected()) {difficulty = 2;}
	    	
	    	// Pass in input for difficulty from user later :D
	    	GameState gs = new GameState(d, difficulty);
	    	d.setGameState(gs);
	    	gs.dealCards();
	    	
	    	d.setVisible(true);
	    }
	    else if((event.getActionCommand()).equals("howTo"))  {new GUIHowToPlay();}
	}
}
