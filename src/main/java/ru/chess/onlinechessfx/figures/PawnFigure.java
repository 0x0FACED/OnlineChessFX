package ru.chess.onlinechessfx.figures;

import ru.chess.onlinechessfx.game.Board;

//пешка
//все ходы реализованы проверено by vladilshk
// нужно добавить превращение в другие фигуры и разобраться с пролетом
public class PawnFigure extends Figure {

    public PawnFigure(int x, int y, boolean color, String name) {
        super(color, x, y, name);
        setImage();
    }

    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board) {

        //white double step
        if (x_2 - x_1 == 2 && !(this.getColor()) && y_1 == y_2 && !isHasMoved()) {
            if (board.getElement(x_2 - 1, y_2).getName().equals("11") || board.getElement(x_2 - 1, y_2).getName().equals("00")) {
                if ((board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00"))) {
                    setHasMoved(true);
                    this.setDoubleStep(true);
                    return true;
                }
            }
        }
        //black double step
        if (x_1 - x_2 == 2 && (this.getColor()) && y_1 == y_2 && !isHasMoved()) {
            if (board.getElement(x_2 + 1, y_2).getName().equals("11") || board.getElement(x_2 + 1, y_2).getName().equals("00")) {
                if ((board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00"))) {
                    setHasMoved(true);
                    this.setDoubleStep(true);
                    return true;
                }
            }
        }

        //white oneStep
        if (x_2 - x_1 == 1 && !(this.getColor()) && y_1 == y_2 && (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00"))) {
            setHasMoved(true);
            return true;
        }

        //black oneStep
        if (x_1 - x_2 == 1 && (this.getColor()) && y_1 == y_2 && (board.getElement(x_2, y_2).getName().equals("11") || board.getElement(x_2, y_2).getName().equals("00"))) {
            setHasMoved(true);
            return true;
        }

        //white eat
        if (x_2 - x_1 == 1 && !(this.getColor()) && ((y_2 - y_1 == 1) || (y_1 - y_2 == 1))) {
            if (!(board.getElement(x_2, y_2).getName().equals("11")) && !(board.getElement(x_2, y_2).getName().equals("00"))) {
                setHasMoved(true);
                return true;
            }
        }

        //black eat
        if (x_1 - x_2 == 1 && this.getColor() && ((y_2 - y_1 == 1) || (y_1 - y_2 == 1))) {
            if (!(board.getElement(x_2, y_2).getName().equals("11")) && !(board.getElement(x_2, y_2).getName().equals("00"))) {
                setHasMoved(true);
                return true;
            }
        }

        return false;
    }

}
