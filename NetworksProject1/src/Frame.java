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
import javax.swing.table.DefaultTableModel;

public class Frame extends JFrame {

	private JTextField textField;
	private JButton uploadButton;
	private JButton searchButton;
	private JButton downloadButton;
	private Object[][] data;
	private JTable table;
	DefaultTableModel dtm;

	public Frame() {
		super("Project 1");
		setLayout(new FlowLayout());
		uploadButton = new JButton("Upload");
		add(uploadButton, BorderLayout.CENTER);
		textField = new JTextField("What are you searching for?", 20);
		textField.setFont(new Font("Serif", Font.PLAIN, 14));
		add(textField); // add textField to JFrame

		searchButton = new JButton("Search");
		add(searchButton);

		table = new JTable();
		dtm = new DefaultTableModel(0, 0);

		// add header of the table
		String header[] = new String[] { "Name", "Last Modified", "Size" };

		// add header in table model
		dtm.setColumnIdentifiers(header);
		// set model into the table object
		table.setModel(dtm);

		// add row dynamically into the table
		for (int count = 1; count <= 30; count++) {
			dtm.addRow(new Object[] { "data", "data", "data" });
		}
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
				//clear table, don't need anything to mess there
				int rowCount = dtm.getRowCount();
				for (int i = rowCount - 1; i >= 0; i--) {
					dtm.removeRow(i);
				}
				Object[] data = {textField.getText(), "14/02", "15kb"}; 
				dtm.addRow(data);

			} else if (event.getActionCommand() == "Download") {
				int column = 0;
				int row = table.getSelectedRow();
				if (row > -1) {
					String value = table.getModel().getValueAt(row, column).toString();
					System.out.println(value);
				}
			} else if (event.getActionCommand() == "Upload") {
				
			}
		}
	}
}
