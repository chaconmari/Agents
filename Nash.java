package tournament;

import games.MatrixGame;
import games.MixedStrategy;
import games.OutcomeIterator;

/**
 * Pure Nash agent that tries to find best solution using Pure Nash equilibrium
 * 
 * @author Marisol Chacon
 * @version 2019.30.04
 */
public class Nash extends Player {
	protected final String newName = "Pure Nash"; // Overwrite this variable in your player subclass

	/** Your constructor should look just like this */
	public Nash() {
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
		double[] value = new double[mg.getNumProfiles()];

		int[] row = new int[mg.getNumProfiles()];
		int[] column = new int[mg.getNumProfiles()];
		OutcomeIterator itr = new OutcomeIterator(mg);

		// getPayoffs for each outcome
		int i = 1;
		int j = 0;
		while (itr.hasNext()) {
			int[] o = itr.next();
			row[j] = o[i - 1];
			column[j] = o[i];
			temp = mg.getPayoffs(o);
			value[j] = temp[i];
			j++;
		}

		double[][] payoffTable = new double[row.length][column.length];

		// create Payoff Table
		for (int m = 0; m < row.length; m++) {
			for (int n = 0; n < column.length; n++) {
				payoffTable[row[n] - 1][column[m] - 1] = value[n];
			}
		}

		double[] x = new double[row.length];
		for (int a = 0; i < row.length; i++) {
			x[a] = row[a];
		}

		double[] y = new double[row.length];
		for (int a = 0; i < row.length; i++) {
			y[a] = column[a];
		}

		// find agent's best pure strategy
		double opt1 = Double.NEGATIVE_INFINITY;
		int maxRow = 0;

		int n = payoffTable[1].length;
		for (int z = 0; z < row.length; z++) {
			double sum = 0.0;
			for (int b = 0; b < n; b++) {
				sum += payoffTable[z][b] * x[b];
			}
			if (sum > opt1) {
				opt1 = sum;
				maxRow = z;
			}
		}

		// change probablity for the lowest regret, all others are 0
		ms.setProb(maxRow + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber); a++)
			if (maxRow + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

}
