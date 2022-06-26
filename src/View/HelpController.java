package View;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable{
    public javafx.scene.control.Button close;
    public javafx.scene.control.Label textInformation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInformation.setWrapText(true);
        textInformation.setText("""
                How to play:
                Up = 8       Up & Left = 7
                Down = 2     Up & Right = 9
                Left = 4     Down & Left = 1
                Right = 6    Down & Right = 3""");
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}
