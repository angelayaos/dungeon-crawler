package unsw.dungeon;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonApplication extends Application {

    private DungeonController dungeonController;
    private Stage mainStage;
    private FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("Dungeon");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        TitleScreenController controller = new TitleScreenController();
        loader.setController(controller);
        
    	Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
