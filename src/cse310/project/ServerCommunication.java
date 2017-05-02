package cse310.project;

import Constants.MessageType;
import Messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCommunication implements Runnable {
    private Socket s;
    private volatile boolean running;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
        
    public ServerCommunication(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(running) {
            try {
                Message m = (Message) ois.readObject();
                if(m.getType() == MessageType.HELP) {
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
        
    public void setRunning(boolean running) {
        this.running = running;
    } 
}
