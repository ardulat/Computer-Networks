package FailMail;
import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Model.Data;

import java.net.*;

public class FailMailTracker extends Thread {
	
	// Instances
	ServerSocket serverSocket;
	Socket socket;
	ArrayList<Data> data;
	String received;
	String[] message;
	String response;
	String address;
	String port;
	
	// Initializer
	public FailMailTracker(ServerSocket serverSocket, Socket socket, ArrayList<Data> data) {
		this.serverSocket = serverSocket;
		this.socket = socket;
		this.data = data;
	}
	
	public void run() {
		try {
			address = socket.getLocalAddress().toString();
			System.out.println(socket.getLocalPort());
            port = "";

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Socket has been created. Waiting for clients...");
            
            while(true) {
            	try{
            		received = inputStream.readLine();
            		System.out.println(received);
            		System.out.println("saipal");
            		message = received.split("\\s", 3); // split by space (\\s) and limit is 3
            		// Authentication
            		if (received.equals("Hi, server!")) {
            			System.out.println("Connection established.");
            			response = "Hello, client!\n";
            			outputStream.write(response.getBytes());
            			outputStream.flush();
            		}
            		// Uploading JSON to server
            		else if (message[0].equals("Upload:")) {
            			port = message[1];
            			String json = message[2];
            			ArrayList<Data> retrieved = retrieveData(json);
            			
            			for (int i = 0; i < retrieved.size(); i++) {
            			    data.add(retrieved.get(i)); 
            			}
            		}
            		else if (message[0].equals("Search:")) {
            			
            			if (message.length > 1) { // "Search: filename" typed - OK
            				
            				String filename = message[1];
            				System.out.println("Searching a file " + filename);
            				
            				int fileCount = 0;
            				for (int i = 0; i < data.size(); i++) {
            					if (!port.equals(data.get(i).getPort())
            						&& (filename.contains(data.get(i).getFilename()))
            						|| data.get(i).getFilename().contains(filename)) {
            						fileCount++;
            						response = response + data.get(i).toString() + "\n"; 
            					}
            				}
            				
            				// Saying how many files have been found
            				System.out.println(fileCount + "files found:");
            				System.out.println(response);
            				
            				String numberOfFiles = fileCount + "\n";
            				outputStream.write(numberOfFiles.getBytes());
            				
            				// Sending files
            				if (fileCount != 0) {
            					System.out.println("Sending file attributes to server...");
            					outputStream.write(response.getBytes());
            				}
            			} else { // "Search: " typed - Error 
            				System.out.println("Please provide a filename you are searching for");
            			}
            		}
            		else if (received.equals("Bye, server!")) {
            			response = "Goodbye, client!";
            			outputStream.write(response.getBytes());
            			outputStream.flush();
            		}
            		else {
            			response = "Oops! Bad request :(";
            			outputStream.write(response.getBytes());
            			outputStream.flush();
            		}
            	} catch(Exception e) {
            		System.out.println("Connection lost.");
            		System.out.println(e);
            		
            		// Delete client's tuples
            		for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getPort().equals(port)) {
                            data.remove(i);
                            i--;
                        }
                    }
                    break;
            	}
            }
		} catch (Exception e) {
			System.out.println("Could not create a socket.");
			System.out.println(e);
		}
	}
	
	public ArrayList<Data> retrieveData(String json) {
		
		JSONParser parser = new JSONParser();
		String filename;
		String lastModified;
		String size;
		String format;
		Data row;
		ArrayList<Data> dt = new ArrayList<Data>();
		
		try{
	         Object obj = parser.parse(json);
	         JSONArray array = (JSONArray)obj;
	         JSONObject tuple;
	         
	         for(int i = 0; i < array.size(); i++) {
	        	 tuple = (JSONObject) array.get(i);
	        	 
	        	 filename = (String) tuple.get("fileName");
	        	 lastModified = (String) tuple.get("lastModified");
	        	 size = (String) tuple.get("fileSize");
	        	 format = (String) tuple.get("format");
	        	 
	        	 row = new Data(filename, format, size, lastModified, address, port);
	        	 dt.add(row);
	        	 System.out.println(row.toString());
	         }
		} catch(ParseException pe){
	        System.out.println(pe);
	    }
		
		return dt;
	}
//	
//	public boolean isPresent(Data u){
//        for (int i = 0; i < data.size(); i++){
//            if (data.get(i).getFilename().equals(u.getFilename())){
//                return true;
//            }
//        }
//        return false;
//    }
}
