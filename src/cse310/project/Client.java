package cse310.project;

import Constants.MessageType;
import Messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    private static final String ADDRESS = "localhost";
    private static final int PORT = 25564;
    private static Socket s;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;        
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while(running) {
            String userInput = sc.nextLine().toUpperCase();
            MessageType mt = getEnum(userInput);
            switch(mt) {
                case HELP: {
                    System.out.println("Login: Takes one argument, your name. The server records this player to be available to play.");
                    System.out.println("Place: This command issues a move. It takes one argument n, which is between 1 and 9 inclusive.");
                    System.out.println("Exit: The player exists the server. It takes no argument. The client program is closed.");
                    break;
                }
                case LOGIN: {
                    
                    break;
                }
                case PLACE: {
                    
                    break;
                }
                case EXIT: {
                    
                    break;
                }
                case INVALID_COMMAND: {
                    System.out.println("Invalid Command");
                    break;
                }
            }
        }
    }
    
    public static MessageType getEnum(String userInput) {
        MessageType mt;
        try {
            mt = MessageType.valueOf(userInput);
        }catch(IllegalArgumentException ex) {
            mt = MessageType.INVALID_COMMAND;    
        }
        return mt;
    }
    
    public static boolean isConnectedToServer() {
        return !(s == null || s.isClosed());
    }
}
