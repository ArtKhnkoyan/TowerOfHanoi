package arthur.towerOfHanoi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by User on 25.12.16.
 */
public class CanvasHanoi extends JPanel {
    private ArrayList<Peg> pegs = new ArrayList<>();

    enum Mode {UP, FLIGHT, DOWN, DONE}

    private int mX;
    private int mY;
    private boolean isSelected = false;
    private Mode mode = Mode.UP;
    private int dX;
    private int dY;
    private int discSize;

    private Peg pegFirst;
    private Peg pegSecond;
    private Peg pegThird;

    private Peg startPeg;
    private Peg endPeg;
    private boolean isRunning;
    private boolean isPaused = false;
    private boolean isStopping = false;
    private boolean isLoadingDiscs = false;
    private Thread t;


    public CanvasHanoi() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressedPerformed(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseDraggedPerformed(e);
            }
        });
    }

    private Peg getSelected() {
        for (int i = 0; i < pegs.size(); i++) {
            if (pegs.get(i).isSelected) {
                return pegs.get(i);
            }
        }
        return null;
    }

    private void select(int x, int y) {
        clearSelects();
        for (int i = 0; i < pegs.size(); i++) {
            if (pegs.get(i).isBelong(x, y)) {
                isSelected = true;
                pegs.get(i).isSelected = true;
                return;
            }
        }
        isSelected = false;
    }

    private void mousePressedPerformed(MouseEvent e) {
        select(e.getX(), e.getY());
        if (isSelected) {
            repaint();
        }
        mX = e.getX();
        mY = e.getY();
    }

    private void mouseDraggedPerformed(MouseEvent e) {
        if (isSelected) {
            getSelected().move(e.getX() - mX, e.getY() - mY);
            getSelected().loadDiscsEndPeg();
            repaint();
        }
        mX = e.getX();
        mY = e.getY();
    }

    private void clearSelects() {
        for (int i = 0; i < pegs.size(); i++) {
            pegs.get(i).isSelected = false;
        }
    }

    public void addPeg(Peg peg) {
        pegs.add(peg);
    }

    public void drawPegs() {
        for (int i = 0; i < 3; i++) {
            addPeg(new Peg(150, 200, 7, 220, Color.RED));
        }
        pegs.get(0).setTitle("PegFirst");
        pegs.get(1).setX(350);
        pegs.get(1).setColor(Color.BLUE);
        pegs.get(1).setTitle("PegSecond");
        pegs.get(2).setX(550);
        pegs.get(2).setColor(Color.ORANGE);
        pegs.get(2).setTitle("PegThird");
    }

    public void addDiscs(int size) {
        isLoadingDiscs = true;
        System.out.println(isRunning);
        if (!isRunning) {
            pegs.get(0).load(size);
            discSize = size;
            repaint();
        }
    }

    public void clearDiscs() {
        if (!isRunning) {
            for (int i = 0; i < pegs.size(); i++) {
                pegs.get(i).clearDiscs();
            }
        }
    }

    private void up() {

        if ((startPeg.lastDisc().getY() + startPeg.lastDisc().getHeight()) > (startPeg.getY() - 3)) {
            dY = -3;
            dX = 0;
        } else {
            this.mode = Mode.FLIGHT;
        }
    }

    private void flight() {
        if ((startPeg.lastDisc().getY() + startPeg.lastDisc().getHeight()) > endPeg.getY()) {
            dY = -3;
            dX = 0;
        } else {
            if ((startPeg.lastDisc().getX() + startPeg.lastDisc().getWidth() / 2) < endPeg.getX()) {
                dY = 0;
                dX = 3;
            } else if ((startPeg.lastDisc().getX() + startPeg.lastDisc().getWidth() / 2) > (endPeg.getX() + endPeg.getWidth() / 2)) {
                dY = 0;
                dX = -3;
            } else if ((startPeg.lastDisc().getY() + startPeg.lastDisc().getHeight()) < (endPeg.getY() - 3)) {
                dY = 3;
                dX = 0;
            } else {
                endPeg.addDisc(startPeg.lastDisc());
                this.mode = Mode.DOWN;
            }
        }
    }

    private void down() {

        if ((startPeg.lastDisc().getY() + startPeg.lastDisc().getHeight()) <
                (endPeg.getY() + endPeg.getHeight() - endPeg.getDiscsCount() * startPeg.lastDisc().getHeight())) {
            dX = 0;
            dY = 3;
        } else {
            this.mode = Mode.DONE;
        }
    }

    private void done() {
        startPeg.removeDisc();
        this.mode = Mode.UP;
        endPeg.loadDiscsEndPeg();
    }

    private synchronized void discPausing() {
        if (isPaused) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveDiscsRecursion(int n, Peg pegFirst, Peg pegSecond, Peg pegThird) {

        if (n == 1) {
            startPeg = pegFirst;
            endPeg = pegThird;
            moveToPeg();
        } else {
            moveDiscsRecursion(n - 1, pegFirst, pegThird, pegSecond);
            startPeg = pegFirst;
            endPeg = pegThird;
            moveToPeg();
            moveDiscsRecursion(n - 1, pegSecond, pegFirst, pegThird);
        }
        int discCount = endPeg.getDiscsCount();
        if (discCount == discSize) {
            isRunning = false;
            t = null;
        }


    }

    private void moveToPeg() {
        while (mode != Mode.DONE) {
            if (t != null && isStopping) {
                System.out.println("isStop: " + isStopping);
                try {
                    t.join();
                    stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            discPausing();
            switch (mode) {
                case UP:
                    up();
                    break;
                case FLIGHT:
                    flight();
                    break;
                case DOWN:
                    down();
            }
            startPeg.lastDisc().move(dX, dY);
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mode == Mode.DONE) {
            done();
            repaint();
        }
    }

    public void moveDiscs(int size) {
        if (isStopping) {
            isStopping = false;
            t = null;
        }

        pegFirst = pegs.get(0);
        pegSecond = pegs.get(1);
        pegThird = pegs.get(2);

        if (t == null) {
            t = new Thread() {
                @Override
                public void run() {
                    System.out.println("isLoad: " + isLoadingDiscs);
                    if (isLoadingDiscs) {
                        while (isRunning) {

                            //todo call recursive method of hanoi towers
                            moveDiscsRecursion(size, pegFirst, pegSecond, pegThird);
                        }
                    }
                }
            };
            isRunning = true;
            t.start();
        }
    }

    public void stop() {
        System.out.println("thread name: " + Thread.currentThread().getName());
        isRunning = false;
        isStopping = true;
    }

    public synchronized void resume() {
        if (isPaused) {
            isPaused = false;
            notify();
        }
    }

    public void pause() {
        isPaused = true;
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), 1000);
        for (int i = 0; i < pegs.size(); i++) {
            pegs.get(i).draw(g);
        }
    }
}
