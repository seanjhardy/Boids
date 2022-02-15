/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boids;

import static boids.Boids.*;
import java.awt.Color;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import net.jafama.FastMath;
/**
 *
 * @author seanjhardy
 */
public class Boid{
    
    public double x,y, xVel,yVel;
    public double viewAngle, viewRadius, speed, size;
    public double direction;
    
    public Boid(int x, int y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.viewRadius = 200;
        this.speed = 10;
        this.size = 10;
        this.viewAngle = 100;
    }
    public void calculateMovement(double[] A, double[] B, double[] C, double[] D){
        int d = 1;
        if(D[2] < 10){
            A[0] = 0;A[1] = 0;
            B[0] = 0;B[1] = 0;
            C[0] = 0;C[1] = 0;
            d = 0;
        }
        double velX = A[0]*SeperationWeight + B[0]*AlignmentWeight + C[0]*CohestionWeight + D[0]*collisionWeight + FastMath.cos(FastMath.toRadians(direction))*directionWeight*d;
        double velY = A[1]*SeperationWeight + B[1]*AlignmentWeight + C[1]*CohestionWeight + D[1]*collisionWeight + FastMath.sin(FastMath.toRadians(direction))*directionWeight*d;
        double[] velocity = {velX, velY};
        velocity = normalise(velocity);
        direction = FastMath.toDegrees(FastMath.atan2(velocity[1],velocity[0]));
        xVel = FastMath.cos(FastMath.toRadians(direction));
        yVel = FastMath.sin(FastMath.toRadians(direction));
        x += xVel*speed;
        y += yVel*speed;
        if(x > 1920){
            x  = 0;
        }if(x < 0){
            x = 1920;
        }
        if(y > 1080){
            y = 0;
        }if(y < 0){
            y = 1080;
        }
    }
    
    public double[] calculateSeperation(Graphics g, ArrayList<Boid> boids){
        double[] point = new double[2];
        int numBoids = 0;
        double distance;
        for(Boid boid: boids){
            if(boid != this){
                distance = distance(this, boid);
                if(distance < 50){
                    point[0] += boid.x-this.x;
                    point[1] += boid.y-this.y;
                    numBoids++;
                }
            }
        }
        if(numBoids > 0){
            point[0] *= -1;
            point[1] *= -1;
            point[0] /= numBoids;
            point[1] /= numBoids;
            point = normalise(point);
            return point;
        }else{
            return point;
        }
    }
    public double[] calculateAlignment(Graphics g, ArrayList<Boid> boids){
        double[] point = new double[2];
        int numBoids = 0;
        double distance;
        for(Boid boid: boids){
            if(boid != this){
                distance = distance(this, boid);
                if(distance < viewRadius){
                    point[0] += boid.xVel;
                    point[1] += boid.yVel;
                    numBoids++;
                }
            }
        }
        if(numBoids > 0){
            point[0] /= numBoids;
            point[1] /= numBoids;
            point = normalise(point);
            return point;
        }else{
            return point;
        }
    }
    public double[] calculateCohesion(Graphics g, ArrayList<Boid> boids){
        double[] point = new double[2];
        int numBoids = 0;
        double distance;
        for(Boid boid: boids){
            if(boid != this){
                distance = distance(this, boid);
                if(distance < viewRadius){
                    point[0] += boid.x;
                    point[1] += boid.y;
                    numBoids++;
                }
            }
        }
        if(numBoids > 0){
            point[0] /= numBoids;
            point[1] /= numBoids;
            point[0] -= x;
            point[1] -= y;
            point = normalise(point);
            return point;
        }else{
            return point;
        }
    }
    public double[] collisionDetection(Graphics g){
        int distance, totalAngles = 0;
        double averageAngle;
        double xComp = 0, yComp = 0;
        double minDistance = 10000;
        double optimalAngle = direction, furthestDistance = 0;
        for(int a = 0; FastMath.abs(a) < 120; a = (int)(FastMath.pow(-1,totalAngles)*(FastMath.abs(a)+2))){
            Ray ray = new Ray(direction + a,(int)x,(int)y, viewRadius); 
            distance = ray.findIntersection(g);
            if(distance < minDistance){
                minDistance = distance;
            }
            /*0if(distance > furthestDistance){
                optimalAngle = direction+a;
                furthestDistance = distance;
                xComp = FastMath.cos(FastMath.toRadians(optimalAngle))*distance;
                yComp = FastMath.sin(FastMath.toRadians(optimalAngle))*distance;
            }*/
            xComp += FastMath.cos(FastMath.toRadians(direction + a))*((distance)/viewRadius);
            yComp += FastMath.sin(FastMath.toRadians(direction + a))*((distance)/viewRadius);
            totalAngles++;
        }
        averageAngle = FastMath.toDegrees(FastMath.atan2(yComp, xComp));
        xComp /= totalAngles;
        yComp /= totalAngles;
        g.setColor(new Color(0,255,0,100));
        g.drawLine((int)x, (int)y, (int)(x+FastMath.cos(FastMath.toRadians(averageAngle))*50), (int)(y+FastMath.sin(FastMath.toRadians(averageAngle))*50));
        double[] vel = {xComp, yComp, minDistance};
        return vel;
    }
    
    public void draw(Graphics g){
        int[] xPoints = {(int)this.x + (int)(FastMath.cos(FastMath.toRadians(direction+90))*size),
                        (int)this.x + (int)(FastMath.cos(FastMath.toRadians(direction))*(2*size)),
                        (int)this.x + (int)(FastMath.cos(FastMath.toRadians(direction-90))*size)};
        int[] yPoints = {(int)this.y + (int)(FastMath.sin(FastMath.toRadians(direction+90))*size),
                        (int)this.y + (int)(FastMath.sin(FastMath.toRadians(direction))*(2*size)),
                        (int)this.y + (int)(FastMath.sin(FastMath.toRadians(direction-90))*size)};
        
        //g.setColor(new Color(255,0,0,5));
        //g.fillArc((int)(x-viewRadius),(int)(y-viewRadius),(int)(viewRadius*2),(int)viewRadius*2, (int)(-direction-viewAngle),(int)(viewAngle*2.0));
        g.setColor(RED);
        g.drawLine((int)(x+FastMath.cos(FastMath.toRadians(direction))*size*2),
                (int)(y+FastMath.sin(FastMath.toRadians(direction))*size*2),
                (int)(x+FastMath.cos(FastMath.toRadians(direction))*(size*2+50)),
                (int)(y+FastMath.sin(FastMath.toRadians(direction))*(size*2+50)));
        g.drawPolygon(xPoints,yPoints,3);
    }

    
    public void simulate(Graphics g, ArrayList<Boid> boids){
        double[] A = calculateSeperation(g, boids);
        double[] B = calculateAlignment(g, boids);
        double[] C = calculateCohesion(g, boids);
        double[] D =  collisionDetection(g);
        calculateMovement(A,B,C,D);
        draw(g);
    }
}
