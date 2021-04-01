

public class BaseballSim {

	static Player[] bases = new Player[3];
	static int runs = 0;
	static double steal;


	public static void main(String[] args) {
		int caraSing = 0;
		int caraDoub = 2;
		int caraTrip = 6;
		int caraHR = 1;
		int caraAB = 34;
		double caraSteal = 0.5;
		boolean caraStealOnSecond = false;


		int gillSing = 0;
		int gillDoub = 1;
		int gillTrip = 6;
		int gillHR = 1;
		int gillAB = 35;
		double gillSteal = 0.5;
		boolean gillStealOnSecond = false;

		int connorSing = 0;
		int connorDoub = 1;
		int connorTrip = 2;
		int connorHR = 10;
		int connorAB = 35;
		double connorSteal = 0.5;
		boolean connorStealOnSecond = false;

		int jamieSing = 0;
		int jamieDoub = 0;
		int jamieTrip = 7;
		int jamieHR = 2;
		int jamieAB = 35;
		double jamieSteal = 0.5;
		boolean jamieStealOnSecond = false;


		int kaleySing = 3;
		int kaleyDoub = 3;
		int kaleyTrip = 2;
		int kaleyHR = 2;
		int kaleyAB = 36;
		double kaleySteal = 0.5;
		boolean kaleyStealOnSecond = false;

		int robSing = 0;
		int robDoub = 3;
		int robTrip = 5;
		int robHR = 4;
		int robAB = 34;
		double robSteal = 0.5;
		boolean robStealOnSecond = false;

		int justinSing = 0;
		int justinDoub = 1;
		int justinTrip = 1;
		int justinHR = 1;
		int justinAB = 11;
		double justinSteal = 0.5;
		boolean justinStealOnSecond = false;


		int samSing = 0;
		int samDoub = 0;
		int samTrip = 7;
		int samHR = 10;
		int samAB = 37;
		double samSteal = 0.5;
		boolean samStealOnSecond = false;

		int aliSing = 1;
		int aliDoub = 5;
		int aliTrip = 3;
		int aliHR = 1;
		int aliAB = 38;
		double aliSteal = 0.5;
		boolean aliStealOnSecond = false;

		Player cara = new Player((double)caraSing/caraAB, (double)caraDoub/caraAB, (double)caraTrip/caraAB, (double)caraHR/caraAB, caraSteal, caraStealOnSecond);
		Player gill = new Player((double)gillSing/gillAB, (double)gillDoub/gillAB, (double)gillTrip/gillAB, (double)gillHR/gillAB, gillSteal, gillStealOnSecond);
		Player connor = new Player((double)connorSing/connorAB, (double)connorDoub/connorAB, (double)connorTrip/connorAB, (double)connorHR/connorAB, connorSteal, connorStealOnSecond);

		Player jamie = new Player((double)jamieSing/jamieAB, (double)jamieDoub/jamieAB, (double)jamieTrip/jamieAB, (double)jamieHR/jamieAB, jamieSteal, jamieStealOnSecond);
		Player kaley = new Player((double)kaleySing/kaleyAB, (double)kaleyDoub/kaleyAB, (double)kaleyTrip/kaleyAB, (double)kaleyHR/kaleyAB, kaleySteal, kaleyStealOnSecond);
		Player rob = new Player((double)robSing/robAB, (double)robDoub/robAB, (double)robTrip/robAB, (double)robHR/robAB, robSteal, robStealOnSecond);

		Player justin = new Player((double)justinSing/justinAB, (double)justinDoub/justinAB, (double)justinTrip/justinAB, (double)justinHR/justinAB, justinSteal, justinStealOnSecond);
		Player sam = new Player((double)samSing/samAB, (double)samDoub/samAB, (double)samTrip/samAB, (double)samHR/samAB, samSteal, samStealOnSecond);
		Player ali = new Player((double)aliSing/aliAB, (double)aliDoub/aliAB, (double)aliTrip/aliAB, (double)aliHR/aliAB, aliSteal, aliStealOnSecond);

		int sum = 0;
		Player[] awaylineup = {sam, justin, ali};
		Player[] homelineup = {connor, gill, cara};
		int awaywins = 0, homewins = 0, tempHomeWins = 0, tempAwayWins = 0;
		for(int a = 0; a < 100000; a++) {
			while(tempAwayWins < 3 && tempHomeWins < 3) {
				if(gameSimulator(awaylineup, homelineup) > 0)
					tempAwayWins++;
				else {
					tempHomeWins++;
				}
			}
			if(tempAwayWins > 2)
				awaywins++;
			else {
				homewins++;
			}
			tempAwayWins = 0;
			tempHomeWins = 0; 
			sum += gameSimulator(awaylineup, homelineup);
		}
		System.out.println((double)awaywins/100000);
		System.out.println((double)homewins/100000);
		System.out.println((double)sum/100000);
	}



	public static void hitSingle(Player p) {
		if(bases[2] != null)
			runs++;
		bases[2] = bases[1];
		bases[1] = bases[0];
		bases[0] = p;
	}

	public static void hitDouble(Player p) {
		if(bases[2] != null)
			runs++;
		if(bases[1] != null)
			runs++;
		bases[2] = bases[1];
		bases[1] = p;
		bases[0] = null;
	}

	public static void hitTriple(Player p) {
		for(int i = 0; i < 3; i++) {
			if(bases[i] != null)
				runs++;
			bases[i] = null;
		}
		bases[2] = p;
	}

	public static void hitHomerun(Player p) {
		for(int i = 0; i < 3; i++) {
			if(bases[i] != null)
				runs++;
			bases[i] = null;
		}
		runs++;
	}

	public static int runCalculator(Player[] lineup, int numOuts) {
		runs = 0;
		int outs = 0;
		int lineupNum = 0;
		while(outs < numOuts) {
			double rand = Math.random();
			if(rand < lineup[lineupNum].sing) {
				hitSingle(lineup[lineupNum]);
			} else if(rand < lineup[lineupNum].sing + lineup[lineupNum].doub) {
				hitDouble(lineup[lineupNum]);
			} else if(rand < lineup[lineupNum].sing + lineup[lineupNum].doub + lineup[lineupNum].trip) {
				hitTriple(lineup[lineupNum]);
			} else if(rand < lineup[lineupNum].sing + lineup[lineupNum].doub + lineup[lineupNum].trip + lineup[lineupNum].homerun) {
				hitHomerun(lineup[lineupNum]);
			} else {
				outs++;
				if(outs%3 == 0)
					for(int i = 0; i < 3; i++) {
						bases[i] = null;
					}
			}
			lineupNum++;
			lineupNum%=lineup.length;
			while(((bases[1] != null && bases[1].stealOnSecond) || bases[2] != null) && outs%3==2) {
				if(bases[2] != null) {
					rand = Math.random();
					if(rand < bases[2].steal) {
						runs++;
						bases[2] = null;
					} else {
						outs++;
						bases[2] = null;
						break;
					}
				}
				if(bases[1] != null && bases[1].stealOnSecond) {
					rand = Math.random();
					if(rand < bases[1].steal) {
						bases[2] = bases[1];
						bases[1] = null;

					} else {
						outs++;
						bases[1] = null;
						break;
					}
				}
			}

		}
		return runs;
	}


	public static int gameSimulator(Player[] lineup1, Player[] lineup2) {
		int inning = 0;
		int runs1 = 0, runs2 = 0;
		while(inning < 9 || runs1 == runs2) {
			runs1 += runCalculator(lineup1, 3);
			runs2 += runCalculator(lineup2, 3);
			inning++;
		}
		return runs1-runs2;
	}
}

