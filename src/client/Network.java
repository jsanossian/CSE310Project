package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Network {
    private SocketChannel socket;

    public Network(InetSocketAddress address) {
        try {
            socket = SocketChannel.open();
            socket.connect(new InetSocketAddress("localhost", 12345));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pollInput() {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            socket.read(buf);
            buf.flip();
            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);
            String message = new String(bytes);
            receiveMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void sendMessage(String message) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
            synchronized (this) {
                while (buf.hasRemaining()) {
                    socket.write(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(String message) {
        String[] lines = message.split("\r\n");
        switch (lines[0]) {
            case "200 OK":
                switch (lines[1]) {
                    case "LOGIN":
                        System.out.println("Successfully logged in with name " + lines[2]);
                        break;
                    case "PLACE":
                        char[] board = lines[2].toCharArray();
                        for (int line = 0; line < 3; line++) {
                            int i = line * 3;
                            System.out.println(" " + board[i] + " | " + board[i + 1] + " | " + board[i + 2] + " ");
                            if (line != 2) System.out.println("-----------");
                        }
                        break;
                    case "WHO":
                        System.out.println("Available players:");
                        for (int i = 2; i < lines.length; i++) {
                            System.out.println(lines[i]);
                        }
                        break;
                    case "GAMES":
                        System.out.println("Ongoing games:");
                        for (int i = 2; i < lines.length; i++) {
                            System.out.println(lines[i]);
                        }
                        break;
                    case "PLAY":
                        System.out.println("Started a game with " + lines[2]);
                        break;
                    case "ENDGAME":
                        System.out.println("Game over! " + lines[2]);
                        break;
                }
                break;
            case "400 ERROR":
                if (lines.length > 2)
                    System.out.println(lines[2]);
                break;
        }
    }
}