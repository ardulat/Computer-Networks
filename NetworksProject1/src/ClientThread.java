import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

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
                 
                String filename;
                try{

                    filename=input.readUTF();    
                    System.out.println("Receving file: "+filename);
                    filename="client"+filename;
                    System.out.println("Saving as file: "+filename);
                //
                    long sz=Long.parseLong(din.readUTF());
                    System.out.println ("File Size: "+(sz/(1024*1024))+" MB");

                    byte b[]=new byte [1024];
                    System.out.println("Receving file..");
                    FileOutputStream fos=new FileOutputStream(new File(filename),true);
                    long bytesRead;
                    do {
                        bytesRead = din.read(b, 0, b.length);
                        fos.write(b,0,b.length);
                    } while(!(bytesRead<1024));

                    System.out.println("Completed");
                    fos.close(); 
                    dout.close();  	
                    s.close();  
                }
                catch(EOFException e) {
                    //do nothing
                }
            } catch(IOException exception) {
                System.out.println("Error: " + exception);
            }
        }
    }