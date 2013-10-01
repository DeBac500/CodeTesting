package robotrader.engine.model;

/**
 * Objects of this class store data about the property that
 * can be set by the setPropertyValue method of the 
 * AbstractTrader class.
 */
public class PropertyMetaData {
	
	/** type interger */
	public static final int TYPE_INTEGER = 0;
	
	/** type double */
	public static final int TYPE_DOUBLE = 1;
  
  /** type int array */
  public static final int TYPE_INTARRAY = 2;

	/**
	 * the key of the property
	 */
	private String _key;
	
	/**
	 * the type of the property
	 */
	private int _type;
	
	/**
	 * the description of the property
	 */
	private String _description;

	/**
	 * creates a new Property Meta Data object
	 * with and empty description
	 * 
	 * @param key the key of the property
	 * @param type the type. either TYPE_INTEGER or 
	 * TYPE_DOUBLE
	 */
	public PropertyMetaData(String key, int type) {
		this(key, type, "");
	}

	/**
	 * creates a new Property Meta Data object.
	 * 
	 * @param key the key of the property
	 * @param type the type of property
	 * @param description the description of the property
	 */
	public PropertyMetaData(String key, int type, String description) {
		_key = key;
		_type = type;
		_description = description;
	}
	
	/**
	 * retrieve key name
	 * 
	 * @return the key
	 */
	public String getKey() {
		return _key;
	}
	
	/**
	 * retrieve value type
	 * 
	 * @return the type of value contained.
	 */
	public int getType() {
		return _type;
	}
	
	/**
	 * retrieve description
	 * 
	 * @return description of the property
	 */
	public String getDescription() {
		return _description;
	}
	
	/**
	 * retrieve type as string
	 * 
	 * @return the type in string format
	 */
	public String getTypeString() {
		return typeToString(getType());
	}
	
	/**
	 * retrieve name for a type
	 * 
	 * @param the type. either TYPE_INTEGER or 
	 * TYPE_DOUBLE
	 * @return the description of the type 
	 */
	public static final String typeToString(int type) {
		switch (type) {
			case TYPE_INTEGER :
				return "INTEGER";
			case TYPE_DOUBLE :
				return "DOUBLE";
		}
		return "?";
	}
}
