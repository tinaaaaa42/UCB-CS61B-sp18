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
}
