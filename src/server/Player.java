package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class Player {
    private static HashMap<String, Player> playersByName = new HashMap<>();
    private SocketChannel socket;
    public String name;
    public Game game;
    public PlayerState state = PlayerState.CONNECTING;

    public Player(SocketChannel socket) {
        this.socket = socket;
    }

    public void remove() {
        if (game != null) {
            if (game.getPlayerX() == this) {
                game.setWinner(game.getPlayerO());
            } else {
                game.setWinner(game.getPlayerX());
            }
            game.end();
        }
        playersByName.remove(name);
    }

    public void sendMessage(String message) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
            while (buf.hasRemaining()) {
                socket.write(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(String message) {
        System.out.println(message);
        String[] words = message.split(" ");
        switch (words[0]) {
            case "LOGIN":
                if (words.length > 1 && state == PlayerState.CONNECTING) {
                    if (!playersByName.containsKey(words[1])) {
                        name = words[1];
                        state = PlayerState.AVAILABLE;
                        playersByName.put(name, this);
                        sendMessage("200 OK\r\nLOGIN\r\n" + name);
                    } else {
                        sendMessage("400 ERROR\r\nLOGIN\r\nName taken!");
                    }
                } else {
                    sendMessage("400 ERROR\r\nLOGIN\r\nAlready logged in!");
                }
                break;
            case "PLACE":
                if (words.length > 1 && state == PlayerState.INGAME) {
                    int pos = Integer.parseInt(words[1]) - 1;
                    if (game.getCurrentPlayer() == this) {
                        if (game.place(pos)) {
                            String response = "200 OK\r\nPLACE\r\n" + new String(game.getBoard());
                            game.getPlayerX().sendMessage(response);
                            game.getPlayerO().sendMessage(response);
                            game.checkVictory();
                        } else {
                            sendMessage("400 ERROR\r\nPLACE\r\nInvalid spot!");
                        }
                    } else {
                        sendMessage("400 ERROR\r\nPLACE\r\nNot your turn!");
                    }
                }
                break;
            case "WHO":
                String response = "200 OK\r\nWHO";
                for (Player player : playersByName.values()) {
                    if (player.state == PlayerState.AVAILABLE) {
                        response += "\r\n" + player.name;
                    }
                }
                sendMessage(response);
                break;
            case "GAMES":
                response = "200 OK\r\nGAMES";
                for (Game game : Game.gamesById.values()) {
                    response += "\r\nID: " + game.getId() + " Players: " + game.getPlayerX().name + " " + game.getPlayerO().name;
                }
                sendMessage(response);
                break;
            case "PLAY":
                if (words.length > 1 && state == PlayerState.AVAILABLE) {
                    Player player = playersByName.get(words[1]);
                    if (player != null && player.state == PlayerState.AVAILABLE && player != this) {
                        Game game = new Game(this, player);
                        game.start();
                    } else {
                        sendMessage("400 ERROR\r\nPLAY\r\nPlayer not available!");
                    }
                } else {
                    if (state == PlayerState.CONNECTING)
                        sendMessage("400 ERROR\r\nPLAY\r\nYou must be logged in to play!");
                    else if (state == PlayerState.INGAME)
                        sendMessage("400 ERROR\r\nPLAY\r\nYou can only play one game at a time!");
                }
                break;
        }
    }
}