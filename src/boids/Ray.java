/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boids;

import static boids.SimulationPanel.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Objects;
import net.jafama.FastMath;
/**
 *
 * @author seanjhardy
 */
public class Ray {
    
    public double x1,y1,x2,y2, angle, viewSize;
    public int[] minPoint = new int[2];
    public int minDistance = -1;
    
    public Ray(double angle, int x, int y, double viewSize){
        this.x1 = x;
        this.y1 = y;
        this.angle = angle;
        this.x2 = x + (FastMath.cos(FastMath.toRadians(this.angle)) * viewSize);
        this.y2 = y + (FastMath.sin(FastMath.toRadians(this.angle)) * viewSize);
        this.viewSize = viewSize;
    }
    
    public int findIntersection(Graphics g) {
        for(int[] line: objects){
            double a1 = line[3] - line[1];
            double b1 = line[0] - line[2];
            double c1 = a1 * line[0] + b1 * line[1];

            double a2 = y2 - y1;
            double b2 = x1 - x2;
            double c2 = a2 * x1 + b2 * y1;

            double delta = a1 * b2 - a2 * b1;
            int[] point = {(int)((b2 * c1 - b1 * c2) / delta), (int)((a1 * c2 - a2 * c1) / delta)};
            if(Objects.equals(line[0], line[2]) ? (comparison(point[1],line[3],line[1])) : (comparison(point[0],line[2],line[0]))){
                if(Objects.equals(x1,x2) ? (comparison(point[1],y1,y2)) : (comparison(point[0],x2,x1))){
                    calcDistance(point);
                }
            }
        }
        if(this.minDistance != -1){
            //g.setColor(new Color(255,0,0,(int)(255-FastMath.min(((double)minDistance/500)*255,255))));
            //g.drawLine((int)x1,(int)y1,this.minPoint[0],this.minPoint[1]);
            return minDistance;
        }
        return (int) viewSize;
    }
   
    public boolean comparison(double x, double x1, double x2){
        return (x < FastMath.max(x1,x2) && x > FastMath.min(x1,x2));
    }
    
    public void calcDistance(int[] point){
        int d = (int) FastMath.hypot(point[1]-y1,point[0]-x1);
        if(d < this.minDistance || minDistance == -1){
            this.minDistance = d;
            this.minPoint[0] = point[0];
            this.minPoint[1] = point[1];
        } 
    }
}
