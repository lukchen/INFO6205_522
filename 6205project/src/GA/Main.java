package GA;

import java.awt.*;
import java.awt.Canvas;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import GA.myCanvas;

import javax.swing.JFrame;

public class Main extends JFrame{

    public static void main(String[] args) throws IOException {
        // Test all the things
        //Test.run();

        int NUMBER_OF_LANDERS = 200;
        int NUMBER_OF_POPULATIONS = 5;
        double MIX_POPULATION_CHANCE = 0.01;
        int REPRODUCING_LANDERS = 5;  // Per population
        int MAX_TIMESTEP = 300;

        int LANDERS_IN_POPULATION = Math.toIntExact(Math.round(NUMBER_OF_LANDERS / NUMBER_OF_POPULATIONS));

        BufferedWriter br = new BufferedWriter(new FileWriter("myfile.csv"));
        StringBuilder sb = new StringBuilder();

        List<List<List<Point>>> finalData = new ArrayList<>();

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


        // Load and draw first level
        String firstlevelname = "Stalagtite";
        String[] firstleveldata = leveldata;
        Level level = new Level(firstleveldata);
        //level.drawTerrain();
        int times = 1;  // In our test we run 1000 * 1000 times to get enough generations, since the canvas shows up at the end of program, here set 1 for TA to see the Canvas
        Lander bestLander = null;

        for(int N = 0; N<times; N++) {
            // Anchor condition
            if (times <= 0) {
                System.out.println(bestLander);
                if (bestLander != null) {
                    bestLander.printActualCommands();
                }
                return;
            }
            times -= 1;

            // Create initial random landers
            if (level.landers.size() == 0) {
                for (int i = 0; i < NUMBER_OF_LANDERS; i++) {
                    Lander lander = new Lander(level.defaultLanderFields);
                    lander.createRandomCommands(MAX_TIMESTEP);
                    level.landers.add(lander);
                }
            }

            // or evolve existing landers
            else {
                // Evolve each population independently
                for (int p = 0; p < NUMBER_OF_POPULATIONS; p++) {
                    for (int i = REPRODUCING_LANDERS; i < LANDERS_IN_POPULATION; i++) {
                        // Replace each low-value lander with a combination of two high-value landers
                        int combinationCount = REPRODUCING_LANDERS * (REPRODUCING_LANDERS - 1);
                        int combination = (i - REPRODUCING_LANDERS) % combinationCount;
                        int momIndex = (int) Math.floor(combination / (REPRODUCING_LANDERS - 1));  // The "row"
                        int dadIndex = combination % (REPRODUCING_LANDERS - 1);  // The "col"
                        if (momIndex <= dadIndex) {
                            // Indexes have to be different
                            dadIndex += 1;
                        }
                        int populationOffset = p * LANDERS_IN_POPULATION;
                        int childIndex = populationOffset+i;
                        int a = 0;
                        level.landers.get(populationOffset + i).inheritCommands(
                                level.landers.get(populationOffset + momIndex),
                                level.landers.get(populationOffset + dadIndex)
                        );
                    }
                }


                // Reset the state of all landers
                for (int i = 0; i < NUMBER_OF_LANDERS; i++) {
                    level.landers.get(i).reset();
                }
            }

            // Fly you fools
            for (int i = 0; i < NUMBER_OF_LANDERS; i++) {
                Lander lander = level.landers.get(i);
                for (int t = 0; t < MAX_TIMESTEP; t++) {
                    lander.applyCommand(t);
                    lander.tick(level);
                }
                // Lander did not touch terrain
                if (lander.score == -1) {
                    lander.calculateScore(level, false);
                }
            }

            // Sort landers by population and score
            ArrayList<ArrayList<Lander>> populations = new ArrayList<ArrayList<Lander>>();
            for (int p = 0; p < NUMBER_OF_POPULATIONS; p++) {
                ArrayList<Lander> newone = new ArrayList<Lander>(level.landers.subList(p * LANDERS_IN_POPULATION, (p + 1) * LANDERS_IN_POPULATION));
                Collections.sort(newone);
                populations.add(newone);
            }
            Collections.sort(level.landers);
            bestLander = level.landers.get(0);

            // Update screen
            if (times % 4 == 0) {
                //level.drawLanders();

                List<List<Point>> twoHundredLanders = new ArrayList<>();
                for (int i = level.landers.size()-1; 0 <= i; i--) {
                    List<Point> eachPoints = new ArrayList<>();
                    for(int k=0; k<level.landers.get(i).points.size(); k++){
                        //System.out.println(" X: "+level.landers.get(i).points.get(k)[0]+" Y: "+level.landers.get(i).points.get(k)[1]);
                        // Append strings from array
                        eachPoints.add(new Point((int)level.landers.get(i).points.get(k)[0], (int)level.landers.get(i).points.get(k)[1]));
                    }
                    twoHundredLanders.add(eachPoints);
                    eachPoints = null;
                }
                finalData.add(twoHundredLanders);
                twoHundredLanders = null;
                System.out.println("Best score: " + bestLander.getScore());
                if (bestLander.timestep == MAX_TIMESTEP) {
                    System.out.println("MAX_TIMESTEP reached, maybe increase?");
                }
            }

        }

        Frame f= new Frame("Canvas");
        f.add(new myCanvas(finalData));
        f.setLayout(null);
        f.setSize(1920, 1080);
        f.setVisible(true);
    }
}

