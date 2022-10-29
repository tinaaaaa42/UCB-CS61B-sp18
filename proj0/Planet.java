public class Planet{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    
    static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV,
		  double yV, double m, String img) {
        
	xxPos = xP;
        yyPos = yP;
	xxVel = xV;
	yyVel = yV;
	mass = m;
	imgFileName = img;
    }

    public Planet(Planet p) {
	
	xxPos = p.xxPos;
	yyPos = p.yyPos;
	xxVel = p.xxVel;
	yyVel = p.yyVel;
	mass = p.mass;
	imgFileName = p.imgFileName;
    }
    
    //* returns the distance between two planets */
    public double calcDistance(Planet p) {
	
        return Math.sqrt((xxPos-p.xxPos)*(xxPos-p.xxPos)+(yyPos-p.yyPos)*(yyPos-p.yyPos));
    }

    //* returns the force exerted on the planet by a given planet */
    public double calcForceExertedBy(Planet p) {
	
	return (G*mass*p.mass/(calcDistance(p)*calcDistance(p)));
    }

    //* returns the force exerted on the planet by a given planet 
    //  in the X direction */
    public double calcForceExertedByX(Planet p) {
	
	double dx = p.xxPos - xxPos;
	return (calcForceExertedBy(p) * dx / calcDistance(p));
    }

    //* returns the force exerted on the planet by a given planet
    //  in the Y direction */
    public double calcForceExertedByY(Planet p) {
	
	double dy = p.yyPos - yyPos;
	return (calcForceExertedBy(p) * dy / calcDistance(p));
    }

    //* returns the Net X force exerted by all the planets in the array*/
    public double calcNetForceExertedByX(Planet[] allPlanets) {

	double sumX = 0;
	for (int i=0;i<allPlanets.length;i+=1) {

	    if (this.equals(allPlanets[i])) {
		continue;
	    }
	    else {
		sumX += calcForceExertedByX(allPlanets[i]);
	    }

	}
	return sumX;
    }

    //* returns the Net Y force exerted by all the planets in the array /*
    public double calcNetForceExertedByY(Planet[] allPlanets) {
	
	double sumY = 0;
	for (int i=0;i<allPlanets.length;i+=1) {
	    
	    if (this.equals(allPlanets[i])) {
		continue;
	    }
	    else {
		sumY += calcForceExertedByY(allPlanets[i]);
	    }

	}
	return sumY;
    }

    //* change the velocity and position of the planet*/
    public void update(double dt, double fX, double fY) {

	double a_x = fX / mass;
	double a_y = fY / mass;

	xxVel = xxVel + a_x * dt;
	yyVel = yyVel + a_y * dt;

	xxPos = xxPos + xxVel * dt;
	yyPos = yyPos + yyVel * dt;

    }
}
