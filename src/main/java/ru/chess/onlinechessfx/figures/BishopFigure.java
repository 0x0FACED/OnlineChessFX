package ru.chess.onlinechessfx.figures;

import ru.chess.onlinechessfx.game.Board;

//слон  ОФИЦЕР
public class BishopFigure extends Figure {

    public BishopFigure(int x, int y, boolean color, String name) {
        super(color, x, y, name);
        setImage();
    }

    @Override
    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board) {
        if (x_2 > x_1 && y_2 > y_1 && (x_2 - x_1 == y_2 - y_1)) {
            int y = y_1;
            for (int x = x_1 + 1; x < x_2; x++) {
                y++;
                if (!(board.getElement(x, y).getName().equals("11")) && !(board.getElement(x, y).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }
        //диагональ лево вверх white
        if (x_2 > x_1 && y_2 < y_1 && (x_2 - x_1 == y_1 - y_2)) {
            int y = y_1;
            for (int x = x_1 + 1; x < x_2; x++) {
                y--;
                if (!(board.getElement(x, y).getName().equals("11")) && !(board.getElement(x, y).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }
        //диагональ право вниз white
        if (x_2 < x_1 && y_2 > y_1 && (x_1 - x_2 == y_2 - y_1)) {
            int y = y_1;
            for (int x = x_1 - 1; x > x_2; x--) {
                y++;
                if (!(board.getElement(x, y).getName().equals("11")) && !(board.getElement(x, y).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }
        //диагональ лево вниз white
        if (x_2 < x_1 && y_2 < y_1 && (x_1 - x_2 == y_1 - y_2)) {
            int y = y_1;
            for (int x = x_1 - 1; x > x_2; x--) {
                y--;
                if (!(board.getElement(x, y).getName().equals("11")) && !(board.getElement(x, y).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
