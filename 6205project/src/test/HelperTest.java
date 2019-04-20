package test;

import GA.Helper;
import GA.Rator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperTest {

    @Test
    public void checkCollisionTest(){
        Rator ratortest=new Rator();
        ratortest.setX(100);
        ratortest.setY(200);
        ratortest.setOnLine1(true);
        ratortest.setOnLine2(false);
        Rator rator1= Helper.checkLineCollision(100,200,300,400,100,200,300,400);

        assertEquals(ratortest.isOnLine2(),rator1.isOnLine2());

    }
}
