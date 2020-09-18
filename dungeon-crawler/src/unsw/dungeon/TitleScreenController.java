package unsw.dungeon;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;


public class TitleScreenController {

    @FXML
	private Pane titleScreen;
	
	@FXML 
    private Button playButton;

	@FXML
    private Button howToButton;

    @FXML
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/ARCADEPI.TTF"), 11);
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/Minecraftia-Regular.ttf"), 88);
    }
    
    @FXML
    public void displayHowTo(ActionEvent event) throws IOException {
        Stage title = (Stage) titleScreen.getScene().getWindow();
        title.close();
        
		Stage primaryStage = new Stage();
        primaryStage.setTitle("How To");
    
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("HowTo.fxml"));
    	Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        Stage title = (Stage) titleScreen.getScene().getWindow();
        title.close();

		Stage primaryStage = new Stage();
        primaryStage.setTitle("Dungeon");

        // DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("maze.json");
        // DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("boulders.json");
        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("portal.json");
        // DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("advanced.json");

        DungeonController controller = dungeonLoader.loadController();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("Dungeon.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        root.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
