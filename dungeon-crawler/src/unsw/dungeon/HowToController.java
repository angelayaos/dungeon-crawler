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

public class HowToController {

    @FXML
    private Pane howTo;

    @FXML
    private Button returnButton;

    @FXML
	public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/ARCADEPI.TTF"), 11);
        Font.loadFont(getClass().getResourceAsStream("src/resources/fonts/Minecraftia-Regular.ttf"), 72);
    }

    @FXML
    public void backToTitleScreen(ActionEvent event) throws IOException {
        Stage instruction = (Stage) howTo.getScene().getWindow();
        instruction.close();
        
		Stage primaryStage = new Stage();
        primaryStage.setTitle("Dungeon");     
           
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("TitleScreen.fxml"));

        TitleScreenController controller = new TitleScreenController();
        loader.setController(controller);

    	Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
