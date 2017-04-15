package Peer;

import Peer.Peer;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PeerMain2 {
	
	public static void main(String args[]) throws IOException {
		
		// Instances
		String received;
		String send;
		String[] message;
		JSONArray array = new JSONArray();
		
		// Connecting to port
		int port = 15129;
		ServerSocket serverSocket = new ServerSocket(port);
		Peer thread = new Peer(serverSocket);
		thread.start();
		
		// Generating sockets
		Socket socket = new Socket("127.0.0.1", 15125);
//		System.out.println(socket.getLocalPort());
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		// Greeting the server
		send = "Hi, server!\n";		
		outputStream.write(send.getBytes());
		outputStream.flush();
		System.out.println(send);
		received = inputStream.readLine();
		System.out.println("Server says: " + received);
		
		// Uploading files
		File directory = new File(System.getProperty("user.dir") + "/sharedFiles/client2/");
		File[] files = directory.listFiles();
		send = "Upload: " + port + " ";
		for (File file : files) {
			if (file.isFile()) {
				JSONObject json = new JSONObject();
				
				// Getting file attributes
				String fileName = file.getName();
			    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			    String lastModified = sdf.format(file.lastModified());
			    float size = file.length() / (1024*1024);
			    String fileSize = "";
			    if (size != 0) {
			    	fileSize = size + "MB";
			    } else {
			    	size = file.length() / 1024;
			    	fileSize = size + "KB";
			    }
			    String format = getFormat(file);
			    
			    // Adding attributes to json array
				json.put("fileName", fileName);
				json.put("lastModified", lastModified);
				json.put("fileSize", fileSize);
				json.put("format", format);
				array.add(json);
			}
		}
		send = send + array;
		send = send + "\n";
		outputStream.write(send.getBytes());
		outputStream.flush();
		
		// Request a file
		while(true) {
			
			System.out.println("Send a message to the server:");
			send = inputStream.readLine();
			message = send.split("\\s");
			if (message.equals("Search:")) {
				send = send + "\n";
				outputStream.write(send.getBytes());
				boolean used = false;
				
				// number of files returned (from server)
				received = inputStream.readLine();
				int filesCount = Integer.parseInt(received);
				System.out.println(filesCount + " files found:");
				
				// what to do with found files
				if (filesCount != 0) {
					for (int i = 0; i < filesCount; i++) {
						received = inputStream.readLine();
						System.out.println(received);
					}
					
					// Assume that server has found 1 file (not several)
					String attr[] = received.split(", ");
					String address = "127.0.0.1";
					String filename = attr[0].substring(1);
					int filePort = Integer.parseInt(attr[5].substring(0, attr[5].length()-1));
					
					System.out.println("File "+filename+" found at address "+address+" on port "+port);
					
					// Start downloading file from a found peer
					String fileSend;
					String fileReceieved;
					Socket fileSocket = new Socket(address, port);
					DataOutputStream fileOutputStream = new DataOutputStream(fileSocket.getOutputStream());
					BufferedReader fileInputStream = new BufferedReader(new InputStreamReader(fileSocket.getInputStream()));
					
					fileSend = "Download: " + filename + "\n";
					fileOutputStream.write(fileSend.getBytes());
					
					DataInputStream din = new DataInputStream(fileSocket.getInputStream());
					String filenameReceived = din.readUTF();
					System.out.println("Receiving " + filenameReceived);
					long fileSize = Long.parseLong(din.readUTF());
					System.out.println("File size is " + fileSize/1024 + "KB");
					
					byte b[] = new byte[1024];
					
					filenameReceived = "received_" + filenameReceived;
					System.out.println("Receiving a file " + filenameReceived);
					FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir")+
																"/receivedFiles/" + filenameReceived), true);
					long bytesRead;
					do {
	                    bytesRead = din.read(b, 0, b.length);
	                    fos.write(b, 0, b.length);
	                } while (!(bytesRead < 1024));
					
					System.out.println("Completed.");
					fos.close();
					din.close();
					fileOutputStream.close();
				}
				else {
					System.out.println("File not found.");
				}
			}
			else if (send.equals("Bye, server!")) { // Say goodbye
				thread.disconnect();
				serverSocket.close();
				socket.close();
				break;
			}
			else {
				// Error message
				System.out.println("Oops! I can't understand you.");
			}
		}
	}
	
	public static String getFormat(File file) {
		String format = "";
		String fileName = file.getName();
	    char[] nameChar = fileName.toCharArray();
	    Stack<Character> myStack = new Stack();
	    for (int j = nameChar.length - 1; j >= 0; j--) {
	    	if (nameChar[j] == '.') {break;}
	    	myStack.push(nameChar[j]);
	    	
	    }
	    while(myStack.size() > 0) {
	    	format += myStack.pop();
	    }
	    
	    return format;
	}
}
