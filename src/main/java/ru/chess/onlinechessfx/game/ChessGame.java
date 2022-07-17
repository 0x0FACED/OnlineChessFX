package ru.chess.onlinechessfx.game;

import javafx.event.EventTarget;
import javafx.scene.layout.GridPane;
import ru.chess.onlinechessfx.Square;
import ru.chess.onlinechessfx.figures.*;

import java.util.Scanner;


//при речекинге королю ставится hasMoved
public class ChessGame {

    private int selectedX;
    private int selectedY;
    private boolean figureSelected = false;
    private Figure selectedFigure;

    public static final int MAX_SIZE = 8;
    private final Board board;
    private boolean player;
    private boolean checkWhite;
    private boolean checkBlack;

    private boolean matWhite;
    private boolean matBlack;

    private int whiteKingX;
    private int whiteKingY;

    private int blackKingX;
    private int blackKingY;

    public ChessGame(GridPane board) {
        this.board = new Board(board);
        this.player = false;
        whiteKingX = 0;
        whiteKingY = 4;

        blackKingX = 7;
        blackKingY = 4;

        checkBlack = false;
        checkWhite = false;
        matWhite = false;
        matBlack = false;
        /*startGame();*/
        addEventHandlers(board);
    }

    /*public void startGame() {
        board.printBoard();

        while (true) {
            if (!player) {
                System.out.println("White step");
            }
            if (player) {
                System.out.println("Black step");
            }
            System.out.println("Enter the coordinates: ");
            Scanner sc = new Scanner(System.in);
            int x_1 = sc.nextInt();
            int y_1 = sc.nextInt();
            int x_2 = sc.nextInt();
            int y_2 = sc.nextInt();

            if ((!player && !board.getElement(x_1, y_1).getColor()) || (player && board.getElement(x_1, y_1).getColor())) {
                if (!move(x_1, y_1, x_2, y_2)) {
                    System.out.println("You can't do this step");
                } else {
                    player = !player;
                    board.printBoard();
                }
            } else if (player && !board.getElement(x_1, y_1).getColor()) {
                System.out.println("It's black turn now");
            } else if (!player && board.getElement(x_1, y_1).getColor()) {
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
                break;
            }
            if (matBlack) {
                System.out.println("White wins");
                break;
            }

            checkWhite = false;
            checkBlack = false;
        }
    }*/

    private void addEventHandlers(GridPane chessBoard){
        chessBoard.setOnMouseClicked(event -> {
            EventTarget target = event.getTarget();


            // Clicked on square
            if(target.toString().equals("Square")) {
                Square square = (Square) target;
                if (square.occupied && !figureSelected){ //empty fal
                    selectedX = square.getX();
                    selectedY = square.getY();
                    figureSelected = true;
                    System.out.println(figureSelected);

                    selectedFigure = (Figure)square.getChildren().get(0);

                } else if (figureSelected){
                    int secondX = square.getX();
                    int secondY = square.getY();
                    move(selectedY, selectedX, secondY, secondX);
                    figureSelected = !figureSelected;
                    System.out.println(figureSelected);
                    board.printBoard();
                    System.out.println(selectedY + " " + selectedX);
                    System.out.println(secondY + " " + secondX);

                    Square initialSquare = (Square) selectedFigure.getParent();
                    square.getChildren().add(selectedFigure);
                    square.occupied = true;
                    initialSquare.getChildren().removeAll();
                    initialSquare.occupied = false;
                    selectedFigure.setFigureX(square.getX());
                    selectedFigure.setFigureY(square.getY());
                }

              //  square.getChildren();

            } /*else {
                Figure newFigure = (Figure) target;
                Square square = (Square) newFigure.getParent();
                System.out.println(newFigure.getName());
                System.out.println(newFigure.getFigureX() + " " + newFigure.getFigureY());
            }*/

        });
    }

