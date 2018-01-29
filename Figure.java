package arthur.towerOfHanoi;

import arthur.figure.Circle;
import arthur.figure.FigureCanvas;

import java.awt.*;


abstract public class Figure implements Cloneable, Runnable {
    static final Color DEFAULT_COLOR = Color.red;


    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    private FigureCanvas figureCanvas;
    private int dX;
    private int dY;
    private boolean isRunning;
    private boolean isPaused;
    private Thread t;

    private float traceRelation = .5f;

    public Figure() {
    }

    public Figure(int x, int y, int width, int height) {
        this(x, y, width, height, DEFAULT_COLOR);
    }

    public Figure(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Figure(int x, int y, int width, int height, FigureCanvas figureCanvas) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.figureCanvas = figureCanvas;
    }

    public Figure(int x, int y, int width, int height, Color color, FigureCanvas figureCanvas) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.figureCanvas = figureCanvas;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public FigureCanvas getFigureCanvas() {
        return figureCanvas;
    }

    public void setFigureCanvas(FigureCanvas figureCanvas) {
        this.figureCanvas = figureCanvas;
    }

    abstract public void draw(Graphics g);

    abstract public boolean isBelong(int x, int y);

    public void move(int dX, int dY) {
        this.x += dX;
        this.y += dY;
    }

    @Override
    public void run() {

        while (isRunning) {
            if (isPaused) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            checkDirection();
            move(dX, dY);
            figureCanvas.repaint();
            try {
                t.sleep(10);
            } catch (InterruptedException e) {
                System.out.println();
            }
        }
    }

    public void start() {
        if (t != null) {
            stop();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        dY = dY == 0 ? 3 : dY;
        dX = dX == 0 ? 3 : dX;
        t = new Thread(this, "t - artur.javaDump.homework.thread");
        isRunning = true;
        t.start();
    }

    public void pause() {
        System.out.println(Thread.currentThread());
        isPaused = true;
    }

    public synchronized void resume() {
        if (isPaused) {
            isPaused = false;
            notify();
        }
    }

    public void stop() {
        resume();
        dX = 0;
        dY = 0;
        isRunning = false;
    }

    private void checkDirection() {

        if (this.y + height >= figureCanvas.getHeight()) {
            dY = dY < 0 ? dY : -dY;
            // addTraceToBottom();
        }
        if (this.x + width >= figureCanvas.getWidth()) {
            dX = dX < 0 ? dX : -dX;
            //   addTraceToRight();
        }
        if (this.y <= 0) {
            dY = dY < 0 ? -dY : dY;
            //   addTraceToTop();
        }
        if (this.x <= 0) {
            dX = dX < 0 ? -dX : dX;
            //  addTraceToLeft();
        }
    }

    public void addTraceToRight() {
        int traceDiameter = (int) (width * traceRelation);
        int tX = figureCanvas.getWidth() - traceDiameter;
        int tY = y + height / 2 - traceDiameter / 2;
        Circle circle = new Circle(tX, tY, traceDiameter, figureCanvas, Color.BLUE);
        figureCanvas.addFigure(circle);
    }

    public void addTraceToLeft() {
        int traceDiameter = (int) (width * traceRelation);
        Circle circle = new Circle(0, getY(), traceDiameter, figureCanvas, Color.BLUE);
        figureCanvas.addFigure(circle);
    }

    public void addTraceToBottom() {
        int traceDiameter = (int) (width * traceRelation);
        int tX = x + width / 2 - traceDiameter / 2;
        int tY = figureCanvas.getHeight() - traceDiameter;
        Circle circle = new Circle(tX, tY, traceDiameter, figureCanvas, Color.BLUE);
        figureCanvas.addFigure(circle);
    }

    public void addTraceToTop() {
        int traceDiameter = (int) (width * traceRelation);
        Circle circle = new Circle(getX(), 0, traceDiameter, figureCanvas, Color.BLUE);
        figureCanvas.addFigure(circle);
    }

}