package lu.base.iris.gui;

/**
 * IViewComponentEvent description : event occuring on an IViewComponent.
 */
public interface IViewComponentEvent {
	/**
	 * Returns the argument.
	 * @return Object
	 */
	public Object getArgument();

	/**
	 * Returns the id.
	 * @return int
	 */
	public int getId();

	/**
	 * Returns the source.
	 * @return Object
	 */
	public Object getSource();

	/**
	 * Returns the type.
	 * @return int
	 */
	public int getType();
}
