package ru.chess.onlinechessfx;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import ru.chess.onlinechessfx.game.ChessGame;

public class Controller {

    @FXML
    GridPane chessBoard;

    public void initialize(){

        // Themes are Coral, Dusk, Wheat, Marine, Emerald, Sandcastle

        ChessGame game = new ChessGame(chessBoard);

    }
}

