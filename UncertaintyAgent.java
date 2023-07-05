package tournament;

import games.MatrixGame;
import games.MixedStrategy;
import games.OutcomeIterator;

/**
 * Agent that allows the user to enter a strategy
 * 
 * @author Marisol Chacon
 * @version 2019.05.11
 */

public class UncertaintyAgent extends Player {
	protected final String newName = "Uncertainty Agent"; // Overwrite this variable in your player subclass

	/** Your constructor should look just like this */
	public UncertaintyAgent() {
		super();
		playerName = newName;
	}

	/**
	 * Initialize is called at beginning of tournament. Use this time to decide on a
	 * strategy depending on the parameters.
	 */
	public void initialize() {
		// System.out.println("Manual Override "+param.getDescription());
	}

	/**
	 * Asks the user for probabilities to enter via console
	 * 
	 * @param mg           The game your agent will be playing
	 * @param playerNumber Row Player = 0, Column Player = 1
	 */
	protected MixedStrategy solveGame(MatrixGame mg, int playerNumber) {

		/*
		 * if (playerNumber == 0) System.out.println("You are row player"); else
		 * System.out.println("You are column player"); mg.printMatrix();
		 */

		double outcomeUncertainty = param.getOutcomeUncertainty();
		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));
		boolean valid = true;

		if (outcomeUncertainty == 0) {
			ms = useMinMax(mg);
		}
		// few outcomes changed
		else if (outcomeUncertainty < mg.getNumActions(playerNumber))
			ms = useFew(mg);
		// many outcomes changed
		else
			// description += "- many outcomes changed ";
			ms = useMany(mg);
		return ms;

	}

	private MixedStrategy useMinMax(MatrixGame mg) {
		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber - 1));

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

		double[][] regretTable = new double[row.length][column.length];

		// create RegretTable
		for (int m = 0; m < row.length; m++) {
			for (int n = 0; n < column.length; n++) {
				regretTable[row[n] - 1][column[m] - 1] = value[n];
			}
		}

		double[] bestOutcome = new double[row.length];

		// find bestOutcome for each Row
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				double t = regretTable[m][n];
				if (bestOutcome[m] < t) {
					bestOutcome[m] = t;
				}
			}
		}

		// subtract bestOutcome to regretTable values
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				regretTable[n][m] = bestOutcome[n] - regretTable[n][m];
			}
		}

		// find max regret for each row
		double[] maxRegret = new double[row.length];

		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				if (maxRegret[m] < regretTable[m][n]) {
					maxRegret[m] = regretTable[m][n];
				}
			}
		}

		// pick the lowest regret from all the rows
		double maxminRegret = 100;
		double temp2 = 0;
		int maxRow = 0;
		for (int n = 0; n < maxRegret.length; n++) {
			temp2 = maxRegret[n];
			if (temp2 < maxminRegret && temp2 != 0) {
				maxminRegret = temp2;
				maxRow = n;
			}
		}

		// change probablity for the lowest regret, all others are 0
		ms.setProb(maxRow + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (maxRow + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

	private MixedStrategy useFew(MatrixGame mg) {
		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber - 1));

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

		double[][] regretTable = new double[row.length][column.length];

		// create RegretTable
		for (int m = 0; m < row.length; m++) {
			for (int n = 0; n < column.length; n++) {
				regretTable[row[n] - 1][column[m] - 1] = value[n];
			}
		}

		double[] bestOutcome = new double[row.length];

		// find bestOutcome for each Row
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				double t = regretTable[m][n];
				if (bestOutcome[m] < t) {
					bestOutcome[m] = t;
				}
			}
		}

		// subtract bestOutcome to regretTable values
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				regretTable[n][m] = bestOutcome[n] - regretTable[n][m];
			}
		}

		// find max regret for each row
		double[] maxRegret = new double[row.length];

		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				if (maxRegret[m] < regretTable[m][n]) {
					maxRegret[m] = regretTable[m][n];
				}
			}
		}

		// pick the lowest regret from all the rows
		double maxminRegret = 100;
		double temp2 = 0;
		int maxRow2 = 0;
		int maxRow = 0;
		for (int n = 0; n < maxRegret.length; n++) {
			temp2 = maxRegret[n];
			if (temp2 < maxminRegret && temp2 != 0) {
				maxRow2 = maxRow;
				maxminRegret = temp2;
				maxRow = n;
			}
		}

		// change probability for the lowest regret, all others are 0
		ms.setProb(maxRow + 1, 0.7);
		ms.setProb(maxRow2 + 1, 0.3);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (maxRow + 1 == a || maxRow2 + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;
	}

	public MixedStrategy useMany(MatrixGame mg) {

		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber - 1));

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

		double[][] regretTable = new double[row.length][column.length];

		// create RegretTable
		for (int m = 0; m < row.length; m++) {
			for (int n = 0; n < column.length; n++) {
				regretTable[row[n] - 1][column[m] - 1] = value[n];
			}
		}

		double[] bestOutcome = new double[row.length];

		// find bestOutcome for each Row
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				double t = regretTable[m][n];
				if (bestOutcome[m] < t) {
					bestOutcome[m] = t;
				}
			}
		}

		// subtract bestOutcome to regretTable values
		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				regretTable[n][m] = bestOutcome[n] - regretTable[n][m];
			}
		}

		// find max regret for each row
		double[] maxRegret = new double[row.length];

		for (int m = 0; m < value.length; m++) {
			for (int n = 0; n < value.length; n++) {
				if (maxRegret[m] < regretTable[m][n]) {
					maxRegret[m] = regretTable[m][n];
				}
			}
		}

		// pick the second lowest lowest regret from all the rows
		double maxminRegret = 100;
		double temp2 = 0;
		int maxRow = 0;
		int maxRow2 = 0;
		for (int n = 0; n < maxRegret.length; n++) {
			temp2 = maxRegret[n];
			if (temp2 < maxminRegret && temp2 != 0) {
				maxRow2 = maxRow;
				maxminRegret = temp2;
				maxRow = n;
			}
		}

		// change probability for the lowest regret, all others are 0
		ms.setProb(maxRow2 + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (maxRow2 + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

}
