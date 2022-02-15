/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boids;

import java.awt.Color;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author seanjhardy
 */
public class SimulationPanel extends JPanel{
    private final JFrame parent;
    private static ArrayList<Boid> boids = new ArrayList<>();
    public static ArrayList<int[]> objects = new ArrayList<>();
    private static Random random = new Random();
    public static boolean drawRay = true;
    public int sleepTime = 50;
    
    public SimulationPanel(JFrame parent){
        this.parent = parent;
        createObjects();
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(16, 29, 48));
        g.fillRect(0,0,1920,1080);
        long startTime = System.currentTimeMillis();
        for(Boid boid: boids){
            boid.simulate(g, boids);
        }
        g.setColor(WHITE);
        for(int[] line : objects){
            g.drawLine(line[0],line[1],line[2],line[3]);
        }
        long endTime = System.currentTimeMillis();
        if(endTime < startTime + sleepTime){
            long diff = endTime - startTime;
            try {
                Thread.sleep(sleepTime-diff);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        revalidate();
        repaint();
    }
    
    public void createObjects(){
        for(int i = 0; i < 100; i++){
            boids.add(new Boid(random.nextInt(1920), random.nextInt(1080), random.nextInt(360)));
        }
        /*
        int[] line1 = {80,80,1860,80};
        objects.add(line1);
        int[] line2 = {80,80,80,1000};
        objects.add(line2);
        int[] line3 = {1860,80,1860,1080};
        objects.add(line3);
        int[] line4 = {80,1000,1860,1000};
        objects.add(line4);*/
        
        for(int obj = 0; obj < 1; obj++){
            if(random.nextBoolean()){
                int x = random.nextInt(1920);
                int y = random.nextInt(1080);
                int length = random.nextInt(500);
                int[] randomLine = {x,y,x, y+length};
                objects.add(randomLine);
            }else{
                int y = random.nextInt(1080);
                int x = random.nextInt(1920);
                int length = random.nextInt(500);
                int[] randomLine = {x,y,x+length,y};
                objects.add(randomLine);
            }
        }
    }
}
