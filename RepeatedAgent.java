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

public class RepeatedAgent extends Player {
	protected final String newName = "Repeated Agent"; // Overwrite this variable in your player subclass

	/** Your constructor should look just like this */
	public RepeatedAgent() {
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

		if (playerNumber == 0)
			System.out.println("You are row player");
		else
			System.out.println("You are column player");
		mg.printMatrix();

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
		double[] payoff = new double[mg.getNumProfiles()];

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
			payoff[j] = temp[i];
			j++;
		}

		MixedStrategy strats[] = new MixedStrategy[mg.getNumPlayers()];
		double[][] h = new double[history.size() * 2][mg.getNumActions(playerNumber - 1) * 2];
		for (int i1 = 0; i1 < history.size(); i1++) {
			for (int p = 0; p < strats.length; p++) {
				strats[p] = history.get(i1)[p];
				for (int j1 = 1; j1 <= mg.getNumActions(playerNumber - 1); j1++) {
					// System.out.println(i1 + " " + p);
					h[i1][j1] = strats[p].getProb(j1);
				}
			}
		}

		int[] num = new int[h.length];
		for (int a = 0; a < h.length; a++) {
			for (int s = 0; s < h[a].length; s++) {
				if (h[a][s] > 0) {
					num[a] += h[a][s];
				}
			}
		}

		int maxChoice = 0;
		int tempChoice = 0;
		for (int x = 0; x < num.length; x++) {
			tempChoice = num[x];
			if (tempChoice > maxChoice) {
				maxChoice = x;
			}
		}

		// get maxPayoff for the row
		int finalchoice = 0;
		double temp1 = 0;
		for (int m = 0; m < 3; m++) {
			temp1 = payoff[m];
			if (payoff[maxChoice] < temp1) {
				finalchoice = m;
			}
		}

		// change probability for the lowest regret, all others are 0
		ms.setProb(finalchoice + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (finalchoice + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

	private MixedStrategy useFew(MatrixGame mg) {
		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber - 1));

		double[] temp = new double[mg.getNumProfiles()];
		double[] payoff = new double[mg.getNumProfiles()];

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
			payoff[j] = temp[i];
			j++;
		}

		MixedStrategy strats[] = new MixedStrategy[mg.getNumPlayers()];
		double[][] h = new double[history.size() * 2][mg.getNumActions(playerNumber - 1) * 2];
		for (int i1 = 0; i1 < history.size(); i1++) {
			for (int p = 0; p < strats.length; p++) {
				strats[p] = history.get(i1)[p];
				for (int j1 = 1; j1 <= mg.getNumActions(playerNumber - 1); j1++) {
					// System.out.println(i1 + " " + p);
					h[i1][j1] = strats[p].getProb(j1);
				}
			}
		}

		int[] num = new int[h.length];
		for (int a = 0; a < h.length; a++) {
			for (int s = 0; s < h[a].length; s++) {
				if (h[a][s] > 0) {
					num[a] += h[a][s];
				}
			}
		}

		int maxChoice = 0;
		int tempChoice = 0;
		for (int x = 0; x < num.length; x++) {
			tempChoice = num[x];
			if (tempChoice > maxChoice) {
				maxChoice = x;
			}
		}

		// get maxPayoff for the row
		int finalchoice = 0;
		double temp1 = 0;
		for (int m = 0; m < 3; m++) {
			temp1 = payoff[m];
			if (payoff[maxChoice] < temp1) {
				finalchoice = m;
			}
		}

		// change probability for the lowest regret, all others are 0
		ms.setProb(finalchoice + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (finalchoice + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

	public MixedStrategy useMany(MatrixGame mg) {

		MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber - 1));

		double[] temp = new double[mg.getNumProfiles()];
		double[] payoff = new double[mg.getNumProfiles()];

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
			payoff[j] = temp[i];
			j++;
		}

		MixedStrategy strats[] = new MixedStrategy[mg.getNumPlayers()];
		double[][] h = new double[history.size() * 2][mg.getNumActions(playerNumber - 1) * 2];
		for (int i1 = 0; i1 < history.size(); i1++) {
			for (int p = 0; p < strats.length; p++) {
				strats[p] = history.get(i1)[p];
				for (int j1 = 1; j1 <= mg.getNumActions(playerNumber - 1); j1++) {
					// System.out.println(i1 + " " + p);
					h[i1][j1] = strats[p].getProb(j1);
				}
			}
		}

		int[] num = new int[h.length];
		for (int a = 0; a < h.length; a++) {
			for (int s = 0; s < h[a].length; s++) {
				if (h[a][s] > 0) {
					num[a] += h[a][s];
				}
			}
		}

		int maxChoice = 0;
		int tempChoice = 0;
		for (int x = 0; x < num.length; x++) {
			tempChoice = num[x];
			if (tempChoice > maxChoice) {
				maxChoice = x;
			}
		}

		// get maxPayoff for the row
		int finalchoice = 0;
		double temp1 = 0;
		for (int m = 0; m < 3; m++) {
			temp1 = payoff[m];
			if (payoff[maxChoice] < temp1) {
				finalchoice = m;
			}
		}

		// change probability for the lowest regret, all others are 0
		ms.setProb(finalchoice + 1, 1.0);// pure strategy that picks the 1st action
		for (int a = 0; a <= mg.getNumActions(playerNumber - 1); a++)
			if (finalchoice + 1 == a) {
			} else
				ms.setProb(a, 0);// set the rest of the strategy to 0

		return ms;

	}

}
