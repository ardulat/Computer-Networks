package FailMail;
import java.util.*;

import Model.Data;

import java.io.*;
import java.net.*;

public class FailMailTrackerMain {
	
    public static void main(String[] args) throws IOException {

        ArrayList<Data> data = new ArrayList<Data>();
        ServerSocket serverSocket = new ServerSocket(15125);
        System.out.println(serverSocket.getLocalPort());

        while (true) {
        	Socket socket = serverSocket.accept();
        	FailMailTracker thread = new FailMailTracker(serverSocket, socket, data);
            thread.start();
        }
    }
}
