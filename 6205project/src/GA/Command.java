package GA;

public class Command {
    int angle;
    double power;

    public Command(int angle, double power){
        this.angle = angle;
        this.power = power;
    }

    public int getAngle(){
        return this.angle;
    }
    public double getPower(){
        return this.power;
    }

    public void setAngle(int angle){
        this.angle = angle;
    }
    public void setPower(double power){
        this.power = power;
    }
}
