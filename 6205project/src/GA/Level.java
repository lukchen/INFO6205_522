package GA;
import java.util.*;
import java.util.stream.Collectors;

public class Level {

    List<Lander> landers;

    // Read fields
    String[] fields;
    int width;
    int height;
    double g;
    int max_thrust;
    int min_angle;
    int max_angle;
    int landingX1;
    int landingX2;
    int landingY;
    double max_dist;
    List<Point> points;
    String[] defaultLanderFields;

    public Level(String [] levelData) {
        //this.svgContainer = null;
        this.landers = new ArrayList<Lander>();

        // Read fields
        this.fields = levelData[0].split(" ");
        this.width      = Integer.parseInt(fields[0]);
        this.height     = Integer.parseInt(fields[1]);
        this.g          = Double.parseDouble(fields[2]);
        this.max_thrust = Integer.parseInt(fields[7]);
        this.min_angle  = Integer.parseInt(fields[8]);
        this.max_angle  = Integer.parseInt(fields[9]);
        this.landingX1  = -1;
        this.landingX2  = -1;
        this.landingY   = -1;

        // Calculate max distance (approximation)
        // TODO get actual, it must be a corner
        this.max_dist = 2 * Math.sqrt(this.width*this.width+this.height*this.height);

        // Read terrain
        int numberOfPoints  = Integer.parseInt(levelData[1]);
        this.points = new ArrayList<Point>();
        double lastX = -1;
        double lastY = -1;
        for (int i = 0; i < numberOfPoints; i++) {
            String [] points_ = levelData[i+2].split(" ");
            int x = Integer.parseInt(points_[0]);
            int y = Integer.parseInt(points_[1]);
            this.points.add(new Point(x,y));
            if (lastY != -1 && lastY == y) {
                this.landingX1 = (int)Math.floor(Math.min(x, lastX));
                this.landingX2 = (int)Math.ceil(Math.max(x, lastX));
                this.landingY  = y;
            }
            lastX = x;
            lastY = y;
        }

        // Calculate all distances to landing area
        this.calculateDistances();

        // Read initial lander state
        this.defaultLanderFields = levelData[numberOfPoints+2].split(" ");
    }

    public void calculateDistances() {

        // Find the two points forming the landing area
        Point lp1 = new Point(0,0); // First landing point
        Point lp2 = new Point(0,0); // Second landing point
        int li=0; // Index of first landing point
        for (int i = 1; i < this.points.size(); i++) {
            Point p1 = this.points.get(i-1);
            Point p2 = this.points.get(i);
            if (p1.getY() == p2.getY()) {
                lp1 = p1;
                lp2 = p2;
                li = i-1;
                break;
            }
        }
        lp1.setDistance(0);
        lp2.distance = 0;

        // Propagate distances away from the landing area
        for (int i = li + 2; i < this.points.size(); i++) {
            Point other = this.points.get(i-1);
            this.points.get(i).setDistance(other.distance);
            this.points.get(i).setDistance(this.points.get(i).distance+this.points.get(i).getDistanceTo(other));
        }
        for (int i = li - 1; 0 <= i; i--) {
            Point other = this.points.get(i+1);
            this.points.get(i).setDistance(other.distance);
            this.points.get(i).setDistance(this.points.get(i).distance+this.points.get(i).getDistanceTo(other));
        }

        // Calculate distances until nothing changes anymore
        // TODO
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            for (int i = 0; i < this.points.size(); i++) {
                Point pi = this.points.get(i);
                for (int j = i+1; j < this.points.size(); j++) {
                    Point pj = this.points.get(i);
                }
            }
        }
    }
    public double getDistanceToLandingArea(double x, double y) {
        double bestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < this.points.size(); i++) {
            Point p = this.points.get(i);
            double distance = p.getDistance(x, y) + p.distance;
            if (bestDistance < distance) {
                continue;
            }

            // Check if we are inside the wall?
            // TODO

            // Look for line intersections
            boolean found = false;
            for (int j = 1; j < this.points.size(); j++) {
                if (j == i || j - 1 == i) {
                    continue;
                }
                Point p1 = this.points.get(j-1);
                Point p2 = this.points.get(j);
                Rator collision = Helper.checkLineCollision(x, y, p.x, p.y, p1.x, p1.y, p2.x, p2.y);
                if (collision.onLine1 && collision.onLine2) {
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            bestDistance = distance;
        }
        return bestDistance;
    }
    public String getPolylineString() {
        // Converts an array of pairs to a polyline string
        // Rotates y-axis
        int height = this.height;

        List<String> coords = this.points.stream().map( p -> {
            return Integer.toString(p.getX()) + "," + Integer.toString(height - p.getY());
        }).collect(Collectors.toList());
        return String.join(" ", coords);
    }
    String toPolylineString (List<Point> points) {
        // Same as above
        // TODO unify
        int height = this.height;
        List<String> coords = points.stream().map( p -> {
            return Integer.toString(p.getX()) + "," + Integer.toString(height - p.getY());
        }).collect(Collectors.toList());
        return String.join(" ", coords);
    }
}

