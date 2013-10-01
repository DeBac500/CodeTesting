package lu.base.iris;

/**
 * IApplication description : interface for an application, in most cases only
 * one application object should exist.
 */
public interface IApplication {
	/**
	 * Method init: initializes the interface.
	 * @return boolean false if a fatal error occured during initialization
	 */
	boolean init();
	/**
	 * Method start: starts the application.
	 */
	void start();
	/**
	* Method stop: stops the application, the application stop can be rejected.
	*/
	void stop();
}