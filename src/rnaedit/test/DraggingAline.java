package rnaedit.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
 


 
public class DraggingAline extends JPanel {
    Line2D.Double line = new Line2D.Double(125,75,300,250);
    double S = 10.0;
    Rectangle2D.Double[] rects;
    boolean showRects = false;
 
    public DraggingAline() {
        rects = new Rectangle2D.Double[3];
        for(int j = 0; j < rects.length; j++)
            rects[j] = new Rectangle2D.Double();
        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();
        rects[0] = new Rectangle2D.Double(x1-S/2, y1-S/2, S, S);
        rects[1] = new Rectangle2D.Double(x2-S/2, y2-S/2, S, S);
        rects[2] = new Rectangle2D.Double();
        setCenter();
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.blue);
        g2.draw(line);
        if(showRects) {
            g2.setPaint(Color.red);
            for(int j = 0; j < rects.length; j++)
                g2.draw(rects[j]);
        }
    }
 
    public void setShowRects(boolean show) {
        showRects = show;
        repaint();
    }
 
    public void setRect(int index, double x, double y) {
        switch(index) {
            case 2:
                double dy = y - rects[2].y;
                double dx = x - rects[2].x;
                rects[0].setFrame(rects[0].x+dx, rects[0].y+dy, S, S);
                rects[1].setFrame(rects[1].x+dx, rects[1].y+dy, S, S);
                break;
            default:
                rects[index].setFrame(x, y, S, S);
        }
        setLine();
    }
 
    private void setLine() {
        line.setLine(getCenter(rects[0]), getCenter(rects[1]));
        setCenter();
        repaint();
    }
 
    private Point2D.Double getCenter(Rectangle2D.Double r) {
        return new Point2D.Double(r.getCenterX(), r.getCenterY());
    }
 
    private void setCenter() {
        double cx = line.getX1() + (line.getX2() - line.getX1())/2;
        double cy = line.getY1() + (line.getY2() - line.getY1())/2;
        rects[2].setFrameFromCenter(cx, cy, cx+S/2, cy+S/2);
    }
 
    public static void main(String[] args) {
        DraggingAline test = new DraggingAline();
        LineHandler handler = new LineHandler(test);
        test.addMouseListener(handler);
        test.addMouseMotionListener(handler);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(test);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
class LineHandler extends MouseInputAdapter {
    DraggingAline view;
    int S = 6;
    Rectangle net = new Rectangle(S,S);
    int selectedIndex = -1;
    Point2D.Double offset = new Point2D.Double();
    boolean dragging = false;
 
    public LineHandler(DraggingAline dal) {
        view = dal;
    }
 
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if(!view.showRects) {
            net.setFrameFromCenter(p.x, p.y, p.x+S/2, p.y+S/2);
            if(view.line.intersects(net)) {
                view.setShowRects(true);
            }
        } else {
            Rectangle2D.Double[] rects = view.rects;
            for(int j = 0; j < rects.length; j++) {
                if(rects[j].contains(p)) {
                    selectedIndex = j;
                    offset.x = p.x - rects[j].x;
                    offset.y = p.y - rects[j].y;
                    dragging = true;
                }
            }
            if(selectedIndex == -1) {
                view.setShowRects(false);
            }
        }
    }
 
    public void mouseReleased(MouseEvent e) {
        selectedIndex = -1;
        dragging = false;
    }
 
    public void mouseDragged(MouseEvent e) {
        if(dragging) {
            double x = e.getX() - offset.x;
            double y = e.getY() - offset.y;
            view.setRect(selectedIndex, x, y);
        }
    }
    
}