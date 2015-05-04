package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
        Scene scene = new Scene(root, 310, 600);

        Image icon = new Image(getClass().getResourceAsStream("calendar.png"));
        Image icon16 = new Image(getClass().getResourceAsStream("calendar16.png"));
        Image icon24 = new Image(getClass().getResourceAsStream("calendar24.png"));
        Image icon32 = new Image(getClass().getResourceAsStream("calendar32.png"));
        Image icon48 = new Image(getClass().getResourceAsStream("calendar48.png"));
        Image icon64 = new Image(getClass().getResourceAsStream("calendar64.png"));
        Image icon128 = new Image(getClass().getResourceAsStream("calendar128.png"));
        Image icon256 = new Image(getClass().getResourceAsStream("calendar256.png"));
        Image icon512 = new Image(getClass().getResourceAsStream("calendar512.png"));

        stage.getIcons().addAll(icon, icon16, icon24, icon32, icon48, icon64, icon128, icon256, icon512);
        loadDockIcon();

        stage.setTitle("The Java Day Calendar");
        stage.setScene(scene);

        System.out.println(stage.getIcons().size());
        stage.show();
    }

    public void loadDockIcon(){
        try {
            URL iconURL = Main.class.getResource("calendar512.png");
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            // Won't work on Windows or Linux.
            System.out.println("Except: " + e);
        }
    }
}
