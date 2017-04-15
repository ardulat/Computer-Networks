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
		System.out.println("Server has been created.");
		try {
			address = socket.getLocalAddress().toString();
			System.out.println(socket.getLocalPort());
            port = "";

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Socket has been created. Waiting for clients...");
            
            while(true) {
            	try{
//            		System.out.println("Check");
            		received = inputStream.readLine();
            		System.out.println(received);
            		System.out.println("Check");
            		message = received.split("\\s", 3); // split by space (\\s) and limit is 3
            		// Authentication
            		if (received.equals("Hi, server!")) {
            			System.out.println("Connection established.");
            			response = "Hello, client!";
            			outputStream.write(response.getBytes());
            			outputStream.flush();
            		}
            		// Uploading JSON to server
            		else if (message[0].equals("Upload:")) {
            			port = message[1];
            			String json = message[2];
            			ArrayList<Data> retrieved = retrieveData(json);
            			for (int i = 0; i < retrieved.size(); i++) {
            			    data.add(i+data.size()-1, retrieved.get(i)); 
            			}
            			System.out.println(data);
            			
            		}
            		else if (message[0].equals("Search:")) {
            			
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
}
