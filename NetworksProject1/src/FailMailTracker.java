import java.io.*;
import java.util.*;
import java.net.*;

public class FailMailTracker extends Thread {
	
	// Instances
	ServerSocket serverSocket;
	Socket socket;
	ArrayList<Data> data;
	String received;
	String[] message;
	String response;
	
	// Initializer
	public FailMailTracker(ServerSocket serverSocket, Socket socket, ArrayList<Data> data) {
		this.serverSocket = serverSocket;
		this.socket = socket;
		this.data = data;
	}
	
	public void run() {
		try {
			String address = socket.getLocalAddress().toString();
            String port = "";

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Socket created. Waiting for clients...");
            
            while(true) {
            	try{
            		received = inputStream.readLine();
            		message = received.split("\\s"); // split by space (\\s) and limit is 2
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
	
	
	
}
