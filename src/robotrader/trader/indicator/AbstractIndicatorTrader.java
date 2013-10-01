package robotrader.trader.indicator;

import robotrader.market.IIndicator;
import robotrader.trader.AbstractTrader;

/**
 *  AbstractIndicatorTrader  description: abstract class for implementing trader
 * classes using a single indicator and completly following its indication.
 * <br>
*/
public abstract class AbstractIndicatorTrader extends AbstractTrader {
	private String _instrument = "";
	private IIndicator _indicator;

	/**
	 * Method setIndicator: sets technical indicator to be used
	 * @param indicator technical indicator to be used
	 */
	public void setIndicator(IIndicator indicator) {
		_indicator = indicator;
	}

	/**
	 * Method getIndicator: gets technical indicator used
	 * @return IIndicator technical indicator used
	 */
	public IIndicator getIndicator() {
		return _indicator;
	}
	
	/**
	 * Returns the instrument.
	 * @return String
	 */
	public String getInstrument() {
		return _instrument;
	}

	/**
	 * Sets the instrument.
	 * @param instrument The instrument to set
	 */
	public void setInstrument(String instrument) {
		_instrument = instrument;
	}
	/**
	 * simple reaction to market events: follow indicator when possible, sell
	 * everything when indicator is SELL and buy as much as possible when
	 * indicator is BUY, hold in any other case.
	 * @see robotrader.trader.AbstractTrader#update()
	 */
	public void update() 
	{
		if (getIndicator() != null) 
		{
			getIndicator().add(getMarket().current());

			if (getIndicator().isReady()) 
			{
				float cash = getCash();
				float quantity = getPosition(_instrument);

				if (getIndicator().isSell()) 
				{
					if (quantity > 0) 
					{
						addQuantityOrder(_instrument, -quantity);
					}
				} 
				else if (getIndicator().isBuy()) 
				{
					if (cash > 0) 
					{
						addAmountOrder(_instrument, cash);
					}
				}
			}
		}
	}
}