package ru.chess.onlinechessfx.figures;

import ru.chess.onlinechessfx.game.Board;

//конь
//все ходы реализованы проверено by vladilshk
// rechecking в доработке не нуждается
public class HorseFigure extends Figure {


    public HorseFigure(int x, int y, boolean color, String name) {
        super(color, x, y, name);
        setImage();
    }

    @Override
    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board) {
        //2 вперёд
        if (x_2 - x_1 == 2 && (y_2 - y_1 == 1 || y_1 - y_2 == 1)) {
            return true;
        }

        //2 назад
        if (x_1 - x_2 == 2 && (y_2 - y_1 == 1 || y_1 - y_2 == 1)) {
            return true;
        }

        //2 вправо
        if (y_2 - y_1 == 2 && (x_2 - x_1 == 1 || x_1 - x_2 == 1)) {
            return true;
        }

        //2 влево
        if (y_1 - y_2 == 2 && (x_2 - x_1 == 1 || x_1 - x_2 == 1)) {
            return true;
        }
        return false;
    }
}
