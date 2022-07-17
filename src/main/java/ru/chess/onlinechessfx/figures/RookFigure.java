package ru.chess.onlinechessfx.figures;

import ru.chess.onlinechessfx.game.Board;

//ладья
//все ходы реализованы проверены by vladilshk
// rechecking в доработке не нуждается
public class RookFigure extends Figure {

    public RookFigure(int x, int y, boolean color, String name) {
        super(color, x, y, name);
        setImage();
    }

    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board) {
        //вперёд white назад black
        if (y_1 == y_2 && x_1 < x_2) {
            for (int i = x_1 + 1; i < x_2; i++) {
                if (!(board.getElement(i, y_1).getName().equals("11")) && !(board.getElement(i, y_1).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }

        //назад white вперёд black
        if (y_1 == y_2 && x_1 > x_2) {
            for (int i = x_1 - 1; i > x_2; i--) {
                if (!(board.getElement(i, y_1).getName().equals("11")) && !(board.getElement(i, y_1).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }

        //право
        if (x_1 == x_2 && y_1 < y_2) {
            for (int i = y_1 + 1; i < y_2; i++) {
                if (!(board.getElement(x_1, i).getName().equals("11")) && !(board.getElement(x_1, i).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }

        //лево
        if (x_1 == x_2 && y_1 > y_2) {
            for (int i = y_1 - 1; i > y_2; i--) {
                if (!(board.getElement(x_1, i).getName().equals("11")) && !(board.getElement(x_1, i).getName().equals("00"))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
