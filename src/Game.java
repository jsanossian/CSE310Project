
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joe
 */
public class Game {
    Player playerX, playerO;
    
    //
    char[] board;

    public Game() {
        
    }
    public boolean start(){
        if(playerX == null || playerO == null){
            return false;
        }
        else{
            board = new char[9];
            for(int i =0; i< board.length; i++){
                board[i] = ' ';
            }
            return true;
        }
    }
    
    public int placeO(int place){
        if(board[place-1] == ' '){
            board[place-1] = 'O';
            return place;
        }
        else return -1;
        
    }
    
    
    public int placex(int place){
        if(board[place-1] == ' '){
            board[place-1] = 'X';
            return place;
        }
        else return -1;
        
    }
     
    public boolean evaluateX(){
        //rows
        if(board[0] == 'X' && board[1]== 'X'  && board[2] == 'X' ){
            return true;
        }
        if(board[3] ==  'X'  && board[4] == 'X'  && board[5] == 'X' ){
            return true;
        }
        if(board[6] ==  'X' && board[7]== 'X'  && board[8] == 'X' ){
            return true;
        }
        //columns
        if(board[0] == 'X'  && board[3]==  'X'  && board[6] == 'X' ){
            return true;
        }
        if(board[1] ==  'X' && board[4]==  'X'  && board[7] == 'X' ){
            return true;
        }
        if(board[2] ==  'X' && board[5]== 'X'  && board[8] ==  'X' ){
            return true;
        }
        //diagonals
        if(board[0] == 'X'  && board[4]== 'X'  && board[8] == 'X' ){
            return true;
        }
        if(board[2] ==  'X' && board[4]== 'X'  && board[6] == 'X' ){
            return true;
        }
        return false;
    }
    
         
      public boolean evaluateO(){
        //rows
        if(board[0] == 'O' && board[1]== 'O'  && board[2] == 'O' ){
            return true;
        }
        if(board[3] ==  'O'  && board[4] == 'O'  && board[5] == 'O' ){
            return true;
        }
        if(board[6] ==  'O' && board[7]== 'O'  && board[8] == 'O' ){
            return true;
        }
        //columns
        if(board[0] == 'O'  && board[3]==  'O'  && board[6] == 'O' ){
            return true;
        }
        if(board[1] ==  'O' && board[4]==  'O'  && board[7] == 'O' ){
            return true;
        }
        if(board[2] ==  'O' && board[5]== 'O'  && board[8] ==  'O' ){
            return true;
        }
        //diagonals
        if(board[0] == 'O'  && board[4]== 'O'  && board[8] == 'O' ){
            return true;
        }
        if(board[2] ==  'O' && board[4]== 'O'  && board[6] == 'O' ){
            return true;
        }
        return false;
    }
    
    public Game(Player playerX, Player PlayerO) {
        this.playerX = playerX;
        this.playerO = PlayerO;
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

    public void setPlayerO(Player PlayerO) {
        this.playerO = PlayerO;
    }


    
}
