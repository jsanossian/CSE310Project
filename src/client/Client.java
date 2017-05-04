package client;

import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Must specify host and port number.");
            return;
        }
        Network network = new Network(new InetSocketAddress(args[0], Integer.parseInt(args[1])));
        new InputThread(network).start();
        while (true) {
            network.pollInput();
        }
    }
}
