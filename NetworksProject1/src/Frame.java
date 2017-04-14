import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
		String header[] = new String[] { "Name", "Format", "Last Modified", "Size" };

		// add header in table model
		dtm.setColumnIdentifiers(header);
		// set model into the table object
		table.setModel(dtm);

		// add row dynamically into the table
		dtm.addRow(new Object[] {"", "", "", ""});
		
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
				
				String value;
				Object[] row;
				for (int i = 0; i < data.size(); i++) {
					
					row = data.get(i);
					value = (String) row[0];
					
					if(value.contains(textField.getText())) {
						dtm.addRow(row);
					}
					
				}
				
				//adding row
				//Object[] data = {textField.getText(), "14/02", "15kb"}; 
				//dtm.addRow(data);

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
				    //getting format of the file
				   
				    String format = "";
				    char[] nameChar = fileName.toCharArray();
				    Stack<Character> myStack = new Stack();
				    for (int j = nameChar.length - 1; j >= 0; j--) {
				    	if (nameChar[j] == '.') {break;}
				    	myStack.push(nameChar[j]);
				    	
				    }
				    while(myStack.size() > 0) {
				    	format += myStack.pop();
				    }
				    data.add(new Object[] {fileName, format.toUpperCase(), lastModified, fileSize});
				    dtm.addRow(data.get(data.size()-1));
				    
				    startServer();
				    
				} else {
				    //User did not choose a valid file
					System.out.println("Upload canceled.");
				}
			}
		}
		public void startServer() {
			//We need a try-catch because lots of errors can be thrown
	        try {
	            ServerSocket sSocket = new ServerSocket(5000);
	            System.out.println("Server started at: " + new Date());
	             
	            
	            //Loop that runs server functions
	            while(true) {
	                //Wait for a client to connect
	                Socket socket = sSocket.accept();
	             
	             
	                 
	                //Create a new custom thread to handle the connection
	                ClientThread cT = new ClientThread(socket);
	                 
	                //Start the thread!
	                new Thread(cT).start();
	                 
	            }
	        } catch(IOException exception) {
	            System.out.println("Error: " + exception);
	        }
		}
	}
}
