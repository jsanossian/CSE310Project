package server;

import java.util.HashMap;

/**
 * @author Joe
 */
public class Game {
    private static int curId = 1;
    public static HashMap<Integer, Game> gamesById = new HashMap<>();
    private Player playerX, playerO;
    private Player curPlayer;
    private Player winner;
    private int id;
    private char[] board;

    public Game(Player playerX, Player PlayerO) {
        this.playerX = playerX;
        this.playerO = PlayerO;
    }

    public boolean start() {
        curPlayer = playerX;
        winner = curPlayer;
        board = new char[9];
        for (int i = 0; i < board.length; i++) {
            board[i] = ' ';
        }
        // Assign a unique id to this game
        id = curId++;
        gamesById.put(id, this);
        playerX.game = this;
        playerO.game = this;
        playerX.state = PlayerState.INGAME;
        playerO.state = PlayerState.INGAME;
        playerX.sendMessage("200 OK\r\nPLAY\r\n" + playerO.name);
        playerO.sendMessage("200 OK\r\nPLAY\r\n" + playerX.name);
        return true;
    }

    public void end() {
        playerX.game = null;
        playerO.game = null;
        playerX.state = PlayerState.AVAILABLE;
        playerO.state = PlayerState.AVAILABLE;
        winner.sendMessage("200 OK\r\nENDGAME\r\nYou won!");
        if (playerX == winner) {
            playerO.sendMessage("200 OK\r\nENDGAME\r\nYou lost!");
        } else {
            playerX.sendMessage("200 OK\r\nENDGAME\r\nYou lost!");
        }
        gamesById.remove(id);
    }

    public boolean place(int pos) {
        char marker = 'X';
        if (curPlayer == playerO) {
            marker = 'O';
        }
        if (pos >= 0 && pos < board.length && board[pos] == ' ') {
            board[pos] = marker;
            if (curPlayer == playerX) {
                curPlayer = playerO;
            } else {
                curPlayer = playerX;
            }
            return true;
        }
        return false;
    }

    public void checkVictory() {
        if (evaluateX()) {
            winner = playerO;
            end();
        }
        if (evaluateO()) {
            winner = playerX;
            end();
        }
    }

    public boolean evaluateX() {
        //rows
        if (board[0] == 'X' && board[1] == 'X' && board[2] == 'X') {
            return true;
        }
        if (board[3] == 'X' && board[4] == 'X' && board[5] == 'X') {
            return true;
        }
        if (board[6] == 'X' && board[7] == 'X' && board[8] == 'X') {
            return true;
        }
        //columns
        if (board[0] == 'X' && board[3] == 'X' && board[6] == 'X') {
            return true;
        }
        if (board[1] == 'X' && board[4] == 'X' && board[7] == 'X') {
            return true;
        }
        if (board[2] == 'X' && board[5] == 'X' && board[8] == 'X') {
            return true;
        }
        //diagonals
        if (board[0] == 'X' && board[4] == 'X' && board[8] == 'X') {
            return true;
        }
        if (board[2] == 'X' && board[4] == 'X' && board[6] == 'X') {
            return true;
        }
        return false;
    }


    public boolean evaluateO() {
        //rows
        if (board[0] == 'O' && board[1] == 'O' && board[2] == 'O') {
            return true;
        }
        if (board[3] == 'O' && board[4] == 'O' && board[5] == 'O') {
            return true;
        }
        if (board[6] == 'O' && board[7] == 'O' && board[8] == 'O') {
            return true;
        }
        //columns
        if (board[0] == 'O' && board[3] == 'O' && board[6] == 'O') {
            return true;
        }
        if (board[1] == 'O' && board[4] == 'O' && board[7] == 'O') {
            return true;
        }
        if (board[2] == 'O' && board[5] == 'O' && board[8] == 'O') {
            return true;
        }
        //diagonals
        if (board[0] == 'O' && board[4] == 'O' && board[8] == 'O') {
            return true;
        }
        if (board[2] == 'O' && board[4] == 'O' && board[6] == 'O') {
            return true;
        }
        return false;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public void setPlayerO(Player playerO) {
        this.playerO = playerO;
    }

    public Player getCurrentPlayer() {
        return curPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[] getBoard() {
        return board;
    }

    public void setBoard(char[] board) {
        this.board = board;
    }
}
