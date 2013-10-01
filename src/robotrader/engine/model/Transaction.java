package robotrader.engine.model;

import java.util.Date;

/**
 *  Transaction        description: One Object of this class hold the data for
 * one executed transaction (=executed order=trade).
 * <br>
*/
public class Transaction {
	private Date _date;
	private String _instrument;
	private float _amount;
	private float _quantity;

	/**
	 *  Transaction constructor.
	 * @param date trade date
	 * @param inst instrument traded 
	 * @param quantity quantity traded (sells have negative quantities) 
	 * @param amount cash amount traded (quantity*price)
	 */
	public Transaction(Date date, String inst, float quantity, float amount) {
		_date = date;
		_instrument = inst;
		_quantity = quantity;
		_amount = amount;
	}

	/**
	 * Method getAmount.
	 * @return double cash amount traded (quantity*price) 
	 */
	public float getAmount() {
		return _amount;
	}
	/**
	 * Method getDate.
	 * @return Date trade date
	 */
	public Date getDate() {
		return _date;
	}

	/**
	 * Method getInstrument.
	 * @return String traded instrument(=stock code)
	 */
	public String getInstrument() {
		return _instrument;
	}

	/**
	 * Method getQuantity.
	 * @return double quantity traded (sells have negative quantities)quantity
	 * quantity traded (sells have negative quantities)
	 */
	public float getQuantity() {
		return _quantity;
	}
}