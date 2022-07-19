package ru.chess.onlinechessfx.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.chess.onlinechessfx.game.Board;

public class Figure extends ImageView {

    public Figure figure;
    private boolean color;
    private int x;
    private int y;

    public int getFigureX() {
        return x;
    }

    public int getFigureY() {
        return y;
    }

    private String name;
    private boolean hasMoved;

    private boolean doubleStep;

    public boolean isDoubleStep() {
        return this.doubleStep;
    }

    public void setDoubleStep(boolean doubleStep) {
        this.doubleStep = doubleStep;
    }

    public Figure() {
        color = false;
        x = 0;
        y = 0;
        name = "-";
    }

    public Figure(boolean color, int x, int y, String name){
        this.color = color;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setImage(){
        Image image = new Image("C:\\Users\\lexaf\\IdeaProjects\\OnlineChessFX\\src\\main\\java\\ru\\chess\\onlinechessfx\\figures\\images\\" + this.name + ".png");
        this.setImage(image);
    }


    public boolean isColor() {
        return color;
    }

    public boolean isHasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public Boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public void setFigureX(int x) {
        this.x = x;
    }

    public void setFigureY(int y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean reChecking(int x_1, int y_1, int x_2, int y_2, Board board){
        return false;
    };


    @Override
    public String toString() {
        return name;
    }

}
