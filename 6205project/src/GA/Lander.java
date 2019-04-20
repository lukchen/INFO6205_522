package GA;

import java.util.ArrayList;
import java.util.List;
import GA.Point;
import sun.font.TrueTypeFont;

public class Lander implements Comparable<Lander>{

    int timestep;
    ArrayList<double []> points;
    ArrayList<double []> speeds; // List of past speeds for debugging
    List<Command> commands; // List of commands, unbounded
    String color;
    boolean isFlying;
    double score;
    double x;
    double y;
    double xspeed;
    double yspeed;



    int fuel;
    int angle;
    int power;
    double initX;
    double initY;
    double initXSpeed;
    double initYSpeed;
    int initFuel;
    int initAngle;
    int initPower;
    double lastDiff;

    public Lander(String[] fields){
        this.timestep   = 0;
        this.points     = new ArrayList<>(); // List of past points for drawing // TODO point class?
        this.speeds     = new ArrayList<>(); // List of past speeds for debugging
        this.commands   = new ArrayList<>(); // List of commands, unbounded
        this.color      = "black";
        this.isFlying   = true;
        this.score      = -1;
        this.x          = Double.parseDouble(fields[0]);
        this.y          = Double.parseDouble(fields[1]);
        this.xspeed     = Double.parseDouble(fields[2]);
        this.yspeed     = Double.parseDouble(fields[3]);
        this.fuel       = Integer.parseInt(fields[4]);
        this.angle      = Integer.parseInt(fields[5]);
        this.power      = Integer.parseInt(fields[6]);
        this.initX      = this.x;
        this.initY      = this.y;
        this.initXSpeed = this.xspeed;
        this.initYSpeed = this.yspeed;
        this.initFuel   = this.fuel;
        this.initAngle  = this.angle;
        this.initPower  = this.power;
        this.lastDiff   = 0;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Lander comparestu) {
        double comparescore = comparestu.getScore();
        /* For Ascending order*/
        //return this.score-comparescore;

        /* For Descending order */
        if((int) comparescore == (int) this.score) return 0;
        return (int) comparescore>(int) this.score?1:-1;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public double getScore(){
        return this.score;
    }
    public void tick(Level level) {
        if (!this.isFlying) {
            return;
        }
        this.timestep += 1;
        this.points.add(new double[]{this.x, this.y});
        this.speeds.add(new double[]{this.xspeed, this.yspeed});

        // Advance the lander one timestep
        // From https://forum.codingame.com/t/mars-lander-puzzle-discussion/32/129
        if (this.fuel < this.power) {
            this.power = this.fuel;
        }

        this.fuel -= power;

        double arcAngle = -this.angle * Math.PI / 180;
        double xacc = Math.sin(arcAngle) * this.power;
        double yacc = Math.cos(arcAngle) * this.power - level.g;
        this.xspeed += xacc;
        this.yspeed += yacc;
        this.x += this.xspeed - (xacc * 0.5);
        this.y += this.yspeed - (yacc * 0.5);

        // If we left the level we stop
        if (this.x < 0 || level.width <= this.x || this.y < 0 || level.height <= this.y) {
            this.isFlying = false;
            this.calculateScore(level, false);
            return;
        }

        // If we hit terrain we stop
        double lastX = this.points.get(this.points.size()-1)[0];
        double lastY = this.points.get(this.points.size()-1)[1];
        for (int i = 1; i < level.points.size(); i++) {
            Point p1 = level.points.get(i-1);
            Point p2 = level.points.get(i);
            Rator collision = Helper.checkLineCollision(lastX, lastY, this.x, this.y, p1.x, p1.y, p2.x, p2.y);
            if (collision.onLine1 && collision.onLine2) {
                this.isFlying = false;
                this.points.add(new double[]{collision.x, collision.y});
                this.calculateScore(level, p1.getY() == p2.getY());
                return;
            }
        }
    }
    // Score is used to order landers by performance
    public void calculateScore(Level level, boolean hitLandingArea) {
        double currentSpeed = Math.sqrt(Math.pow(this.xspeed, 2) + Math.pow(this.yspeed, 2));

        // 0-100: crashed somewhere, calculate score by distance to landing area
        if (!hitLandingArea) {

            double lastX = this.points.get(this.points.size()-2)[0];
            double lastY = this.points.get(this.points.size()-2)[1];
            double distance = level.getDistanceToLandingArea(lastX, lastY);

            // Calculate score from distance
            this.score = 100 - (100 * distance / level.max_dist);

            // High speeds are bad, they decrease maneuvrability
            double speedPen = 0.1 * Math.max(currentSpeed - 100, 0);
            this.score -= speedPen;
        }

        // 100-200: crashed into landing area, calculate score by speed above safety
        else if (this.yspeed < -40 || 20 < Math.abs(this.xspeed)) {
            int xPen = 0;
            if (20 < Math.abs(this.xspeed)) {
                xPen = (int)(Math.abs(this.xspeed) - 20) / 2;
            }
            int yPen = 0;
            if (this.yspeed < -40) {
                yPen = (int)(-40 - this.yspeed) / 2;
            }
            this.score = 200 - xPen - yPen;
            return;
        }

        // 200-300: landed safely, calculate score by fuel remaining
        else {
            this.score = 200 + (100 * this.fuel / this.initFuel);
        }

        // Set color according to score
        //this.setColor(Helper.rainbow(300 + 300, this.score))
    }
    public void inheritCommands(Lander mom, Lander dad) {

        // Crossover at a random point
        Lander first = mom;
        Lander second = dad;
        if (Math.random() < 0.5) {
            // Randomly choose a first and a second parent
            first = dad;
            second = mom;
        }
        int minTimestep = Math.min(first.timestep, second.timestep);
        int crossoverFrom = (int) Math.floor(minTimestep / 3);
        int crossoverTo = (int) Math.floor(minTimestep * 2 / 3);
        int crossoverIndex = Helper.getRandomInt(crossoverFrom, crossoverTo);
        for (int i = 0; i < crossoverIndex; i++) {
            this.commands.get(i).setAngle(first.commands.get(i).getAngle());
            this.commands.get(i).setPower(first.commands.get(i).getPower());
            //this.commands.set(i, new Command(first.commands.get(i).getAngle(), first.commands.get(i).getPower()));
        }
        for (int i = crossoverIndex; i < second.commands.size(); i++) {
            this.commands.get(i).setAngle(second.commands.get(i).getAngle());
            this.commands.get(i).setPower(second.commands.get(i).getPower());
            //this.commands.set(i, new Command(second.commands.get(i).getAngle(), second.commands.get(i).getPower()));
        }

        // Randomly smoothen command sequences
        if (Math.random() < 0.2) {
            // Angle
            int i = Helper.getRandomInt(0, this.commands.size() - 3);
            int avg = (this.commands.get(i).getAngle() + this.commands.get(i+1).getAngle() + this.commands.get(i+2).getAngle()) / 3;
            this.commands.get(i).setAngle(avg);
            this.commands.get(i+1).setAngle(avg);
            this.commands.get(i+2).setAngle(avg);
        }
        if (Math.random() < 0.2) {
            // Power
            int i = Helper.getRandomInt(0, this.commands.size() - 3);
            double avg = (this.commands.get(i).getPower() + this.commands.get(i+1).getPower() + this.commands.get(i+2).getPower() / 3);
            this.commands.get(i).setPower(avg);
            this.commands.get(i+1).setPower(avg);
            this.commands.get(i+2).setPower(avg);
        }


    }
    public void cloneCommands(Lander other) {
        for (int i = 0; i < this.commands.size(); i++) {
            this.commands.set(i, new Command(other.commands.get(i).getAngle(), other.commands.get(i).getPower()));
        }
        this.score = other.score;
    }
    public void reset() {
        this.points   = new ArrayList<>(); // TODO recycle objects?
        this.speeds   = new ArrayList<>(); // TODO recycle objects?
        this.isFlying = true;
        this.timestep = 0;
        this.x        = this.initX;
        this.y        = this.initY;
        this.xspeed   = this.initXSpeed;
        this.yspeed   = this.initYSpeed;
        this.angle    = this.initAngle;
        this.power    = this.initPower;
        this.fuel     = this.initFuel;
        this.lastDiff = 0;
    }
    public void applyCommand(int t) {
        // Read current command
        int newAngle = this.commands.get(t).getAngle();
        double newPower = this.commands.get(t).getPower();

        // Set angle
        // TODO when almost landed set angle to 0?
        //newAngle = Math.round(newAngle);
        newAngle = Math.max(newAngle, -90);
        newAngle = Math.min(newAngle,  90);
        if (newAngle + 15 < this.angle) {
            this.angle -= 15;
        }
        else if (this.angle + 15 < newAngle) {
            this.angle += 15;
        }
        else {
            this.angle = newAngle;
        }

        // Set power
        newPower += this.lastDiff;
        int roundedPower = (int) Math.round(newPower);
        roundedPower = Math.max(roundedPower, Math.max(0, this.power - 1));
        roundedPower = Math.min(roundedPower, Math.max(4, this.power + 1));
        this.lastDiff = newPower - roundedPower;
        this.power = roundedPower;
    }
    public void printActualCommands() {
        ArrayList result = new ArrayList<Object>();
        this.angle = this.initAngle;
        this.power = this.initPower;
        for (int t = 0; t < this.timestep; t++) {
            this.applyCommand(t);
            result.add(new Command(angle, power));
        }

        System.out.println(result);
    }
    public void createRandomCommands(int count) {
        int angle = this.angle;
        for (int i = 0; i < count; i++) {
            angle += Helper.getRandomInt(-15, 15);
            angle = Math.min(angle,  90);
            angle = Math.max(angle, -90);
            double power =  4.1 * Math.random();
            this.commands.add(new Command(angle, power));
        }
    }

}
