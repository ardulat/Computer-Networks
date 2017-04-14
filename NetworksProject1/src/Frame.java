import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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
	private JTable table;
	DefaultTableModel dtm;
	
	ArrayList<Object[]> data = new ArrayList<Object[]>();

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
		dtm.addRow(new Object[] {"", "", ""});
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		downloadButton = new JButton("Download");
		add(downloadButton);

		ButtonHandler handler = new ButtonHandler();
		searchButton.addActionListener(handler);
		downloadButton.addActionListener(handler);
		uploadButton.addActionListener(handler);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//action for Search button
			if (event.getActionCommand() == "Search") {
				//clear table, don't need anything to mess there
				int rowCount = dtm.getRowCount();
				for (int i = rowCount - 1; i >= 0; i--) {
					dtm.removeRow(i);
				}
				
				//adding row
				Object[] data = {textField.getText(), "14/02", "15kb"}; 
				dtm.addRow(data);

			} //action for Download button
			else if (event.getActionCommand() == "Download") {
				System.out.println("Download pressed.");
				
				//when we choose a row
				int column = 0;
				int row = table.getSelectedRow();
				if (row > -1) {
					String value = table.getModel().getValueAt(row, column).toString();
					System.out.println(value);
				}
			} //action for Upload button 
			else if (event.getActionCommand() == "Upload") {
				System.out.println("Upload pressed.");
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Frame.this); //Where frame is the parent component

				File file = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    file = fc.getSelectedFile();
				    //Now you have your file to do whatever you want to do
				    String fileName = file.getName();
				    System.out.println("Filename: " + fileName);
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				    String lastModified = sdf.format(file.lastModified());
				    System.out.println("Date modified: " + lastModified);
				    float size = file.length() / (1024*1024);
				    String fileSize = "";
				    if (size != 0) {
				    	fileSize = size + "MB";
				    } else {
				    	size = file.length() / 1024;
				    	fileSize = size + "KB";
				    }
				    data.add(new Object[] {fileName, lastModified, fileSize});
				    dtm.addRow(data.get(data.size()-1));
				} else {
				    //User did not choose a valid file
					System.out.println("Upload canceled.");
				}
			}
		}
	}
}
