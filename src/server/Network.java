package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Set;

public class Network {
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private HashMap<SocketChannel, Player> playersBySocket = new HashMap<>();

    public Network(InetSocketAddress address) {
        try {
            // Create non-blocking server socket
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            // We are interested when a client tries to connect
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            serverSocket.bind(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pollInput() {
        try {
            // Check if any of the sockets have actions pending (blocking)
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {
                if (key.isAcceptable()) {
                    // Server socket has a client pending, so accept it
                    accept(key);
                } else if (key.isReadable()) {
                    // Socket has a read pending, so read it
                    read(key);
                }
            }
            keys.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key) {
        try {
            // Create a non-blocking socket
            SocketChannel socket = serverSocket.accept();
            socket.configureBlocking(false);
            // We are interested when the socket has a read pending
            socket.register(selector, SelectionKey.OP_READ);
            playersBySocket.put(socket, new Player(socket));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        SocketChannel socket = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            int bytesRead = socket.read(buf);
            if (bytesRead == -1) {
                // Connection closed gracefully
                close(key);
                return;
            }
        } catch (IOException e) {
            // Connection closed forcefully
            close(key);
            return;
        }
        // Convert the bytes read to a string
        buf.flip();
        byte[] bytes = new byte[buf.remaining()];
        buf.get(bytes);
        String message = new String(bytes);
        // Get the player pertaining to this socket and dispatch the message
        Player player = playersBySocket.get(socket);
        if (player != null) {
            player.receiveMessage(message);
        }
    }

    private void close(SelectionKey key) {
        SocketChannel socket = (SocketChannel) key.channel();
        try {
            key.cancel();
            socket.close();
            Player player = playersBySocket.get(socket);
            if (player != null) {
                playersBySocket.remove(socket);
                player.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
