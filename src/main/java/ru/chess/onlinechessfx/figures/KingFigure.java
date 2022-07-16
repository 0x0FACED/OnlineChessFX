package ru.chess.onlinechessfx.figures;

import ru.chess.onlinechessfx.game.Board;

//все ходы реализованы проверено by vladilshk
// rechecking в доработке не нуждается
// ахуеть я круто его сделал
public class KingFigure extends Figure {

    public KingFigure(int x, int y, boolean color, String name) {
        super(color, x, y, name);
        setImage();
    }

    @Override
    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board) {

        if (((x_1 - x_2 <= 1) && (x_1 - x_2 >= -1)) && ((y_1 - y_2 <= 1 && y_1 - y_2 >= -1))) {
            return true;
        }
        return false;
    }

}
