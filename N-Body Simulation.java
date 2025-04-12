import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Nbody {

    /**
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        double big_t = Double.parseDouble(args[0]);
        double delta_t = Double.parseDouble(args[1]);

        String resourceFolder = "resources/";
        String fileName = resourceFolder + args[2];
        FileInputStream fileInputStream = new FileInputStream(fileName);
        System.setIn(fileInputStream);

        // Use StdIn to read from the file.
        int numBodies = StdIn.readInt();
        double universeRadius = StdIn.readDouble();

        // Print out the values read in
        StdOut.println("big_t          = " + big_t);
        StdOut.println("delta_t        = " + delta_t);
        StdOut.println("numBodies      = " + numBodies);
        StdOut.println("universeRadius = " + universeRadius);

        // store vars
        double[] posX = new double[numBodies];
        double[] posY = new double[numBodies];
        double[] velX = new double[numBodies];
        double[] velY = new double[numBodies];
        double[] mass = new double[numBodies];
        String[] pictures = new String[numBodies];

        for (int i = 0; i < numBodies; i++) {
            posX[i] = StdIn.readDouble();
            posY[i] = StdIn.readDouble();
            velX[i] = StdIn.readDouble();
            velY[i] = StdIn.readDouble();
            mass[i] = StdIn.readDouble();
            pictures[i] = StdIn.readString();

        }
        
        StdDraw.setXscale(-universeRadius, universeRadius);
        StdDraw.setYscale(-universeRadius, universeRadius);
        
        new Thread(() -> {StdAudio.play("resources/2001.wav"); 
        }).start();

        double G = 6.67430e-11; // gravitational constant
        for (double t = 0; t < big_t; t += delta_t) { // simulation loop
            StdDraw.picture(0, 0, "resources/starfield.jpg");
            for (int i = 0; i < numBodies; i++) { // loop 
                
                double fx = 0;
                double fy = 0;
               
                for (int j = 0; j < numBodies; j++) {
                    if (i != j) {
                        double dx = posX[j] - posX[i];
                        double dy = posY[j] - posY[i];
                        double r = Math.sqrt(dx * dx + dy * dy);
                        double F = (G * mass[i] * mass[j]) / (r * r);
                        // calculate Fx, calculate Fy
                        fx += F * dx / r;
                        fy += F * dy / r;
                    }
                }

                // calculate ax and ay
                double ax = fx / mass[i];
                double ay = fy / mass[i];
                // calculate bx and by
                double bx = velX[i] + delta_t * ax;
                double by = velY[i] + delta_t * ay;
                // calculate rx and ry
                double rx = posX[i] + delta_t * bx;
                double ry = posY[i] + delta_t * by;

                // readjust position based on radius
                if (rx > universeRadius) {
                    rx = universeRadius;
                } else if (rx < -universeRadius) {
                    rx = -universeRadius;
                }
                if (ry > universeRadius) {
                    ry = universeRadius;
                } else if (ry < -universeRadius) {
                    ry = -universeRadius;
                }

                // update velocities and positions
                velX[i] = bx;
                velY[i] = by;
                posX[i] = rx;
                posY[i] = ry;
            }

            // draw each body according to their new position
            for (int i = 0; i < numBodies; i++) {
                StdDraw.picture(posX[i], posY[i], "resources/" + pictures[i]);
            }
            StdDraw.show(2);

        } // end loop
    } // end main
} // end class
