package test;

import GA.Lander;
import GA.Level;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelTest {


    @Test
    public void getDistanceToLandingAreaTest()
    {
//        7000 3000 3.711 1.0 1.0 1 0 4 -90 90
        String[] leveldata = {
                "7000 3000 3.711 1.0 1.0 1 0 4 -90 90",
                "15",
                "0 2500", "100 200", "500 150",
                "1000 2000", "2000 2000", // Landing area
                "2010 1500", "2200 800", "2500 200",
                "6899 300", "6999 2500", "4100 2600",
                "4200 1000", "3500 800", "3100 1100", // Stalagtite
                "3400 2900",

                // Lander config
                "6500 1300 0 50 1750 0 0"
        };
        String [] fields={"100","20","10","11.5","20","15","60"};
        //Lander lander=new Lander(fields);

      //  String [] fields={"7000","3000","3.711","1.0","1.0","1.0","4","-90","90"};
        Level level=new Level(leveldata);

        double dist=level.getDistanceToLandingArea(200,100);

        assertEquals(2220,dist,.9);

    }
}
