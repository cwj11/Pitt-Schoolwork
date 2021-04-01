public class Player {
	
	public double sing, doub, trip, homerun, steal;
	public boolean stealOnSecond;
	
	public Player(double s, double d, double t, double hr, double st, boolean stS) {
		sing = s;
		doub = d;
		trip = t;
		homerun = hr;
		steal = st;
		stealOnSecond = stS;
	}
}