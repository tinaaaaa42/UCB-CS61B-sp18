public class NBody {

    //* returns the radius in the given file */
    public static double readRadius(String file) {
	In in = new In(file);
	in.readInt();
	return in.readDouble();
    }

    //* returns an array of Planets in the file */
    public static Planet[] readPlanets(String file) {

	In in = new In(file);
	int num = in.readInt();
	in.readDouble();

	Planet[] resPlanets = new Planet[num];
	for (int i=0;i<num;i+=1) {
	    double xP = in.readDouble();
	    double yP = in.readDouble();
	    double xV = in.readDouble();
	    double yV = in.readDouble();
	    double m = in.readDouble();
	    String img = in.readString();

	    resPlanets[i] = new Planet(xP, yP, xV, yV, m, img);
	}

	return resPlanets;
    }

    public static void main(String[] args) {

	double T = Double.parseDouble(args[0]);
	double dt = Double.parseDouble(args[1]);
	String fileName = args[2];

	double radius = readRadius(fileName);
	Planet[] allPlanets = readPlanets(fileName);

	// set the background
	StdDraw.setScale(-radius, radius);
	StdDraw.clear();
	StdDraw.picture(0, 0, "images/starfield.jpg");

	// draw all planets
	for (int i=0;i<allPlanets.length;i+=1) {

	    allPlanets[i].draw();
	}

	StdDraw.show();

	// animation
	StdDraw.enableDoubleBuffering();
	double timer;

	for (timer=0;timer<T;timer+=dt) {

	double[] xForces = new double[allPlanets.length];
	double[] yForces = new double[allPlanets.length];
	

	for (int i=0;i<allPlanets.length;i+=1) {

	    xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
	    yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
	}

	// update
	for (int i=0;i<allPlanets.length;i+=1) {
	
	    allPlanets[i].update(dt, xForces[i], yForces[i]);
	}

	// redraw the background
	StdDraw.picture(0, 0, "images/starfield.jpg");

	// draw all the planets
	for (int i=0;i<allPlanets.length;i+=1) {

	    allPlanets[i].draw();
	}

	StdDraw.show();
	StdDraw.pause(10);
	}

	// print the final state of the universe
	StdOut.printf("%d\n", allPlanets.length);
	StdOut.printf("%.2e\n", radius);
	for (int i=0;i<allPlanets.length;i+=1) {

	    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
			  allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
			  allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);
	}
    }
}
