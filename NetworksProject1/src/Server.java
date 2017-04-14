import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	String filename;
	
	Server(String filename) {
		filename = this.filename;
	}
	
	public static void main(String args[]) throws Exception {
		
		Server server = new Server(filename);
		
		//create server socket on port 15123
        ServerSocket ss=new ServerSocket(15123); 
        System.out.println ("Waiting for request");	
        
        Socket s=ss.accept();  
        System.out.println ("Connected With "+s.getInetAddress().toString());
        DataInputStream din=new DataInputStream(s.getInputStream());  
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
        try{
        	
            System.out.println("SendGet....Ok");

            if(true){  

                System.out.println("Sending File: "+filename);
                dout.writeUTF(filename);
                dout.flush();  

                File f=new File(filename);
                FileInputStream fin=new FileInputStream(f);
                long sz=(int) f.length();

                byte b[]=new byte [1024];

                int read;

                dout.writeUTF(Long.toString(sz)); 
                dout.flush(); 

                System.out.println ("Size: "+sz);
                System.out.println ("Buf size: "+ss.getReceiveBufferSize());

                while((read = fin.read(b)) != -1){
                    dout.write(b, 0, read); 
                    dout.flush(); 
                }
                fin.close();

                System.out.println("..ok"); 
                dout.flush(); 
            }  
            dout.writeUTF("stop");  
            System.out.println("Send Complete");
            dout.flush();  
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("An error occured");
        }
        din.close();  
        s.close();  
        ss.close();  
	}
}
