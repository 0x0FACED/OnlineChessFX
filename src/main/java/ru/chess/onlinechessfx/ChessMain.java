package ru.chess.onlinechessfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChessMain extends Application {
    //for urls
    public static final String userName = "lexaf";
    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Image image = new Image("C:\\Users\\" + userName + "\\IdeaProjects\\OnlineChessFX\\src\\main\\java\\ru\\chess\\onlinechessfx\\mainMenu.png");
        Image iconImage = new Image("C:\\Users\\" + userName + "\\IdeaProjects\\OnlineChessFX\\src\\main\\java\\ru\\chess\\onlinechessfx\\figures\\images\\HB.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(800);
        imageView.setFitWidth(800);
        pane.getChildren().add(imageView);
        Button buttonNewGame = new Button("New Game");
        Button buttonLoadGame = new Button("Load Last Game");
        pane.getChildren().add(buttonNewGame);
        pane.getChildren().add(buttonLoadGame);

        buttonNewGame.setFont(Font.font("Bold", FontWeight.BOLD, 20));
        buttonLoadGame.setFont(Font.font("Bold", FontWeight.BOLD, 20));

        buttonNewGame.setLayoutX(50);
        buttonNewGame.setLayoutY(50);
        buttonNewGame.setMinHeight(50);
        buttonNewGame.setMinWidth(220);

        buttonLoadGame.setLayoutX(50);
        buttonLoadGame.setLayoutY(130);
        buttonLoadGame.setMinHeight(50);
        buttonLoadGame.setMinWidth(220);

        Scene primaryScene = new Scene(pane, 800, 800);
        Stage stage = new Stage();
        stage.setScene(primaryScene);
        stage.setTitle("The Best CHESS Game in the World!");
        stage.getIcons().add(iconImage);
        stage.setScene(primaryScene);

        stage.show();

        buttonNewGame.setOnAction(actionEvent -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("chess-view.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            primaryStage.setTitle("Chess");
            primaryStage.setScene(new Scene(root, 800, 800));
            primaryStage.setResizable(false);

            stage.getIcons().add(iconImage);
            stage.close();
            primaryStage.show();
        });

        buttonLoadGame.setOnAction(actionEvent -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("chess-view-loadGame.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            primaryStage.setTitle("Chess");
            stage.getIcons().add(iconImage);
            primaryStage.setScene(new Scene(root, 800, 800));
            primaryStage.setResizable(false);
            stage.close();
            primaryStage.show();
        });

    }
    public static void main(String[] args) {
        launch(args);
    }
}

