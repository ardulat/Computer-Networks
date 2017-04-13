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
// A simple Client Server Protocol .. Client for Echo Server

import java.io.*;
import java.net.*;
import java.util.Date;
//We need a Scanner to receive input from the user
import java.util.Scanner;
 
public class Client
{
    public static void main(String[] args)
    {
        new Client();
    }
     
    public Client()
    {
    //We set up the scanner to receive user input
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 5000);
            
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             
            //This will wait for the server to send the string to the client saying a connection
            //has been made.
            String inputString = input.readLine();
            System.out.println(inputString);
            //Again, here is the code that will run the client, this will continue looking for 
            //input from the user then it will send that info to the server.
            while(true) {
                //Here we look for input from the user
                String userInput = scanner.nextLine();
                //Now we write it to the server
                output.println(userInput);
            }
        } catch (IOException exception) {
            System.out.println("Error: " + exception);
        }
    }
}