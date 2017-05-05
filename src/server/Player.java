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
            // Socket is not guaranteed to write all bytes - loop until all bytes are written
            while (buf.hasRemaining() && socket.isOpen()) {
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
                        // Player with name already exists
                        sendMessage("400 ERROR\r\nLOGIN\r\nName taken!");
                    }
                } else {
                    // Player must be already logged in
                    sendMessage("400 ERROR\r\nLOGIN\r\nAlready logged in!");
                }
                break;
            case "PLACE":
                if (words.length > 1 && state == PlayerState.INGAME) {
                    int pos = Integer.parseInt(words[1]) - 1;
                    if (game.getCurrentPlayer() == this) {
                        if (game.place(pos)) {
                            // Check if someone won
                            game.checkVictory();
                        } else {
                            // Either pos < 0, pos > 8, or the spot is already taken
                            sendMessage("400 ERROR\r\nPLACE\r\nInvalid spot!");
                        }
                    } else {
                        // It is not this player's turn
                        sendMessage("400 ERROR\r\nPLACE\r\nNot your turn!");
                    }
                } else {
                    // Not in a game
                    sendMessage("400 ERROR\r\nPLACE\r\nNot in a game!");
                }
                break;
            case "WHO":
                String response = "200 OK\r\nWHO";
                // Create list of available players
                for (Player player : playersByName.values()) {
                    if (player.state == PlayerState.AVAILABLE) {
                        response += "\r\n" + player.name;
                    }
                }
                sendMessage(response);
                break;
            case "GAMES":
                response = "200 OK\r\nGAMES";
                // Create list of ongoing games
                for (Game game : Game.gamesById.values()) {
                    response += "\r\nID: " + game.getId() + " Players: " + game.getPlayerX().name + " " + game.getPlayerO().name;
                }
                sendMessage(response);
                break;
            case "PLAY":
                if (words.length > 1 && state == PlayerState.AVAILABLE) {
                    Player player = playersByName.get(words[1]);
                    if (player != null && player.state == PlayerState.AVAILABLE && player != this) {
                        // Start game with the selected player
                        Game game = new Game(this, player);
                        game.start();
                    } else {
                        // Either the selected player is not available, or this player tried to play with himself
                        sendMessage("400 ERROR\r\nPLAY\r\nPlayer not available!");
                    }
                } else {
                    // This player is not available
                    if (state == PlayerState.CONNECTING)
                        sendMessage("400 ERROR\r\nPLAY\r\nYou must be logged in to play/observe!");
                    else if (state == PlayerState.INGAME)
                        sendMessage("400 ERROR\r\nPLAY\r\nYou can only play/observe one game at a time!");
                }
                break;
            case "OBSERVE":
                if (words.length > 1 && state == PlayerState.AVAILABLE) {
                    Game game = Game.gamesById.get(Integer.parseInt(words[1]));
                    if (game != null) {
                        game.addObserver(this);
                        sendMessage("200 OK\r\nOBSERVE\r\n" + game.getId());
                    } else {
                        // Game not found
                        sendMessage("400 ERROR\r\nOBSERVE\r\nInvalid game ID!");
                    }
                } else {
                    // This player is not available
                    if (state == PlayerState.CONNECTING)
                        sendMessage("400 ERROR\r\nOBSERVE\r\nYou must be logged in to play/observe!");
                    else if (state == PlayerState.INGAME)
                        sendMessage("400 ERROR\r\nOBSERVE\r\nYou can only play/observe one game at a time!");
                }
                break;
            case "UNOBSERVE":
                if (words.length > 1) {
                    Game game = Game.gamesById.get(Integer.parseInt(words[1]));
                    if (game != null && game.hasObserver(this)) {
                        game.removeObserver(this);
                        sendMessage("200 OK\r\nUNOBSERVE\r\n" + game.getId());
                    } else {
                        // Game does not exist or player is not observing
                        sendMessage("400 ERROR\r\nUNOBSERVE\r\nYou are not observing that game!");
                    }
                }
                break;
            case "SAY":
                if (words.length > 1 && game != null) {
                    game.sendAll("200 OK\r\nSAY\r\n" + name + ": " + message.substring(4));
                } else {
                    // Player is not in a game
                    sendMessage("400 ERROR\r\nSAY\r\nYou are not in a game!");
                }
                break;
        }
    }
}