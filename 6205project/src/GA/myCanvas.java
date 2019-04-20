package GA;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

class myCanvas extends Canvas {

    List<List<List<Point>>> landers;

    public myCanvas (List<List<List<Point>>> landers) {
        this.landers = landers;
        setBackground (Color.GRAY);
        setSize(1920, 1080);
    }

    public void paint (Graphics g) {
        Graphics2D g2;
        g2 = (Graphics2D) g;



        for(int i=0; i<this.landers.size(); i++){
            int[] xs = {0,20,100,200,400,420,440,500,1378,1398,820,840,700,620,680};
            int[] ys = {300,740,730,400,400,500,640,760,740,300,280,600,640,580,220};

            g.drawPolyline(xs, ys, 15);
            for(int j=0; j<200; j++){
                for(int k=1; k<this.landers.get(i).get(j).size(); k++){
                    Point point1 = this.landers.get(i).get(j).get(k-1);
                    Point point2 = this.landers.get(i).get(j).get(k);
                    g2.drawLine(point1.getX()/5, 800-point1.getY()/5, point2.getX()/5, 800-point2.getY()/5);
                }
            }
           // super.paint(g);
        }
        //g2.drawString ("Hey", 300, 300);
        //g2.drawString ("Hey", 350, 130);
    }

}