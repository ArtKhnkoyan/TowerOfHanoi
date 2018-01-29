package arthur.towerOfHanoi;

import arthur.dynamicarray.DynamicArray;
import arthur.figure.Figure;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by User on 17.01.17.
 */
public class Peg extends Figure {
    private List<Disc> discs = new ArrayList<>();

    private String title;
    public boolean isSelected;

    public Peg(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Peg(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    private int getXPeg() {
        return getX() - getWidth() * 8;
    }

    private int getYPeg() {
        return getY() + getHeight();
    }

    private int getWidthPeg() {
        return getWidth() * 17;
    }

    private int getHeightPeg() {
        return getWidth() / 2;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        g.fillRect(getXPeg(), getYPeg(), getWidthPeg(), getHeightPeg());
        g.drawString(title, getX(), getY() - 15);
        paint(g);
    }

    @Override
    public boolean isBelong(int x, int y) {
        return x >= getX() && x <= getX() + getWidth()
                && y >= getY() && y <= getY() + getHeight()
                || x >= getXPeg() && x <= getXPeg() + getWidthPeg()
                && y >= getYPeg() && y <= getYPeg() + getHeightPeg();
    }

    public void load(int size) {
        Random random = new Random();
        Color color;
        for (int i = 0; i < size; i++) {
            int diskX = getXPeg() + ((i + 1) * 4);
            int diskY = getYPeg() - ((i + 1) * 21);
            int diskWidth = getWidthPeg() - (2 * (i + 1) * 4);
            int diskHeight = 20;
            color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
            discs.add(new Disc(diskX, diskY, diskWidth, diskHeight, color));
        }
    }

    public void loadDiscsEndPeg() {
        for (int i = 0; i < discs.size(); i++) {
            int diskX = getXPeg() + (getWidthPeg() - discs.get(i).getWidth()) / 2;
            int diskY = getYPeg() - ((i + 1) * 21);
            discs.get(i).setX(diskX);
            discs.get(i).setY(diskY);
        }
    }

    public void clearDiscs() {
        discs.clear();
    }

    public Disc lastDisc() {
        return discs.get(discs.size() - 1);
    }

    public void addDisc(Disc disc) {
        discs.add(disc);
    }

    public void removeDisc() {
        discs.remove(discs.size() - 1);
    }

    public int getDiscsCount() {
        return discs.size();
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), 1000);
        for (int i = 0; i < discs.size(); i++) {
            discs.get(i).draw(g);
        }
    }
}
