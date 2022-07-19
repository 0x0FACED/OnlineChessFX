package ru.chess.onlinechessfx.game;

import javafx.event.EventTarget;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import ru.chess.onlinechessfx.Square;
import ru.chess.onlinechessfx.figures.*;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


//при речекинге королю ставится hasMoved
public class ChessGame {

    private int selectedX;
    private int selectedY;
    private boolean figureSelected = false;
    private Figure selectedFigure;

    public static final int MAX_SIZE = 8;
    private final Board board;
    public static boolean player;
    private boolean checkWhite;
    private boolean checkBlack;

    private boolean matWhite;
    private boolean matBlack;

    private int whiteKingX;
    private int whiteKingY;

    private int blackKingX;
    private int blackKingY;

    public File file = new File("C:\\Users\\booqi\\IdeaProjects\\OnlineChessFX\\src\\main\\java\\ru\\chess\\onlinechessfx\\saves\\save.txt");

    public ChessGame(GridPane board, boolean isLoad) throws IOException {
        this.board = new Board(board, isLoad);
        if (!isLoad){
            player = false;
        }
        whiteKingX = 7;
        whiteKingY = 4;

        blackKingX = 0;
        blackKingY = 4;

        checkBlack = false;
        checkWhite = false;
        matWhite = false;
        matBlack = false;
        addEventHandlers(board);
    }

