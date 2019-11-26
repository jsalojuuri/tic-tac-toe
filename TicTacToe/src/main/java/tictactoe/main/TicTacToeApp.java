package tictactoe.main;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tictactoe.dao.FilePlayerDao;
import tictactoe.dao.H2PlayerDao;
import tictactoe.domain.Player;
import tictactoe.service.GameService;

/**
 *
 * @author salojuur
 */
public class TicTacToeApp extends Application {
    private GameService gameService;
    private BorderPane appScreen;
    private VBox startPane;
    private VBox startInputPane;
    private VBox newPlayerPane;
    private GridPane gameBoard;
    
    private Scene startScene;
    private Scene newPlayerScene;
    private Scene gameScene;

    private Label startLabel;
    private Label gameLabel;
    
    private Font font;
    
    @Override
    public void init() throws Exception { 
        
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        String userFile = properties.getProperty("userFile");
        FilePlayerDao playerDao = new FilePlayerDao(userFile);
        
        this.gameService = new GameService(20, playerDao);
        this.appScreen = new BorderPane();  
        this.gameBoard = new GridPane();
        this.startPane = new VBox(10);
        this.startInputPane = new VBox(10);
        this.newPlayerPane = new VBox(10);
        this.font = new Font("Arial", 30);
        
        this.gameLabel = new Label("Player X, please make your move");
        gameLabel.setFont(font);
        this.startLabel = new Label("Tic-Tac-Toe");
        startLabel.setFont(font);
    }
    
    @Override
    public void start(Stage primaryStage) {

        // Start inputpane: welcome text & inputs
        Text startText = new Text("PREGAME SETUP");
        Label playerXLabel = new Label("Player X");
        Label playerOLabel = new Label("Player O");
        ComboBox playerXBox = setupPlayerBox();
        ComboBox playerOBox = setupPlayerBox();  
        startInputPane.getChildren().addAll(startText, playerXLabel, playerXBox, playerOLabel, playerOBox);
        
        // Start pane: infomessage & buttons
        Label infoMessage = new Label();
        Button startGameButton = new Button("Start new game");
        Button createPlayerButton = new Button("Create new player");
 
        startGameButton.setOnAction(e->{
            primaryStage.setScene(gameScene);
        });  
        
        createPlayerButton.setOnAction(e->{
            primaryStage.setScene(newPlayerScene);   
        });
        
        // Start scene
        startPane.getChildren().addAll(infoMessage, startInputPane, startGameButton, createPlayerButton);
        startScene = new Scene(startPane, 250, 250);
        
        
        //New playername pane: input
        HBox newPlayerNamePane = new HBox(10);
        newPlayerNamePane.setPadding(new Insets(10));
        
        
        TextField newPlayerNameInput = new TextField(); 
        Label newPlayerNameLabel = new Label("Player name");
        newPlayerNameLabel.setPrefWidth(100);
        newPlayerNamePane.getChildren().addAll(newPlayerNameLabel, newPlayerNameInput);
        
        // New player pane: message & button
        Label playerCreationMessage = new Label();
        Button createNewPlayerButton = new Button("create");
        createNewPlayerButton.setPadding(new Insets(10));

        createNewPlayerButton.setOnAction(e->{
            String playerName = newPlayerNameInput.getText();
   
            if ( playerName.length()< 2 ) {
                playerCreationMessage.setText("Name too short");
                playerCreationMessage.setTextFill(Color.RED);              
            } else if ( gameService.createPlayer(playerName) ){
                playerCreationMessage.setText("");                
                infoMessage.setText("new player created");
                infoMessage.setTextFill(Color.GREEN);
                primaryStage.setScene(startScene);      
            } else {
                playerCreationMessage.setText("Name already taken, please ue another name");
                playerCreationMessage.setTextFill(Color.RED);        
            }
 
        });  
        
        newPlayerPane.getChildren().addAll(playerCreationMessage, newPlayerNamePane, createNewPlayerButton); 
       
        newPlayerScene = new Scene(newPlayerPane, 300, 250);
        
        
        // Game scene
        appScreen.setTop(gameLabel);
        appScreen.setCenter(gameBoard);
        gameScene = new Scene(appScreen, 1000, 1000);
        setGameBoard(gameService.getGameBoard());

        //primary stage
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(startScene);
        primaryStage.show();
        
    }
    
    public ComboBox setupPlayerBox() {
        ComboBox box = new ComboBox();
        List<Player> players = new ArrayList<>();
        players = gameService.getPlayers();
        for (Player player: players) {
            box.getItems().add(player.getName());
        }
        return box;
    }
    
    public void setGameBoard(String[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                Button button = createButton(i,j);
                this.gameBoard.add(button, i, j);
            }
        }  
    }
    
    public Button createButton(int i , int j) {
        Button button = new Button();
        button.setPrefSize(80, 40);
        button.setFont(font);
        button = setStateFor(button, i, j);
        return button;
    }
    
    public void checkStatus(String player, String opponent) {
        
        if (gameService.checkStatus().equals(player)) {
            gameLabel.setText("PLAYER " + player + " WON!");
            gameBoard.setDisable(true);
        } else {
            gameLabel.setText("Player " + opponent + " , please make your move ");
        } 
    }
    
    public Button setStateFor(Button button, int i, int j) {
        button.setOnAction((event) -> {
            if (button.getText().isEmpty()) {
                if (gameService.isTurnX()) {
                    button.setText("X");
                    gameService.setGameSquare(i, j, "X");
                    gameService.changeTurn();
                    checkStatus("X", "O");
                    
                } else {
                    button.setText("O");
                    gameService.setGameSquare(i, j, "O");
                    gameService.changeTurn();
                    checkStatus("O", "X");
                }
            } 
        });
        return button;
    }
         
    public static void main(String[] args) {
        launch(args);
    }
    
}