package tournament;

import games.MatrixGame;
import games.MixedStrategy;
import games.OutcomeIterator;

/**
 * Maxmin Payoff agent that always picks the choice that will lead to the
 * maximum minimum Payoff
 * 
 * @author Marisol Chacon
 * @version 2019.30.04
 */
public class MaxMin extends Player {
	protected final String newName = "MaxMin Payoff"; // Overwrite this variable in your player subclass

	/** constructor **/
	public MaxMin() {
		super();
		playerName = newName;
	}

	/**
	 * THIS METHOD SHOULD BE OVERRIDDEN GameMaster will call this to compute your
	 * strategy.
	 * 
	 * @param mg           The game your agent will be playing
	 * @param playerNumber Row Player = 1, Column Player = 2
	 */
	protected MixedStrategy solveGame(MatrixGame mg, int playerNumber) {
		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));

		double[] temp = new double[mg.getNumProfiles()];
		double[] payoff = new double[mg.getNumProfiles()];

		int[] row = new int[mg.getNumProfiles()];
		int[] column = new int[mg.getNumProfiles()];
		OutcomeIterator itr = new OutcomeIterator(mg);

		// get payoff for each choice
		int i = 1;
		int j = 0;
		while (itr.hasNext()) {
			int[] o = itr.next();
			row[j] = o[i - 1];
			column[j] = o[i];
			temp = mg.getPayoffs(o);
			payoff[j] = temp[i];
			j++;
		}

		double[] rowPayoff = new double[row.length];

		// get maxPayoff for each row
		for (int m = 0; m < payoff.length; m++) {
			if (rowPayoff[row[m]] < payoff[m]) {
				rowPayoff[row[m]] = payoff[m];
			}
		}

		// select max Payoff
		double minmaxPayoff = 0;
		double temp2 = 0;
		int maxRow = 0;
		for (int n = 0; n < rowPayoff.length; n++) {
			temp2 = rowPayoff[n];
			if (temp2 > minmaxPayoff) {
				minmaxPayoff = temp2;
				maxRow = n;
			}
		}

		// set maxPayoff to be the choice, and all other choices zero
		ms.setProb(maxRow, 1.0);
		for (int a = 0; a <= mg.getNumActions(playerNumber); a++)
			if (maxRow == a) {
			} else
				ms.setProb(a, 0);

		return ms;
	}

}
