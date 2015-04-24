package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by John on 4/22/15.
 */
public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
        Scene scene = new Scene(root, 305, 579);

        Image icon16 = new Image(getClass().getResourceAsStream("calendar16.png"));
        Image icon24 = new Image(getClass().getResourceAsStream("calendar24.png"));
        Image icon32 = new Image(getClass().getResourceAsStream("calendar32.png"));
        Image icon48 = new Image(getClass().getResourceAsStream("calendar48.png"));
        stage.getIcons().addAll(icon16, icon24, icon32, icon48);

        stage.setTitle("A Java Calandar App");
        stage.setScene(scene);

        System.out.println(stage.getIcons().size());
        stage.show();
    }
}
