package robotrader.stats;

import java.util.ArrayList;
import java.util.List;

import robotrader.trader.AbstractTrader;

/**
 * This object holds a list of traders and computes the
 * percentage of change in the traders evaluation within one
 * year. 
 * 
 * @author xcapt
 * @author klinst
 */
public class YearlyReport 
{
	/**
	 * The list of traders
	 */
	private List _traders;
	
	/**
	 * The list of evaluations
	 * for all traders
	 */
	private ArrayList _evalHistory = new ArrayList();

	/**
	 * Creates a new YearlyReport object.
	 *
	 * @param traders list of traders
	 */
	public YearlyReport(List traders) 
	{
		_traders = traders;
		pushEval();
	}

	/**
	 * Gets all the evaluations of all traders
	 * and adds them to the evaluation history
	 * list.
	 */
	private void pushEval() 
	{
		float[] eval = new float[_traders.size()];
		
		for (int i = 0; i < _traders.size(); i++) 
		{
      AbstractTrader trader = (AbstractTrader) _traders.get(i);
			eval[i] = trader.getEvaluation();
		}
		_evalHistory.add(0, eval);
	}

	/**
	 * Adds the evaluations of all traders
	 * to the evalution history list.
	 */
	public void yearUpdate() 
	{
		pushEval();
	}

	/**
	 * Gets the change in the evaluation of the
	 * trader's account from this year and the 
	 * previous year.
	 * 
	 * @param traderno The number of the trader in the list
	 * @return The fraction of change in the trader's evaluation
	 */
	public float getYearChange(int traderno) 
	{
		float[] lasteval = (float[]) _evalHistory.get(0);
		float[] preveval = (float[]) _evalHistory.get(1);

		return (lasteval[traderno] - preveval[traderno])
			* 100
			/ preveval[traderno];
	}

}