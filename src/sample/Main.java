package sample;

import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/MyView.fxml").openStream());

        Scene scene = new Scene(root,800,700);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(700);
        primaryStage.setTitle("Alice In Wonderland Maze Game");
        primaryStage.getIcons().add(new Image("file:resources/images/Icon.jpeg"));
        scene.getStylesheets().add("View/Main.css");

        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        primaryStage.setScene(scene);

        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(windowEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("are you sure you want to leave the game?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                primaryStage.close();
                Platform.exit();
            } else {
                windowEvent.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