    public boolean move(int x_1, int y_1, int x_2, int y_2) {
        //рокировка
        if (castling(x_1, y_1, x_2, y_2)) {
            board.printBoard();
            return true;
        }

        if (takingOnThePassCheck(x_1, y_1, x_2, y_2)) {
            board.setElement(x_2, y_2, board.getElement(x_1, y_1));
            initNullCell(x_1, y_1);
            initNullCell(x_1, y_2);
            return true;
        }

        // замена пешки
        if (pawnUpdate(x_1, y_1, x_2, y_2)) {
            board.getElement(x_2, y_2).setFigureX(x_2);
            board.getElement(x_2, y_2).setFigureY(y_2);
            board.printBoard();

            return true;
        }

        // обычный ход
        if (check(x_1, y_1, x_2, y_2)) {
            if (board.getElement(x_1, y_1).reChecking(x_1, y_1, x_2, y_2, board)) {
                board.setElement(x_2, y_2, board.getElement(x_1, y_1));
                initNullCell(x_1, y_1);
                //board.printBoard();
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

        }
        if (checkKing(true, this.board, blackKingX, blackKingY)) {
            System.out.println("Check for black");
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
        WhiteCell wc = new WhiteCell();
        BlackCell bc = new BlackCell();
        // если атакующая пешка черная
        if (player) {
            if (board.getElement(x_1, y_1).getColor() && x_1 == 3) {
                // если наша черная пешка стоит по 0-й вертикали
                if (y_1 == 0 && player != board.getElement(x_1, y_1 + 1).getColor() && board.getElement(x_1, y_1 + 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                    // если наша черная пешка стоит по 7-й вертикали
                } else if (y_1 == 7 && player != board.getElement(x_1, y_1 - 1).getColor() && board.getElement(x_1, y_1 - 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }

                // общий случай для черной пешки
                if (player != board.getElement(x_1, y_1 + 1).getColor() && board.getElement(x_1, y_1 + 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }
                if (player != board.getElement(x_1, y_1 - 1).getColor() && board.getElement(x_1, y_1 - 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }
            }
        } else {
            if (!board.getElement(x_1, y_1).getColor() && x_1 == 4) {
                // если наша черная пешка стоит по 0-й вертикали
                if (y_1 == 0 && player != board.getElement(x_1, y_1 + 1).getColor() && board.getElement(x_1, y_1 + 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                    // если наша черная пешка стоит по 7-й вертикали
                } else if (y_1 == 7 && player != board.getElement(x_1, y_1 - 1).getColor() && board.getElement(x_1, y_1 - 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }

                // общий случай для черной пешки
                if (player != board.getElement(x_1, y_1 + 1).getColor() && board.getElement(x_1, y_1 + 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }
                if (player != board.getElement(x_1, y_1 - 1).getColor() && board.getElement(x_1, y_1 - 1).isDoubleStep()) {
                    if (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // замена пешки
    public boolean pawnUpdate(int x_1, int y_1, int x_2, int y_2) {
        if (!check(x_1, y_1, x_2, y_2)) {
            return false;
        }

        //white pawn
        if ((board.getElement(x_1, y_1).getName().equals("PW") && x_1 == 6 && x_2 == 7 && (y_1 == y_2 || y_1 - y_2 == 1 || y_1 - y_2 == -1)) || (board.getElement(x_1, y_1).getName().equals("PB") && x_1 == 1 && x_2 == 0 && (y_1 == y_2 || y_1 - y_2 == 1 || y_1 - y_2 == -1))) {
            boolean localColor = board.getElement(x_1, y_1).getColor();
            while (true) {
                System.out.println("Select the shape number:");
                System.out.println("1 - Queen");
                System.out.println("2 - Horse");
                System.out.println("3 - Rook");
                System.out.println("4 - Bishop");
                Scanner sc = new Scanner(System.in);
                int number = sc.nextInt();
                initNullCell(x_1, y_1);
                if (number == 1) {
                    if (!localColor) {
                        QueenFigure newQueen = new QueenFigure(x_2, y_2, localColor, "QW");
                        board.setElement(x_2, y_2, newQueen);
                    } else {
                        QueenFigure newQueen = new QueenFigure(x_2, y_2, localColor, "QB");
                        board.setElement(x_2, y_2, newQueen);
                    }
                    return true;
                } else if (number == 2) {
                    if (!localColor) {
                        HorseFigure newHorse = new HorseFigure(x_2, y_2, localColor, "HW");
                        board.setElement(x_2, y_2, newHorse);
                    } else {
                        HorseFigure newHorse = new HorseFigure(x_2, y_2, localColor, "HB");
                        board.setElement(x_2, y_2, newHorse);
                    }
                    return true;
                } else if (number == 3) {
                    if (!localColor) {
                        RookFigure newRook = new RookFigure(x_2, y_2, localColor, "RW");
                        board.setElement(x_2, y_2, newRook);
                    }
                    if (localColor) {
                        RookFigure newRook = new RookFigure(x_2, y_2, localColor, "RB");
                        board.setElement(x_2, y_2, newRook);
                    }
                    return true;
                } else if (number == 4) {
                    if (!localColor) {
                        BishopFigure newBishop = new BishopFigure(x_2, y_2, localColor, "BW");
                        board.setElement(x_2, y_2, newBishop);
                    }
                    if (localColor) {
                        BishopFigure newBishop = new BishopFigure(x_2, y_2, localColor, "BB");
                        board.setElement(x_2, y_2, newBishop);
                    }
                    return true;
                } else {
                    System.out.println("Enter number of the shape");
                }
            }
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
