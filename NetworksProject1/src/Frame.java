import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Frame extends JFrame {

	private JTextField textField;
	private JButton searchButton;
	private JButton downloadButton;
	private List<Object[]> list ;

	public Frame() {
		super("Project 1");
		setLayout(new FlowLayout());
		textField = new JTextField("What are you searching for?", 20);
		textField.setFont(new Font("Serif", Font.PLAIN, 14));
		add(textField); // add textField to JFrame

		searchButton = new JButton("Search");
		add(searchButton);

		Object columnNames[] = { "Name", "Last Modified", "Size" };
		list =  new ArrayList<Object[]> (1);
		String[] empty = new String[] {"", "", ""};
		list.add(empty);
		Object[][] rowData = (Object[][]) list.toArray();
		JTable table = new JTable( rowData, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		downloadButton = new JButton("Download");
		add(downloadButton);

		ButtonHandler handler = new ButtonHandler();
		searchButton.addActionListener(handler);
		downloadButton.addActionListener(handler);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() == "Search") {
				list.get(0)[0] = "index.html";
				list.get(0)[1] = "12/05/16";
				list.get(0)[2] = "5 kb";

			} else {
			}
		}
	}
}
