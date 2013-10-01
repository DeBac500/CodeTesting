package lu.base.iris.gui.mdi;

import lu.base.iris.gui.IViewComponentEvent;

/**
 * ViewComponentEvent description : encapsulates events to IViewComponent
 * Allows transmission of IView events to the encapsulated IViewComponent
 */
public class ViewComponentEvent implements IViewComponentEvent{

	public static int HIDE = 1;

	private Object _source;
	private int _type;
	private int _id;
	private Object _argument;

	/**
	 *  ViewComponentEvent constructor.
	 * @param source
	 * @param type
	 * @param id
	 * @param argument
	 */
	public ViewComponentEvent(Object source, int type, int id, Object argument) {
		_source = source;
		_type = type;
		_id = id;
		_argument = argument;
	}

	/**
	 * Returns the argument.
	 * @return Object
	 */
	public Object getArgument() {
		return _argument;
	}

	/**
	 * Returns the id.
	 * @return int
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Returns the source.
	 * @return Object
	 */
	public Object getSource() {
		return _source;
	}

	/**
	 * Returns the type.
	 * @return int
	 */
	public int getType() {
		return _type;
	}

}
