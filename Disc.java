package arthur.towerOfHanoi;

import arthur.dynamicarray.DynamicArray;
import arthur.figure.Figure;

import java.awt.*;

/**
 * Created by User on 17.01.17.
 */
public class Disc extends Figure {

    public Disc(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Disc(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean isBelong(int x, int y) {
        return false;
    }

}
