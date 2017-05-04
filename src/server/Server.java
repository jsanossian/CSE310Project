package server;

import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) {
        Network network = new Network(new InetSocketAddress(12345));
        while (true) {
            network.pollInput();
        }
    }
}
