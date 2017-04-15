package Peer;

import Peer.Peer;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PeerMain1 {
	
	public static void main(String args[]) throws IOException {
		
		// Instances
		String received;
		String send;
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		// Connecting to port
		int port = 15127;
		ServerSocket serverSocket = new ServerSocket(port);
		Peer thread = new Peer(serverSocket);
		thread.start();
		
		// Generating sockets
		Socket socket = new Socket("127.0.0.1", 15125);
		System.out.println(socket.getLocalPort());
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		// Greeting the server
		send = "Hi, server!";		
		outputStream.write(send.getBytes());
		outputStream.flush();
		System.out.println(send);
		received = inputStream.readLine();
		System.out.println("Server says: " + received);
		
		// Uploading files
		File directory = new File(System.getProperty("user.dir") + "/sharedFiles/");
		File[] files = directory.listFiles();
		send = "Upload: " + port;
		for (File file : files) {
			if (file.isFile()) {
				
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
				
				send = send + array;
			}
		}
		outputStream.write(send.getBytes());
		outputStream.flush();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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
