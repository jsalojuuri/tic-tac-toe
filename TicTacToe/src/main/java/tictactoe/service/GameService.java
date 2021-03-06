package tictactoe.service;

import java.util.ArrayList;
import java.util.List;
import tictactoe.dao.*;

/** 
 * GameService class manipulates player records and game state
 * @author salojuur
 */

public class GameService {
    
    private Dao playerDao;
    private GameState gameState;
    
    public GameService(int boardWidth, Dao playerDao) {
        initGameBoard(boardWidth);
        this.playerDao = playerDao;
    }
      
    /**
    * Creates new player. Checks first if player with same name exists and if not, creates new player record
    * @param    playerName   player name
    * @return   true, if new player is created. Else false.
    */   
    public boolean createPlayer(String playerName) {
        
        if (playerExists(playerName)) {
            return false;
        }

        Player player = new Player(playerName);
        try {
            playerDao.create(player);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        
        return true;
    }
    
    /**
    * Deletes player. Checks first if player with same name exists and if not, notifies of an error, else deletes player record
    * @param    playerName   player name
    * @return   true, if player record is deleted, else false
    */  
    public boolean deletePlayer(String playerName) {
        
        if (!playerExists(playerName)) {
            return false;
        }
        
        try {
            List<Player> players = this.getPlayers();
            for (Player player: players) {
                if (player.getName().equals(playerName)) {
                    playerDao.delete(player);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
        return true;
        
    }
    
    /**
    * Checks if player already exists
    * @param   playerName   player name
    * @return   true, if player already exists, else false
    */  
    private boolean playerExists(String playerName) {
        
        List<Player> players = new ArrayList<>();
        
        try {      
            players = playerDao.list();
            for (Player p: players) {
                if (p.getName().equals(playerName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return false;
    }
    
    /**
    * Initialises gameboard
    * @param   width   game board width that will also be its height
    */  
    public void initGameBoard(int width) {        
        this.gameState = new GameState(width);
    }
    
    public String[][] getGameBoard() {
        return gameState.getGameBoard();
    }
    
    public void setGameSquare(int i, int j, String chip) {
        gameState.setGameSquare(i, j, chip);
    }
    
    /**
     * Checks if it is player X's turn
     * @return true, if it is player X's turn
     */
    public boolean isTurnX() {
        return gameState.isTurnX();
    }
    
    /**
     * Change turn from player X to O and vice versa when executed
     */
    public void changeTurn() {
        if (gameState.isTurnX()) {
            gameState.setTurnX(false);
            gameState.incrementMovesCount();
        } else {
            gameState.setTurnX(true);
            gameState.incrementMovesCount();
        }
    }
    
    /**
     * Checks if winner is found after every move
     * @return empty string, if winner not found and game is still on. If game has ended, N if it was a tie, X/O, if either one has won the game
     */
    public String checkStatus() {
        return gameState.checkGameStatus();
    }
    
    /**
     * Checks if it is ok to log in player
     * @param playerName player's name
     * @return true, if player is on the player list, else false
     */
    public boolean login(String playerName) {
        try {
            List<Player> players = playerDao.list();
            for (Player player: players) {
                if (player.getName().equals(playerName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return false;
    }
    
    public List<Player> getPlayers() {
        
        List<Player> players = new ArrayList<>();
        
        try {
            players = playerDao.list();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return players;
    }
    
    public void setPlayerX(String playerX) {
        gameState.setPlayerX(playerX);
    }
    
    public void setPlayerO(String playerO) {
        gameState.setPlayerO(playerO);
    }
    
    public String getPlayerX() {
        return gameState.getPlayerX();
    }
    
    public String getPlayerO() {
        return gameState.getPlayerO();
    }
    
}
