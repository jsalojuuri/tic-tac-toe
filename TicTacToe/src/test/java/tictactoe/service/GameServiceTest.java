package tictactoe.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import tictactoe.dao.Dao;
import tictactoe.dao.FilePlayerDao;

/**
 *
 * @author salojuur
 */
public class GameServiceTest {
    
    private Properties properties;
    private String userTestFile;
    private Dao dao;
    private GameService gameService;
    
    
    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        userTestFile = properties.getProperty("userTestFile");
        dao = new FilePlayerDao(userTestFile);
        gameService = new GameService(10, dao);
    }
    
    @Test
    public void constructorWorksIfProperUserFileIsGiven() {
        assertNotNull(gameService);
    }
    
    @Test
    public void gameBoardIsInitalizedRight() {
        gameService.initGameBoard(25);
        assertEquals(25, gameService.getGameBoard().length);
    }
    
    @Test
    public void gameSquareIsSetCorrect() {
        gameService.setGameSquare(0, 0, "X");
        assertEquals("X", gameService.getGameBoard()[0][0]);
    }
    
    @Test
    public void playerTurnGetsChangedRight() {
        assertEquals(true, gameService.isTurnX());
        gameService.changeTurn();
        assertEquals(false, gameService.isTurnX());
        gameService.changeTurn();
        assertEquals(true, gameService.isTurnX());
    }
    
    @Test
    public void gameStatusIsCorrect() {
        assertEquals("", gameService.checkStatus());
        gameService.setGameSquare(0, 0, "X");
        gameService.setGameSquare(0, 1, "X");
        gameService.setGameSquare(0, 2, "X");
        gameService.setGameSquare(0, 3, "X");
        gameService.setGameSquare(0, 4, "X");
        assertEquals("X", gameService.checkStatus());
    }
    
    @Test
    public void getAndSetPlayers() {
        assertEquals("Player X", gameService.getPlayerX());
        assertEquals("Player O", gameService.getPlayerO());
        gameService.setPlayerX("X");        
        assertEquals("X", gameService.getPlayerX());
        gameService.setPlayerO("O");        
        assertEquals("O", gameService.getPlayerO());
    }
    
    @Test
    public void createAndGetandLoginPlayerWorks() {
        gameService.createPlayer("testPlayerABC");
        List<Player> players = gameService.getPlayers();
        assertNotNull(players);
        assertEquals("testPlayerABC", players.get(0).getName());
        assertTrue(gameService.login("testPlayerABC"));
    }
    
    @Test
    public void cannotAddPlayerThatAlreadyExists() {
        gameService.createPlayer("testPlayerABC2");
        List<Player> players = gameService.getPlayers();
        assertEquals("testPlayerABC2", players.get(0).getName());
        assertEquals(false, gameService.createPlayer("testPlayerABC2"));
    }
    
    @Test
    public void deletePlayerWorks() {
        gameService.createPlayer("testPlayerABC3");
        List<Player> players = gameService.getPlayers();
        assertEquals("testPlayerABC3", players.get(0).getName());
        gameService.deletePlayer("testPlayerABC3");
        players = gameService.getPlayers();
        assertEquals(true,players.isEmpty());
    }
    
    @Test
    public void canNotDeletePlayerThatDoesNotExist() {
        assertEquals(false, gameService.deletePlayer("foo"));
    }
        
    @After
    public void tearDown() {
        gameService.deletePlayer("testPlayerABC");
        gameService.deletePlayer("testPlayerABC2");
        gameService.deletePlayer("testPlayerABC3");
    }
    
}
