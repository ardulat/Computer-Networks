import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Frame extends JFrame {

	private JTextField textField;
	private JButton searchButton;
	private JLabel resultsLabel;
	private JButton downloadButton;

	public Frame() {
		super("Project 1");
		setLayout(new FlowLayout());
		textField = new JTextField("What are you searching for?", 20);
		textField.setFont(new Font("Serif", Font.PLAIN, 14));
		add(textField); // add textField to JFrame

		searchButton = new JButton("Search");
		add(searchButton);
		resultsLabel = new JLabel();
		add(resultsLabel);
		
		downloadButton = new JButton("Download");
		add(downloadButton);

		ButtonHandler handler = new ButtonHandler();
		searchButton.addActionListener(handler);
		downloadButton.addActionListener(handler);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() == "Search") {
				resultsLabel.setText(textField.getText());
			} else {
				resultsLabel.setText("You are downloading");
			}
		}
	}
}
