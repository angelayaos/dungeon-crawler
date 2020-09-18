package unsw.dungeon;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class LoseScreenController {

    @FXML
    private Pane loseScreen;

    @FXML
    private Button retryButton;

    @FXML
	public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/ARCADEPI.TTF"), 11);
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/Minecraftia-Regular.ttf"), 72);
    }

    @FXML
    void backToLevel(ActionEvent event) throws IOException {
        Stage title = (Stage) loseScreen.getScene().getWindow();
        title.close();

		Stage primaryStage = new Stage();
        primaryStage.setTitle("Dungeon");

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("portal.json");
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
