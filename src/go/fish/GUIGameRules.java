/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class GUIGameRules extends JFrame 
{
	private JTextArea taRules;
	private JScrollPane scrollRules;
	private Color blue, white;
	private JLabel rulesLabel;
	private String rulesText;
		
	public GUIGameRules()
	{
		// Colors
		blue = new Color(0, 162, 255);
		white = new Color(255, 255, 255);
		
		// frame properties
		setSize(402, 263);
		setTitle("Game Rules");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
				
		getContentPane().setLayout(null);
		getContentPane().setBackground(blue);
		
		// Create components
		rulesLabel = new JLabel("Game Rules:");
		rulesLabel.setBounds(10, 11, 212, 75);
		rulesLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
		rulesLabel.setForeground(white);

		rulesText = "";
		try {
			Scanner fin = new Scanner(new File("res/txtFiles/gameRules.txt"));
			while (fin.hasNextLine()) {rulesText += fin.nextLine();}
			fin.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Could not find the file!");
		}


		// Add components
		getContentPane().add(rulesLabel);
		
		taRules = new JTextArea();
		taRules.setWrapStyleWord(true);
		taRules.setLineWrap(true);
		taRules.setEditable(false);
		taRules.setMargin(new Insets(5,10,10,10));
		taRules.setText(rulesText);
		taRules.setCaretPosition(taRules.getDocument().getLength()/5);
		
		scrollRules = new JScrollPane(taRules);
		scrollRules.setBounds(10, 72, 376, 152);
		scrollRules.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollRules);
		        
	}
	

	
}
