package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class DataSheetGraph extends JPanel{
    private static final long serialVersionUID = 1L;
    private DataSheet dataSheet = null;
    private boolean isConnected;
    private int deltaX;
    private int deltaY;
    transient private Color color;

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
        repaint();
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
        repaint();
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
        repaint();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public DataSheetGraph() {
        super();
        initialize();
    }

    private void initialize() {
        isConnected = false;
        deltaX = 5;
        deltaY = 5;
        color = Color.red;
        this.setSize(300, 400);
    }

    private double minX() {
        double result = 0;
        if (dataSheet != null) {
            int size = dataSheet.numData();
            for (int i = 0; i < size; i++)
                if (dataSheet.getX(i) < result)
                    result = dataSheet.getX(i);
        }
        return result;
    }

    private double maxX() {
        double result = 0;
        if (dataSheet != null) {
            int size = dataSheet.numData();
            for (int i = 0; i < size; i++)
                if (dataSheet.getX(i) > result)
                    result = dataSheet.getX(i);
        }
        return result;
    }

    private double minY() {
        double result = 0;
        if (dataSheet != null) {
            int size = dataSheet.numData();
            for (int i = 0; i < size; i++)
                if (dataSheet.getY(i) < result)
                    result = dataSheet.getY(i);
        }
        return result;
    }

    private double maxY() {
        double result = 0;
        if (dataSheet != null) {
            int size = dataSheet.numData();
            for (int i = 0; i < size; i++)
                if (dataSheet.getY(i) > result)
                    result = dataSheet.getY(i);
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        showGraph(g2);
    }

    public void showGraph(Graphics2D gr) {
        double xMin, xMax, yMin, yMax;
        double width = getWidth();
        double height = getHeight();
        xMin = minX() - deltaX;
        xMax = maxX() + deltaX;
        yMin = minY() - deltaY;
        yMax = maxY() + deltaY;
// Визначаємо коефіцієнти перетворення та положення початку координат
        double xScale = width / (xMax - xMin);
        double yScale = height / (yMax - yMin);
        double x0 = -xMin * xScale;
        double y0 = yMax * xScale;
// Заповнюємо область графіка білим кольором
        Paint oldColor = gr.getPaint();
        gr.setPaint(Color.WHITE);
        gr.fill(new Rectangle2D.Double(0.0, 0.0, width, height));
        Stroke oldStroke = gr.getStroke();
        Font oldFont = gr.getFont();
// Створюємо лінії сітки
// Сітка для вісі X
        float[] dashPattern = {10, 10};
        gr.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
        gr.setFont(new Font("Serif", Font.BOLD, 14));
// Взагалі слід створити метод для обчислення кроку сітки
        double xStep = 1;
        for (double dx = xStep; dx < xMax; dx += xStep) {
            double x = x0 + dx * xScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(x, 0, x, height));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dx / xStep) * xStep + "", (int) x + 2, 10);
        }
        for (double dx = -xStep; dx >= xMin; dx -= xStep) {
            double x = x0 + dx * xScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(x, 0, x, height));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dx / xStep) * xStep + "", (int) x + 2, 10);
        }
// Сітка для вісі Y
// Взагалі слід створити метод для обчислення кроку сітки
        double yStep = 1;
        for (double dy = yStep; dy < yMax; dy += yStep) {
            double y = y0 - dy * yScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(0, y, width, y));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dy / yStep) * yStep + "", 2, (int) y - 2);
        }
        for (double dy = -yStep; dy >= yMin; dy -= yStep) {
            double y = y0 - dy * yScale;
            gr.setPaint(Color.LIGHT_GRAY);
            gr.draw(new Line2D.Double(0, y, width, y));
            gr.setPaint(Color.BLACK);
            gr.drawString(Math.round(dy / yStep) * yStep + "", 2, (int) y - 2);
        }
// Вісі координат
        gr.setPaint(Color.BLACK);
        gr.setStroke(new BasicStroke(3.0f));
        gr.draw(new Line2D.Double(x0, 0, x0, height));
        gr.draw(new Line2D.Double(0, y0, width, y0));
        gr.drawString("X", (int) width - 10, (int) y0 - 2);
        gr.drawString("Y", (int) x0 + 2, 10);
// Відображаємо точки, якщо визначено сховище
        if (dataSheet != null) {
            if (!isConnected) {
                for (int i = 0; i < dataSheet.numData(); i++) {
                    double x = x0 + (dataSheet.getX(i) * xScale);
                    double y = y0 - (dataSheet.getY(i) * yScale);
                    gr.setColor(Color.white);
                    gr.fillOval((int) (x - 5 / 2), (int) (y - 5 / 2), 5, 5);
                    gr.setColor(color);
                    gr.drawOval((int) (x - 5 / 2), (int) (y - 5 / 2), 5, 5);
                }
            } else {
                gr.setPaint(color);
                gr.setStroke(new BasicStroke(2.0f));
                double xOld = x0 + dataSheet.getX(0) * xScale;
                double yOld = y0 - dataSheet.getY(0) * yScale;
                for (int i = 1; i < dataSheet.numData(); i++) {
                    double x = x0 + dataSheet.getX(i) * xScale;
                    double y = y0 - dataSheet.getY(i) * yScale;
                    gr.draw(new Line2D.Double(xOld, yOld, (double) x, y));
                    xOld = x;
                    yOld = y;
                }
            }
// Відновляємо вихідні значення
            gr.setPaint(oldColor);
            gr.setStroke(oldStroke);
            gr.setFont(oldFont);
        }
    }
}
