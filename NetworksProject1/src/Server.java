/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
}