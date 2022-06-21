package View;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable{
    public javafx.scene.control.Button close;
    public javafx.scene.control.Label textInformation;
    //public javafx.scene.image.ImageView imageOfNumpad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInformation.setWrapText(true);
        textInformation.setText("if you want save maze, create new maze or load maze->> press file.\n"+
                "you can also see the properties and change them in options.\n"+
                "Up = 8       Slant Up & Left = 7 \n"+
                "Down = 2     Slant Up & Right = 9 \n"+
                "Left = 4     Slant Down & Left = 1\n"+
                "Right = 6    Slant Down & Right = 3");

    }
    public void close() {
        Platform.exit();
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}
