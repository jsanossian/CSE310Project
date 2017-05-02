package cse310.project;

import Constants.MessageType;
import Messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    
    private static volatile boolean running = true;
    private static final int PORT = 25564;
    private static ServerSocket ss;
    
    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            while(running && !ss.isClosed()) {
                Socket s = ss.accept();
                ServerCommunication t = new ServerCommunication(s);
                Thread th = new Thread(t);
                th.start();
            }
        } catch(IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
