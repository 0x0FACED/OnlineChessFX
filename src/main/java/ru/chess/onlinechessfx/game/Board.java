package ru.chess.onlinechessfx.game;

import ru.chess.onlinechessfx.figures.*;
import ru.chess.onlinechessfx.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Figure[][] body;

    private GridPane board;

    private List<Square> squares = new ArrayList<>();

    public Square getSquare(int x, int y) {
        return squares.get(x * 8 + y);
    }

    public Board() {
        this.body = new Figure[ChessGame.MAX_SIZE][ChessGame.MAX_SIZE];
    }

    public Board(GridPane board, boolean isLoad) throws IOException {
        this.body = new Figure[ChessGame.MAX_SIZE][ChessGame.MAX_SIZE];
        this.board = board;

        fillBoard();
        if (!isLoad) {
            makeGraphicBoard(board);
            createFigures();
            addFigures();
            printBoard();
        } else {
            makeGraphicBoard(board);
            loadSave();
        }
    }

    public void loadSave() throws IOException {

        // путь до папки с сохранением у каждого свой
        FileInputStream fileToLoad = new FileInputStream("C:\\Users\\lexaf\\IdeaProjects\\OnlineChessFX\\src\\main\\java\\ru\\chess\\onlinechessfx\\saves\\save.txt");

        StringBuilder sb = new StringBuilder();
        int k;
        while ((k = fileToLoad.read()) != -1) {
            sb.append((char) k);
        }

        // здесь мы уже из стринг билдера загрузили наше сохранение
        String s = String.valueOf(sb);
        // присвоили строке и создали массив строк, где regex - пробел
        String[] split = s.split(" ");

        // так как файл с сохранением хранит в себе расстановку фигру после последнего хода и последнего игрока,
        // то мы его загружаем, чтобы не ходил всегда белый
        ChessGame.player = split[64].equals("\ntrue");

        // переменные, необходимые для прохода по сохранению и созданию с расстановкой фигур на доске
        int x = 1;
        int counter = 0;
        int counterChar = 0;

        // основной цикл, в ходе которого мы расставим все фигуры на доску
        for (int i = 0; i < squares.size(); ++i) {
            Square square = squares.get(counter);
            if (split[i].charAt(counterChar) == '\n') {
                counterChar = 1;
            } else {
                counterChar = 0;
            }

            if (split[i].charAt(counterChar) == 'R') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new RookFigure(square.getX(), square.getY(), false, "RW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new RookFigure(square.getX(), square.getY(), true, "RB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            } else if (split[i].charAt(counterChar) == 'H') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new HorseFigure(square.getX(), square.getY(), false, "HW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new HorseFigure(square.getX(), square.getY(), true, "HB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            } else if (split[i].charAt(counterChar) == 'B') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new BishopFigure(square.getX(), square.getY(), false, "BW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new BishopFigure(square.getX(), square.getY(), true, "BB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            } else if (split[i].charAt(counterChar) == 'Q') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new QueenFigure(square.getX(), square.getY(), false, "QW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new QueenFigure(square.getX(), square.getY(), true, "QB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            } else if (split[i].charAt(counterChar) == 'K') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new KingFigure(square.getX(), square.getY(), false, "KW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new KingFigure(square.getX(), square.getY(), true, "KB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            } else if (split[i].charAt(counterChar) == 'P') {
                if (split[i].charAt(counterChar + 1) == 'W') {
                    Figure figure = new PawnFigure(square.getX(), square.getY(), false, "PW");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                } else {
                    Figure figure = new PawnFigure(square.getX(), square.getY(), true, "PB");
                    addFigure(square, figure);
                    body[square.getY()][square.getX()] = figure;
                }
            }

            // мое личное изобретение. крч объясняю.
            // так как в нашем проекте довольно странно определены координаты (квадраты, arraylist, figure[][]), то возникают траблы.
            // конечно, это фиксится, если покопаться, но придется половину кода переписывать, что может и не получиться.
            // так что есть варик забить и делать после каждой фигуры counter += 8, ибо у квадратов порядок по столбцам (сверху вниз)
            // а у матрицы, на которой вся логика построена - слева направо (как и в обычной матрице), то есть нам в square надо
            // перешагивать на 8 клеток вправо постоянно, а когда дойдем до конца (56+ индекса), то просто присвоим значение, которое на 1
            // больше, то есть по сути просто сделаем переход на новую строку. иначе говоря, таким образом я имитирую проход как в матрице по arraylist'у.
            // костыль, да, но переделывать все - еще больше костылей родить.
            counter += 8;
            if (counter >= 64) {
                counter = x;
                ++x;
            }
        }
    }

    public void makeGraphicBoard(GridPane board) {
        for (int i = 0; i < ChessGame.MAX_SIZE; i++) {
            for (int j = 0; j < ChessGame.MAX_SIZE; j++) {
                Square square = new Square(i, j);
                square.setName("Square" + i + j);
                square.setPrefHeight(100);
                square.setPrefWidth(100);
                square.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                setTheme(square, i, j);
                board.add(square, i, j, 1, 1);
                squares.add(square);
            }
        }
    }

    private void setTheme(Square square, int i, int j) {
        Color color1 = Color.SANDYBROWN;
        Color color2 = Color.FLORALWHITE;

        if ((i + j) % 2 == 0) {
            square.setBackground(new Background(new BackgroundFill(color1, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            square.setBackground(new Background(new BackgroundFill(color2, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void addFigure(Square square, Figure figure) {
        square.getChildren().add(figure);
        square.occupied = true;
    }

    private void addFigures() {
        for (Square square : squares) {
            if (square.occupied) continue;
            if (square.getY() == 1) {
                addFigure(square, new PawnFigure(square.getX(), square.getY(), true, "PB"));
            } else if (square.getY() == 6) {
                addFigure(square, new PawnFigure(square.getX(), square.getY(), false, "PW"));
            } else if (square.getY() == 0) {
                if (square.getX() == 4) {
                    addFigure(square, new KingFigure(square.getX(), square.getY(), true, "KB"));
                }
                if (square.getX() == 3) {
                    addFigure(square, new QueenFigure(square.getX(), square.getY(), true, "QB"));
                }
                if (square.getX() == 2 || square.getX() == 5) {
                    addFigure(square, new BishopFigure(square.getX(), square.getY(), true, "BB"));
                }
                if (square.getX() == 1 || square.getX() == 6) {
                    addFigure(square, new HorseFigure(square.getX(), square.getY(), true, "HB"));
                }
                if (square.getX() == 0 || square.getX() == 7) {
                    addFigure(square, new RookFigure(square.getX(), square.getY(), true, "RB"));
                }
            } else if (square.getY() == 7) {
                if (square.getX() == 4) {
                    addFigure(square, new KingFigure(square.getX(), square.getY(), false, "KW"));
                }
                if (square.getX() == 3) {
                    addFigure(square, new QueenFigure(square.getX(), square.getY(), false, "QW"));
                }
                if (square.getX() == 2 || square.getX() == 5) {
                    addFigure(square, new BishopFigure(square.getX(), square.getY(), false, "BW"));
                }
                if (square.getX() == 1 || square.getX() == 6) {
                    addFigure(square, new HorseFigure(square.getX(), square.getY(), false, "HW"));
                }
                if (square.getX() == 0 || square.getX() == 7) {
                    addFigure(square, new RookFigure(square.getX(), square.getY(), false, "RW"));
                }
            }
        }
    }

    public void createFigures() {
        // x - строка
        // У - столбик

        // КОРОЛЕВЫ
        QueenFigure queenWhite = new QueenFigure(7, 3, false, "QW");
        body[7][3] = queenWhite;
        QueenFigure queenBlack = new QueenFigure(0, 3, true, "QB");
        body[0][3] = queenBlack;

        // КОРОЛИ
        KingFigure kingWhite = new KingFigure(7, 4, false, "KW");
        body[7][4] = kingWhite;
        KingFigure kingBlack = new KingFigure(0, 4, true, "KB");
        body[0][4] = kingBlack;

        // КОНИ
        HorseFigure horseWhite_1 = new HorseFigure(7, 1, false, "HW");
        body[7][1] = horseWhite_1;
        HorseFigure horseWhite_2 = new HorseFigure(7, 6, false, "HW");
        body[7][6] = horseWhite_2;
        HorseFigure horseBlack_1 = new HorseFigure(0, 1, true, "HB");
        body[0][1] = horseBlack_1;
        HorseFigure horseBlack_2 = new HorseFigure(0, 6, true, "HB");
        body[0][6] = horseBlack_2;

        // ЛАДЬИ
        RookFigure rookWhite_1 = new RookFigure(7, 0, false, "RW");
        body[7][0] = rookWhite_1;
        RookFigure rookWhite_2 = new RookFigure(7, 7, false, "RW");
        body[7][7] = rookWhite_2;
        RookFigure rookBlack_1 = new RookFigure(0, 0, true, "RB");
        body[0][0] = rookBlack_1;
        RookFigure rookBlack_2 = new RookFigure(0, 7, true, "RB");
        body[0][7] = rookBlack_2;

        //ОШИЦЕРЫ
        BishopFigure bishopWhite_1 = new BishopFigure(7, 2, false, "BW");
        body[7][2] = bishopWhite_1;
        BishopFigure bishopWhite_2 = new BishopFigure(7, 5, false, "BW");
        body[7][5] = bishopWhite_2;
        BishopFigure bishopBlack_1 = new BishopFigure(0, 2, true, "BB");
        body[0][2] = bishopBlack_1;
        BishopFigure bishopBlack_2 = new BishopFigure(0, 5, true, "BB");
        body[0][5] = bishopBlack_2;

        // ПЕШКИ
        PawnFigure pawnWhite_1 = new PawnFigure(6, 0, false, "PW");
        body[6][0] = pawnWhite_1;
        PawnFigure pawnWhite_2 = new PawnFigure(6, 1, false, "PW");
        body[6][1] = pawnWhite_2;
        PawnFigure pawnWhite_3 = new PawnFigure(6, 2, false, "PW");
        body[6][2] = pawnWhite_3;
        PawnFigure pawnWhite_4 = new PawnFigure(6, 3, false, "PW");
        body[6][3] = pawnWhite_4;
        PawnFigure pawnWhite_5 = new PawnFigure(6, 4, false, "PW");
        body[6][4] = pawnWhite_5;
        PawnFigure pawnWhite_6 = new PawnFigure(6, 5, false, "PW");
        body[6][5] = pawnWhite_6;
        PawnFigure pawnWhite_7 = new PawnFigure(6, 6, false, "PW");
        body[6][6] = pawnWhite_7;
        PawnFigure pawnWhite_8 = new PawnFigure(6, 7, false, "PW");
        body[6][7] = pawnWhite_8;

        PawnFigure pawnBlack_1 = new PawnFigure(1, 0, true, "PB");
        body[1][0] = pawnBlack_1;
        PawnFigure pawnBlack_2 = new PawnFigure(1, 1, true, "PB");
        body[1][1] = pawnBlack_2;
        PawnFigure pawnBlack_3 = new PawnFigure(1, 2, true, "PB");
        body[1][2] = pawnBlack_3;
        PawnFigure pawnBlack_4 = new PawnFigure(1, 3, true, "PB");
        body[1][3] = pawnBlack_4;
        PawnFigure pawnBlack_5 = new PawnFigure(1, 4, true, "PB");
        body[1][4] = pawnBlack_5;
        PawnFigure pawnBlack_6 = new PawnFigure(1, 5, true, "PB");
        body[1][5] = pawnBlack_6;
        PawnFigure pawnBlack_7 = new PawnFigure(1, 6, true, "PB");
        body[1][6] = pawnBlack_7;
        PawnFigure pawnBlack_8 = new PawnFigure(1, 7, true, "PB");
        body[1][7] = pawnBlack_8;

    }

    public void fillBoard() {
        WhiteCell whiteCell = new WhiteCell();
        BlackCell blackCell = new BlackCell();
        for (int x = 0; x < ChessGame.MAX_SIZE; ++x) {
            for (int y = 0; y < ChessGame.MAX_SIZE; ++y) {
                if ((x + y) % 2 == 0) {
                    body[x][y] = /*"00"*/ whiteCell;
                } else {
                    body[x][y] = /*"11"*/ blackCell;
                }
            }
        }
    }

    public void printBoard() {
        StringBuilder str = new StringBuilder();
        str.append(" ");
        for (int i = 0; i < ChessGame.MAX_SIZE; i++) {
            str.append("   ");
            str.append(i);
        }

        str.append("\n");

        int counterStr = 0;
        for (int x = 0; x < ChessGame.MAX_SIZE; x++) {
            str.append(counterStr + " [");
            for (int y = 0; y < ChessGame.MAX_SIZE; y++) {
                str.append(" ");
                str.append(body[x][y].toString());
                str.append(" ");
            }

            str.append("]");
            str.append("\n");
            counterStr++;
        }
        String finishedStr = String.valueOf(str);
        System.out.println(finishedStr);
    }

    public Figure[][] getBody() {
        return body;
    }

    public Figure getElement(int x, int y) {
        return body[x][y];
    }

    public void setElement(int x, int y, Figure obj) {
        body[x][y] = obj;
    }

    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < ChessGame.MAX_SIZE; ++i) {
            for (int j = 0; j < ChessGame.MAX_SIZE; ++j) {
                newBoard.body[i][j] = this.body[i][j];
            }
        }

        return newBoard;
    }
}


