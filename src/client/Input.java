package client;

import java.util.Scanner;

public class Input {
    public Input(Network network) {
        new Thread(() ->
        {
            Scanner scanner = new Scanner(System.in);
            String line;
            while ((line = scanner.nextLine()) != null) {
                String[] words = line.split(" ");
                switch (words[0].toUpperCase()) {
                    case "HELP":
                        System.out.println("Commands:");
                        System.out.println("help - shows this message");
                        System.out.println("login <name> - connect to the server with the desired name");
                        System.out.println("place <n> - place your marker (X or O) in the desired slot");
                        System.out.println("exit - exit the server");
                        System.out.println("who - show a list of players who are available to play");
                        System.out.println("games - show a list of games that are currently ongoing");
                        System.out.println("play <name> - start a game with the desired player");
                        break;
                    case "LOGIN":
                        if (words.length > 1) {
                            network.sendMessage("LOGIN " + words[1]);
                        }
                        break;
                    case "PLACE":
                        if (words.length > 1) {
                            network.sendMessage("PLACE " + words[1]);
                        }
                        break;
                    case "EXIT":
                        System.exit(0);
                        break;
                    case "WHO":
                        network.sendMessage("WHO");
                        break;
                    case "GAMES":
                        network.sendMessage("GAMES");
                        break;
                    case "PLAY":
                        if (words.length > 1) {
                            network.sendMessage("PLAY " + words[1]);
                        }
                        break;
                }
            }
        }).start();
    }
}
