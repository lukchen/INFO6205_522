package test;

import GA.Helper;
import GA.Lander;
import GA.Rator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LanderTest {


    @Test
    public void compreToTest(){
        String [] fields={"100","200","10.5","11.5","20","15","60"};
        Lander lander=new Lander(fields);

        lander.setScore(10);
        String [] fields2={"10","20","1.5","1.15","2","1.5","6"};
        Lander lander2=new Lander(fields);

        lander2.setScore(5);
        assertEquals(-1,lander.compareTo(lander2));
    }

}
