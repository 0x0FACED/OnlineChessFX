package ru.chess.onlinechessfx;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import ru.chess.onlinechessfx.game.ChessGame;

import java.io.IOException;

public class ControllerLoadGame {

    @FXML
    GridPane chessBoard;

    public void initialize() throws IOException {

        // второй аргумент - нужна ли загрузка сохранения
        ChessGame game = new ChessGame(chessBoard, true);
    }
}