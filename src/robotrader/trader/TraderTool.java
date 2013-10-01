package robotrader.trader;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A utility to show the relative performance of a number
 * of traders.
 * 
 * @author bob
 * @author klinst
 */
public class TraderTool {
	
	/**
	 * prints a sorted list of traders to System.out.
	 * The lists are sorted by the profit and loss per
	 * transaction as well as the overall profit and
	 * loss.
	 *  
	 * @param traders
	 */
	public static void printRank(Object[] traders) {
		System.out.println();
		System.out.println();
		System.out.println("--- Trader ranks ---");

		System.out.println("--- Trader ranks PnlPerTrans ---");

		Comparator comp = getPnlPerTransComparator();
		Arrays.sort(traders, comp);

		for (int i = 0; i < traders.length; i++) {
			AbstractTrader trader = (AbstractTrader) traders[i];

			//IAccount account = (IAccount)_accounts.get(trader);
			double score =
				(trader.getTransactionCount() == 0)
					? 0
					: (trader.getPnL() / trader.getTransactionCount());

			System.out.println(
				(i + 1)
					+ ") "
					+ trader.getName()
					+ " pnl/trans "
					+ score
					+ " #: "
					+ trader.getTransactionCount());
		}

		System.out.println("--- Trader ranks Pnl ---");
		comp = getPnlComparator();
		Arrays.sort(traders, comp);

		for (int i = 0; i < traders.length; i++) {
			AbstractTrader trader = (AbstractTrader) traders[i];

			System.out.println(
				(i + 1)
					+ ") "
					+ trader.getName()
					+ " pnl: "
					+ trader.getPnL()
					+ " #: "
					+ trader.getTransactionCount());
		}

		System.out.println("---   ---");
	}

	/**
	 * returns a Comparator which compares
	 * Abstract Traders in terms of their
	 * overall profit and loss
	 * 
	 * @return Comparator
	 * @see java.util.Comparator
	 */
	public static Comparator getPnlComparator() {
		return new Comparator() {
			
			/**
			 * @return false
			 */
			public boolean equals(Object obj) {
				return false;
			}

			/**
			 * compares two Abstract Traders in
			 * terms of their overall profit and loss
			 * 
			 * @param o1 The first Trader 
			 * @param o2 The second Trader
			 * @return a negative integer, zero, or a positive 
			 * integer as the first argument is less than, equal to, 
			 * or greater than the second
			 */
			public int compare(Object o1, Object o2) {
				AbstractTrader t2 = (AbstractTrader) o2;
				AbstractTrader t1 = (AbstractTrader) o1;

				return Double.compare(
						t2.getPnL(),
						t1.getPnL());
			}
		};
	}

	/**
 	 * returns a Comparator which compares
	 * Abstract Traders in terms of their
	 * profit and loss per transaction
	 * 
	 * @return Comparator
	 * @see java.util.Comparator
	 */
	public static Comparator getPnlPerTransComparator() {
		return new Comparator() {
			public boolean equals(Object obj) {
				return false;
			}

			/**
			 * compares two traders regarding their
			 * profit and loss per transaction
			 * 
			 * @param o1 first Trader
			 * @param o2 second Trader
			 * @return a negative integer, zero, or a positive 
			 * integer as the first argument is less than, equal to, 
			 * or greater than the second
			 */
			public int compare(Object o1, Object o2) {
				AbstractTrader t2 = (AbstractTrader) o2;
				AbstractTrader t1 = (AbstractTrader) o1;

				if ((t1.getTransactionCount() == 0)
					&& (t2.getTransactionCount() == 0)) {
					return 0;
				}

				if (t1.getTransactionCount() == 0) {
					return 1;
				}

				if (t2.getTransactionCount() == 0) {
					return -1;
				}

				return Double.compare(
						t2.getPnL()/t2.getTransactionCount(),
						t1.getPnL()/t1.getTransactionCount());
			}
		};
	}
}