    private void addEventHandlers(GridPane chessBoard) {
        if (!player) {
            System.out.println("White step");
        }
        if (player) {
            System.out.println("Black step");
        }
        chessBoard.setOnMouseClicked(event -> {
            EventTarget target = event.getTarget();

            Square square = null;

            if (target.toString().equals("Square") || target.toString().charAt(1) == 'W' || target.toString().charAt(1) == 'B' ) {
                if(target.toString().charAt(1) == 'W' || target.toString().charAt(1) == 'B'){
                    Figure newFigure = (Figure) target;
                    square = (Square) newFigure.getParent();
                }
                else if(target.toString().equals("Square")){
                    square = (Square) target;
                }
                
                if (square.occupied && !figureSelected) { //empty fal
                    selectedX = square.getX();
                    selectedY = square.getY();
                    figureSelected = true;

                    selectedFigure = (Figure) square.getChildren().get(0);
                    System.out.println("You select: " + selectedFigure.toString());
                    System.out.println("Coordinates of selected figure: ");
                    System.out.println("X: " + selectedFigure.getFigureY());
                    System.out.println("Y: " + selectedFigure.getFigureX());

                } else if (figureSelected) {
                    int secondX = square.getX();
                    int secondY = square.getY();
                    if ((!player && !board.getElement(selectedY, selectedX).getColor()) || (player && board.getElement(selectedY, selectedX).getColor())) {
                        if (!move(selectedY, selectedX, secondY, secondX, selectedFigure, square)) {
                            System.out.println("You can't do this step");
                        } else {
                            player = !player;
                        }
                    } else if (player && !board.getElement(selectedY, selectedX).getColor()) {
                        System.out.println("It's black turn now");
                    } else if (!player && board.getElement(selectedY, selectedX).getColor()) {
                        System.out.println("It's white turn now");
                    }
                    matAndCheck();
                    if (!player && checkBlack) {
                        matBlack = true;
                    }
                    if (player && checkWhite) {
                        matWhite = true;
                    }

                    if (matWhite) {
                        System.out.println("Black wins");
                        System.exit(0);
                    }
                    if (matBlack) {
                        System.out.println("White wins");
                        System.exit(0);
                    }

                    checkWhite = false;
                    checkBlack = false;
                    figureSelected = !figureSelected;

                    if (!player) {
                        System.out.println("White step");
                    }
                    if (player) {
                        System.out.println("Black step");
                    }

                    try {
                        FileOutputStream fileToSave = new FileOutputStream(file);
                        String data = makeSave();
                        fileToSave.write(data.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public String makeSave(){
        StringBuilder str = new StringBuilder();
        for (int x = 0; x < ChessGame.MAX_SIZE; x++) {
            for (int y = 0; y < ChessGame.MAX_SIZE; y++) {
                str.append(board.getBody()[x][y].toString());
                str.append(" ");
            }

            str.append("\n");
        }
        str.append(player);
        return String.valueOf(str);
    }

    public void moveFigure(Square square){
        Square initialSquare = (Square) selectedFigure.getParent();
        square.getChildren().add(selectedFigure);
        square.occupied = true;
        initialSquare.getChildren().removeAll();
        initialSquare.occupied = false;
        selectedFigure.setFigureX(square.getX());
        selectedFigure.setFigureY(square.getY());
    }

    public void killFigure(Square square){
        Square initialSquare = (Square) selectedFigure.getParent();
        System.out.println("You killed: " + square.getChildren());
        square.getChildren().remove(0);
        square.getChildren().add(selectedFigure);
        square.occupied = true;
        initialSquare.getChildren().removeAll();
        initialSquare.occupied = false;
        selectedFigure.setFigureY(square.getX());
        selectedFigure.setFigureY(square.getY());
    }

    public void doCastling(int x, int kingY, int rookY, int rookY_2){
        Square squareKing = board.getSquare(kingY, x);
        moveFigure(squareKing);
        Square squareRook = board.getSquare(rookY, x);
        selectedFigure = (Figure) squareRook.getChildren().get(0);
        squareRook = board.getSquare(rookY_2, x);
        moveFigure(squareRook);
    }

    public boolean move(int x_1, int y_1, int x_2, int y_2, Figure selectedFigure, Square square) {
        //рокировка
        if (castling(x_1, y_1, x_2, y_2)) {
            board.printBoard();
            return true;
        }

        if (takingOnThePassCheck(x_1, y_1, x_2, y_2)) {
            board.setElement(x_2, y_2, board.getElement(x_1, y_1));
            moveFigure(square);
            if(player){
                Square sqKill = board.getSquare(PawnFigure.passY, PawnFigure.passX - 1);
                System.out.println(sqKill.getChildren().toString());
                sqKill.getChildren().remove(0);
            }
            else {
                Square sqKill = board.getSquare(PawnFigure.passY, PawnFigure.passX + 1);
                System.out.println(sqKill.getChildren().toString());
                sqKill.getChildren().remove(0);
            }
            initNullCell(x_1, y_1);
            initNullCell(x_1, y_2);
            return true;
        }

        // замена пешки
        if (pawnUpdateMenu(x_1, y_1, x_2, y_2, square)) {
            /*board.getElement(x_2, y_2).setFigureX(x_2);
            board.getElement(x_2, y_2).setFigureY(y_2);*/
            board.printBoard();
            return true;
        }

        // обычный ход
        if (check(x_1, y_1, x_2, y_2)) {
            if (selectedFigure.reChecking(x_1, y_1, x_2, y_2, board)) {
                if (!checkForEmptyCell(x_2, y_2)) {
                    moveFigure(square);
                } else if (board.getElement(x_2, y_2).getColor() != board.getElement(x_1, y_1).getColor()) {
                    killFigure(square);
                }

                board.setElement(x_2, y_2, board.getElement(x_1, y_1));
                initNullCell(x_1, y_1);
                board.printBoard();

                if (board.getElement(x_2, y_2).getName().equals("KW")) {
                    whiteKingX = x_2;
                    whiteKingY = y_2;
                }
                if (board.getElement(x_2, y_2).getName().equals("KB")) {
                    blackKingX = x_2;
                    blackKingY = y_2;
                }
                board.getElement(x_2, y_2).setFigureX(x_2);
                board.getElement(x_2, y_2).setFigureY(y_2);

                board.getElement(x_2, y_2).setHasMoved(true);
                if(board.getElement(x_2, y_2).getName().charAt(0) != 'P'){
                    PawnFigure.passX = -1;
                    PawnFigure.passY = -1;
                }
                return true;
            } else {
                System.out.println("Error\n");
            }
        }
        return false;
    }

    public void matAndCheck() {
        if (checkKing(false, this.board, whiteKingX, whiteKingY)) {
            System.out.println("Check for white");
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("CHECK FOR WHITE");

            alert.showAndWait();
        }

        if (checkKing(true, this.board, blackKingX, blackKingY)) {
            System.out.println("Check for black");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("CHECK FOR BLACK");

            alert.showAndWait();
        }

        if (checkWhite) {
            checkMate(false);
        }
        if (checkBlack) {
            checkMate(true);
        }
    }

    public void initNullCell(int x_1, int y_1) {
        WhiteCell whiteCell = new WhiteCell();
        BlackCell blackCell = new BlackCell();

        if ((x_1 + y_1) % 2 == 1) {
            board.setElement(x_1, y_1, blackCell);
        } else {
            board.setElement(x_1, y_1, whiteCell);
        }
    }

    public boolean check(int x_1, int y_1, int x_2, int y_2) {
        //your figure or not
        /*if(player != board.getElement(x_1, y_1).getColor()){
            return false;
        }*/
        //move off the board
        if (x_2 > 7 || y_2 > 7 || x_1 > 7 || y_1 > 7) {
            return false;
        }
        //move an empty cell
        if (board.getElement(x_1, y_1).getName().equals("11") || board.getElement(x_1, y_1).getName().equals("00")) {
            return false;
        }

        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
            return true;
        }

        return !board.getElement(x_1, y_1).getColor().equals(board.getElement(x_2, y_2).getColor());
    }

    //рокировка
    //вроде работает, но нужно тестрировать
    public boolean castling(int x_1, int y_1, int x_2, int y_2) {
        if (!board.getElement(x_1, y_1).getColor()) {
            if (checkWhite) {
                return false;
            }
        }
        if (board.getElement(x_1, y_1).getColor()) {
            if (checkBlack) {
                return false;
            }
        }

        if ((board.getElement(x_1, y_1).getName().charAt(0) == 'K' && board.getElement(x_2, y_2).getName().charAt(0) == 'R') || (board.getElement(x_1, y_1).getName().charAt(0) == 'R' && board.getElement(x_2, y_2).getName().charAt(0) == 'K')) {
            if (board.getElement(x_1, y_1).getColor() == board.getElement(x_2, y_2).getColor()) {
                if (!board.getElement(x_1, y_1).isHasMoved() && !board.getElement(x_2, y_2).isHasMoved()) {
                    int kingX;
                    int kingY;
                    int rookX;
                    int rookY;
                    if (board.getElement(x_1, y_1).getName().charAt(0) == 'K') {
                        kingX = x_1;
                        kingY = y_1;
                        rookX = x_2;
                        rookY = y_2;
                    } else {
                        kingX = x_2;
                        kingY = y_2;
                        rookX = x_1;
                        rookY = y_1;
                    }

                    int firstY;
                    int lastY;
                    if (y_1 < y_2) {
                        firstY = y_1;
                        lastY = y_2;
                    } else {
                        firstY = y_2;
                        lastY = y_1;
                    }

                    int distance = lastY - firstY;
                    for (int i = firstY + 1; i < lastY; ++i) {
                        if (!board.getElement(x_1, i).getName().equals("11") && !board.getElement(x_1, i).getName().equals("00")) {
                            return false;
                        }
                    }

                    board.getElement(x_1, y_1).setHasMoved(true);
                    board.getElement(x_2, y_2).setHasMoved(true);

                    // длинная рокировка
                    if (distance == 4) {
                        board.setElement(x_1, kingY - 2, board.getElement(kingX, kingY));
                        board.setElement(x_1, rookY + 3, board.getElement(rookX, rookY));
                        doCastling(x_1, kingY - 2, rookY, rookY + 3);

                        if (board.getElement(x_1, kingY - 2).getName().equals("KW")) {
                            whiteKingX = x_1;
                            whiteKingY = kingY - 2;
                        }
                        if (board.getElement(x_1, kingY - 2).getName().equals("KB")) {
                            blackKingX = x_1;
                            blackKingY = kingY - 2;
                        }

                        initNullCell(x_1, y_1);
                        initNullCell(x_2, y_2);
                    }

                    // короткая рокировка
                    if (distance == 3) {
                        board.setElement(x_1, kingY + 2, board.getElement(kingX, kingY));
                        board.setElement(x_1, rookY - 2, board.getElement(rookX, rookY));
                        doCastling(x_1, kingY + 2,  rookY, rookY - 2);

                        if (board.getElement(x_1, kingY + 2).getName().equals("KW")) {
                            whiteKingX = x_1;
                            whiteKingY = kingY - 2;
                        }
                        if (board.getElement(x_1, kingY + 2).getName().equals("KB")) {
                            blackKingX = x_1;
                            blackKingY = kingY - 2;
                        }

                        initNullCell(x_1, y_1);
                        initNullCell(x_2, y_2);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    //взятие на проходе
    /*Условия:
     * 1) Только ответным ходом
     * 2) Пешка, совершающая взятие, должна находиться на 5-й горизонтали (для нас 4-й) для белых, а на 4-й (для нас 3-й) для черных.
     * 3) стоит пешка и пешка противка совершает двойной ход. только тогда возможно взятие на проходе. */
    public boolean takingOnThePassCheck(int x_1, int y_1, int x_2, int y_2) {
        // если атакующая пешка черная
        if (board.getElement(x_1, y_1).getName().charAt(0) == 'P') {
            if (player) {
                if (board.getElement(x_1, y_1).getColor() && x_1 == 4) {
                    // если наша черная пешка стоит по 0-й вертикали
                    if (y_1 == 0 && player != board.getElement(x_1, y_1 + 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                        // если наша черная пешка стоит по 7-й вертикали
                    } else if (y_1 == 7 && player != board.getElement(x_1, y_1 - 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }

                    // общий случай для черной пешки
                    if (player != board.getElement(x_1, y_1 + 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }
                    if (player != board.getElement(x_1, y_1 - 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }
                }
            } else {
                if (!board.getElement(x_1, y_1).getColor() && x_1 == 3) {
                    // если наша белая пешка стоит по 0-й вертикали
                    if (y_1 == 0 && player != board.getElement(x_1, y_1 + 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                        // если наша белая пешка стоит по 7-й вертикали
                    } else if (y_1 == 7 && player != board.getElement(x_1, y_1 - 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }

                    // общий случай для белой пешки
                    if (player != board.getElement(x_1, y_1 + 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }
                    if (player != board.getElement(x_1, y_1 - 1).getColor() && PawnFigure.passX == x_2 && PawnFigure.passY == y_2) {
                        if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // замена пешки


    public boolean pawnUpdateMenu(int x_1, int y_1, int x_2, int y_2, Square square) {

        if (board.getElement(x_1, y_1).getName().charAt(0) == 'P'
                && (y_1 == y_2 || y_1 - y_2 == 1 || y_1 - y_2 == -1)
                && ((x_1 == 6 && x_2 == 7) || x_1 == 1 && x_2 == 0)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Pawn Update");
            alert.setHeaderText(null);
            alert.setContentText("Choose new figure");

            ButtonType buttonTypeQueen = new ButtonType("Queen");
            ButtonType buttonTypeRook = new ButtonType("Rook");
            ButtonType buttonTypeHorse = new ButtonType("Horse");
            ButtonType buttonTypeBishop = new ButtonType("Bishop");

            alert.getButtonTypes().setAll(buttonTypeQueen, buttonTypeRook, buttonTypeHorse, buttonTypeBishop);

            Optional<ButtonType> result = alert.showAndWait();
            String col;
            if (player) {
                col = "B";
            } else {
                col = "W";
            }

            Figure newFigure = null;
            if (result.get() == buttonTypeQueen) {
                newFigure = new QueenFigure(y_2, x_2, player, "Q" + col);
            } else if (result.get() == buttonTypeRook) {
                newFigure = new RookFigure(y_2, x_2, player, "R" + col);
            } else if (result.get() == buttonTypeHorse) {
                newFigure = new HorseFigure(y_2, x_2, player, "H" + col);
            } else {
                newFigure = new BishopFigure(y_2, x_2, player, "B" + col);
            }

            if(checkForEmptyCell(x_2, y_2)){
                killFigure(square);
            }else {
                moveFigure(square);
            }
            square.getChildren().remove(0);

            square.getChildren().add(newFigure);
            board.setElement(x_2, y_2, newFigure);
            initNullCell(x_1, y_1);
            System.out.println(board.getSquare(y_1, x_1).getChildren());

            return true;
        }
        return false;
    }

    public boolean checkKing(boolean color, Board board, int kX, int kY) {

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // если фигура противоположного цвета и не пустая клетка
                if (board.getElement(x, y).getColor().equals(!color) && !board.getElement(x, y).getName().equals("11") && !board.getElement(x, y).getName().equals("00")) {
                    if (!color) {
                        if (board.getElement(x, y).reChecking(x, y, kX, kY, board)) {
                            checkWhite = true;
                            return true; // mat white
                        }
                    } else {
                        if (board.getElement(x, y).reChecking(x, y, kX, kY, board)) {
                            checkBlack = true;
                            return true; // mat black
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean checkMate(boolean color) {
        //поставили шах белым, ход белого, проверяем все ходы, если ход возможен и шаха не будет, то возвращаем фолс
        //проверка всех ходов короля
        //virtualStep(color, whiteKingX, whiteKingY, 3, 6);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if ((whiteKingY + y < 8 && whiteKingY + y > -1) && (whiteKingX + x > -1 & whiteKingY + x < 8)) {
                    if (!color && x != -1 && y != -1) {
                        if (!virtualStep(color, whiteKingX, whiteKingY, whiteKingX + x, whiteKingY + y)) {
                            return false;
                        }
                    }
                    if (color && x != -1 && y != -1) {
                        if (!virtualStep(color, blackKingX, blackKingY, blackKingX + x, blackKingY + y)) {
                            return false;
                        }
                    }
                }
            }
        }

        //проверка всех возможных ходов
        for (int x_1 = 0; x_1 < 8; x_1++) {
            for (int y_1 = 0; y_1 < 8; y_1++) {
                if (board.getElement(x_1, y_1).getColor().equals(color) && !board.getElement(x_1, y_1).getName().equals("11") && !board.getElement(x_1, y_1).getName().equals("00")) {
                    for (int x_2 = 0; x_2 < 8; x_2++) {
                        for (int y_2 = 0; y_2 < 8; y_2++) {
                            if (x_1 == 6 && y_1 == 4 && x_2 == 4 && y_2 == 4) {
                                System.out.println();
                            }
                            if (!virtualStep(color, x_1, y_1, x_2, y_2)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (!color) {
            matWhite = true;
            System.out.println("Mat for white");
        } else {
            matBlack = true;
            System.out.println("Mat for black");
        }
        return true;
    }

    public boolean virtualStep(boolean color, int x_1, int y_1, int x_2, int y_2) {
        Board newBoard;
        Figure blackCell = new BlackCell();

        if (board.getElement(x_1, y_1).reChecking(x_1, y_1, x_2, y_2, board) && check(x_1, y_1, x_2, y_2)) {
            newBoard = board.copy();
            newBoard.setElement(x_2, y_2, board.getElement(x_1, y_1));
            newBoard.setElement(x_1, y_1, blackCell);
            if (!color) {
                if (whiteKingX == x_1 && whiteKingY == y_1) {
                    if (!checkKing(color, newBoard, x_2, y_2)) {
                        return false;
                    }
                } else {
                    if (!checkKing(color, newBoard, whiteKingX, whiteKingY)) {
                        return false;
                    }
                }
            }
            if (color) {
                if (blackKingX == x_1 && blackKingY == y_1) {
                    if (!checkKing(color, newBoard, x_2, y_2)) {
                        return false;
                    }
                } else {
                    if (!checkKing(color, newBoard, blackKingX, blackKingY)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Board getBoard() {
        return board;
    }

    public boolean checkForEmptyCell(int x, int y) {
        return (!board.getElement(x, y).getName().equals("11") && !board.getElement(x, y).getName().equals("00"));
    }
}