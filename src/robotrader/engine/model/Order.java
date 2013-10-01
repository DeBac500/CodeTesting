package robotrader.engine.model;

/**
 * This class holds data for an order placed by a Trader
 * 
 * orders can be placed by cashamount or by quantity
 */
public class Order 
{
	private boolean _qtyorder;
	private float _value;
	private String _instrument;

	/**
	 * constructor for a new order may it be by quantity
	 * or by amount
	 */
	public Order(String instrument, boolean qtyorder, float value) {
		_qtyorder = qtyorder;
		_value = value;
		_instrument = instrument;
	}
	
  /**
	 * retrieve typeof order : quantity or cash amount
	 */
	public boolean isQuantityOrder() 
  {
		return _qtyorder;
	}
	
  /**
	 * retrieve value of order (quantity or cash amount) 
	 * depending on isQuantityOrder
	 */
	public float getValue() 
  {
		return _value;
	}
	
  /**
	 * retrieve instrument for order
	 */
	public String getInstrument() {
		return _instrument;
	}

}
