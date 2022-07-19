package ru.chess.onlinechessfx;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.chess.onlinechessfx.game.ChessGame;

import java.io.IOException;

public class Controller {

    @FXML
    GridPane chessBoard;

    public void initialize() throws IOException {

        // второй аргумент - нужна ли загрузка сохранения
        ChessGame game = new ChessGame(chessBoard, false);

    }
}

