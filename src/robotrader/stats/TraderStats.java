package robotrader.stats;

/**
 * A class holding trading statistics such
 * as the maximum win and loss for a 
 * trader and stock.
 *  
 * @author bob
 * @author klinst
 */
public class TraderStats 
{
	private float _openposition = 0;
	private float _maxloss = 0;
	private float _maxwin = 0;
	private int _winposday = 0;
	private int _losposday = 0;
	private int _downposday = 0;
	private int _upposday = 0;
	private int _days = 0;
	private float _lasteval;
	private float _cumulatedrisk = 0;

	/**
	 * Sets the latest evaluation 
	 * @param eval
	 */
	public void setLastEval(float eval) {
		_lasteval = eval;
	}

	/**
	 * Gets the latest evaluation
	 * @return
	 */
	public double getLastEval() {
		return _lasteval;
	}

	/**
	 * Returns the _losposday.
	 * @return int
	 */
	public int getLosingPosDays() {
		return _losposday;
	}

	/**
	 * Returns the _maxloss.
	 * @return double
	 */
	public double getMaximumPosLoss() {
		return _maxloss;
	}

	/**
	 * Returns the _maxwin.
	 * @return double
	 */
	public double getMaximumPosWin() {
		return _maxwin;
	}

	/**
	 * Returns the _openposition.
	 * @return double
	 */
	public double getOpenPosition() {
		return _openposition;
	}

	/**
	 * Returns the _winposday.
	 * @return int
	 */
	public int getWinningPosDays() {
		return _winposday;
	}
	
	/**
	 * Gets the number of down positive days
	 * @return
	 */
	public int getDownPosDays() {
		return _downposday;
	}
	
	/**
	 * Get the number of up positive days.
	 * @return
	 */
	public int getUpPosDays() {
		return _upposday;
	}

	/**
	 * Increment the losing positive days.
	 *
	 */
	public void incLosingPosDays() {
		this._losposday++;
	}
	
	/**
	 * Increment the down positive days
	 *
	 */
	public void incDownPosDays() {
		this._downposday++;
	}
	
	/**
	 * Increment the up positive days
	 *
	 */
	public void incUpPosDays() {
		this._upposday++;
	}
	
	/**
	 * Increment the trading days
	 *
	 */
	public void incDays() {
		this._days++;
	}

	/**
	 * Sets the _maxloss.
	 * @param _maxloss The _maxloss to set
	 */
	public void updtLoss(float loss) {
		this._maxloss = Math.min(_maxloss, loss);
	}

	/**
	 * Sets the _maxwin.
	 * @param _maxwin The _maxwin to set
	 */
	public void updtWin(float win) {
		this._maxwin = Math.max(_maxwin, win);
	}

	/**
	 * Sets the _openposition.
	 * @param _openposition The _openposition to set
	 */
	public void setOpenPosition(float openposition) {
		this._openposition = openposition;
	}

	/**
	 * Increments the _winposday.
	 * @param _winposday The _winposday to set
	 */
	public void incWinningPosDays() {
		this._winposday++;
	}

	/**
	 * Gets the number of days of stock data
	 * @return
	 */
	public int getDays() 
  {
		return this._days;
	}

	/**
	 * Increases the cumulated risk
	 * @param risk
	 */
	public void addRisk(float risk) 
  {
		this._cumulatedrisk += risk;
	}

	/**
	 * Calculates the mean risk by taking
	 * the average of the risk over the
	 * trading days.
	 * 
	 * @return
	 */
	public float getMeanRisk() 
  {
		return this._cumulatedrisk / this._days;
	}
}
