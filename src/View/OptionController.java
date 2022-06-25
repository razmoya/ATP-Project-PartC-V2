package View;

import Server.Configurations;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class OptionController implements Initializable {
    public javafx.scene.control.Button close;
    public javafx.scene.control.Button save;
    public ChoiceBox algorithms;
    public ChoiceBox mazeType;
    public ChoiceBox threadNum;
    String algo="BFS";
    String maze="MyMazeGenerator";
    String core="2";

    public void close() {
        Platform.exit();
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }

    public void save() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(String.format("Settings Saved \n "+ "\n Algorithm: "+algo+ "\n MazeType: "+maze+ "\n Number of thread: "+core));
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configurations.instance();

        algorithms.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch","BestFirstSearch");
        mazeType.getItems().addAll("SimpleMazeGenerator", "MyMazeGenerator");
        threadNum.getItems().addAll("1", "2", "3", "4");

    }

    public void SetConfiguration() throws IOException {
        OutputStream output=null;
        InputStream input=null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("./Resources/config.properties"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + ",");
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            String text = stringBuilder.toString();
            bufferedReader.close();
        } catch (IOException e) {
        }
        if (input == null) {
            output = new FileOutputStream("Resources/config.properties");
            Properties prop = new Properties();
            if (algorithms.getValue()==(String)"BreadthFirstSearch")
                algo="BreadthFirstSearch";
            if (algorithms.getValue()==(String)"DepthFirstSearch")
                algo="DepthFirstSearch";
            if (algorithms.getValue()==(String)"BestFirstSearch")
                algo="BestFirstSearch";
            if (mazeType.getValue()==(String)"SimpleMazeGenerator")
                maze="SimpleMazeGenerator";
            if (mazeType.getValue()==(String)"MyMazeGenerator")
                maze="MyMazeGenerator";
            if (threadNum.getValue()==(String)"1")
                core="1";
            if (threadNum.getValue()==(String)"2")
                core="2";
            if (threadNum.getValue()==(String)"3")
                core="3";
            if (threadNum.getValue()==(String)"4")
                core="4";

            prop.setProperty("threadPoolSize", core);
            prop.setProperty("mazeGeneratingAlgorithm", maze);
            prop.setProperty("mazeSearchingAlgorithm", algo);
            prop.store(output, null);
        }

        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        save();
    }
}