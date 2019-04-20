package GA;

public class Point {

    // A point in the terrain of the level
    int x,y;
    double distance;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.distance = Integer.MAX_VALUE;// Distance to landing zone
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }

    public double getDistanceTo(Point other){
        return Math.sqrt(
                Math.pow(this.x - other.x, 2)
                        + Math.pow(this.y - other.y, 2)
        );
    }
    public double getDistance(double x, double y) {
        return Math.sqrt(
                Math.pow(this.x - x, 2)
                        + Math.pow(this.y - y, 2)
        );
    }

}
