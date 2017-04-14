/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networks.project;

/**
 *
 * @author anuar
 */
import java.net.*; 
import java.io.*; 
import java.util.Scanner;
public class Server { 
    public static void main (String [] args ) throws IOException { 
        ServerSocket serverSocket = new ServerSocket(15123); 
        System.out.println("Waiting for clients...");
        Socket socket = serverSocket.accept();
        System.out.println("Accepted connection : " + socket);
        String filename;
        System.out.println("Enter File Name: ");
        Scanner sc=new Scanner(System.in);
        filename=sc.nextLine();
        sc.close();
        File transferFile = new File (filename); 
        byte [] bytearray = new byte [(int)transferFile.length()]; 
        FileInputStream fin = new FileInputStream(transferFile); 
        BufferedInputStream bin = new BufferedInputStream(fin); 
        bin.read(bytearray,0,bytearray.length); 
        OutputStream os = socket.getOutputStream(); 
        System.out.println("Sending Files..."); 
        os.write(bytearray,0,bytearray.length); 
        os.flush(); 
        socket.close(); 
        System.out.println("File transfer complete"); 
    } 
} 
