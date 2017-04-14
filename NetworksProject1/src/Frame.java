import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

			} //action for Download button
			else if (event.getActionCommand() == "Download") {
				System.out.println("Download pressed.");
				
				//when we choose a row
				int column = 0;
				int row = table.getSelectedRow();
				if (row > -1) {
					String name = table.getModel().getValueAt(row, column).toString();
					System.out.println("The name of the file is = " + name);
					try {
						sendData(name);
						System.out.println("Data is sent.");
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Error!");
					}
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
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
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
				    if (data.size() == 0) {
				    	dtm.removeRow(0);
				    }
				    data.add(new Object[] {fileName, format.toUpperCase(), lastModified, fileSize});
				    dtm.addRow(data.get(data.size()-1));
				} else {
				    //User did not choose a valid file
					System.out.println("Upload canceled.");
				}
			}
		}
		public void sendData(String filename) throws Exception {
            //create server socket on port 15123
            ServerSocket ss=new ServerSocket(15123); 
            System.out.println ("Waiting for request");	
            receiveData();
            Socket s=ss.accept();  
            System.out.println ("Connected With "+s.getInetAddress().toString());
            DataInputStream din=new DataInputStream(s.getInputStream());  
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
            try{
            	
                System.out.println("SendGet....Ok");

                if(true){  

                    System.out.println("Sending File: "+filename);
                    dout.writeUTF(filename);
                    dout.flush();  

                    File f=new File(filename);
                    FileInputStream fin=new FileInputStream(f);
                    long sz=(int) f.length();

                    byte b[]=new byte [1024];

                    int read;

                    dout.writeUTF(Long.toString(sz)); 
                    dout.flush(); 

                    System.out.println ("Size: "+sz);
                    System.out.println ("Buf size: "+ss.getReceiveBufferSize());

                    while((read = fin.read(b)) != -1){
                        dout.write(b, 0, read); 
                        dout.flush(); 
                    }
                    fin.close();

                    System.out.println("..ok"); 
                    dout.flush(); 
                }  
                dout.writeUTF("stop");  
                System.out.println("Send Complete");
                dout.flush();  
            }
            catch(Exception e) {
                e.printStackTrace();
                System.out.println("An error occured");
            }
            din.close();  
            s.close();  
            ss.close();  
		}
		
		public void receiveData() throws Exception {
			String address = "localhost";
	        //create the socket on port 15123
	        Socket s=new Socket(address,15123);  
	        DataInputStream din=new DataInputStream(s.getInputStream());  
	        DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
	        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  

	        System.out.println("Send Get to start...");
	        String filename="";  
	        try{

	            filename=din.readUTF();     
	            System.out.println("Receving file: "+filename);
	            filename="client_"+filename;
	            System.out.println("Saving as file: "+filename);
	        //
	            long sz=Long.parseLong(din.readUTF());
	            System.out.println ("File Size: "+(sz/(1024*1024))+" MB");

	            byte b[]=new byte [1024];
	            System.out.println("Receving file..");
	            FileOutputStream fos=new FileOutputStream(new File(filename),true);
	            long bytesRead;
	            do {
	                bytesRead = din.read(b, 0, b.length);
	                fos.write(b,0,b.length);
	            } while(!(bytesRead<1024));

	            System.out.println("Completed");
	            fos.close(); 
	            dout.close();  	
	            s.close();  
	        }
	        catch(EOFException e) {
	        	e.printStackTrace();
                System.out.println("An error occured");
	        }
		}
	}
}
