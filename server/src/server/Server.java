/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author bakhytnazirov
 */
// echo server
import java.net.*;
import java.util.*;
import java.io.*;
 
public class Server
{
    public static void main(String[] args)
    {
        new Server();
    }
     
    public Server()
    {
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
     
    //Here we create the ClientThread inner class and have it implement Runnable
    //This means that it can be used as a thread
    class ClientThread implements Runnable
    {
        Socket threadSocket;
         
        //This constructor will be passed the socket
        public ClientThread(Socket socket)
        {
            //Here we set the socket to a local variable so we can use it later
            threadSocket = socket;
        }
         
        public void run()
        {
            //All this should look familiar
            try {
                //Create the streams
                PrintWriter output = new PrintWriter(threadSocket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
                System.out.println("New client arrived");
                 
                //Tell the client that he/she has connected
                output.println("Hi, client!");
                output.println("You have connected at: " + new Date());
                 
                while (true) {
                    //This will wait until a line of text has been sent
                    String chatInput = input.readLine();
                    
                    System.out.println(chatInput);
                    
                    if (chatInput.equals("Hello")) {
                        output.println("Hi");
                    }
                }
            } catch(IOException exception) {
                System.out.println("Error: " + exception);
            }
        }
    }
}