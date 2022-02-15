/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boids;

/**
 *
 * @author seanjhardy
 */
import net.jafama.FastMath;
public class Boids {

    private static GUIManager frame;
    public static double SeperationWeight = 4.0/8.0;
    public static double AlignmentWeight = 3.0/8.0;
    public static double CohestionWeight = 1.0/8.0;
    public static double collisionWeight = 30.0;
    public static double directionWeight = 1.0;
    
    public static void main(String[] args){
        //creates a new, non static instance of the tourno class
        Boids tourno = new Boids();
    }
    public Boids(){
        frame = new GUIManager();
    }
    
    public static double distance(Boid boidA, Boid boidB){
        return FastMath.hypot(boidA.y - boidB.y,boidA.x - boidB.x);
    }
    
    public static double angle(Boid boidA, Boid boidB) {
        double dx = boidB.x - boidA.x;
        double dy = -(boidB.y - boidA.y);
        double inRads = FastMath.atan2(dy, dx);
        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
        if (inRads < 0)
            inRads = FastMath.abs(inRads);
        else
            inRads = 2 * FastMath.PI - inRads;
        return FastMath.toDegrees(inRads);
    }
    
    public static double length(double x,double y){
        return FastMath.sqrt(x*x + y*y);
    }

    public static double[] normalise(double[] point){
        double[] newPoint = point;
        double length = length(newPoint[0],newPoint[1]);
        if(length > 0){
            newPoint[0] /= length;
            newPoint[1] /= length;
        }
        return newPoint;
    }
}
