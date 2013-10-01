package robotrader.gui;

import org.apache.log4j.BasicConfigurator;

/**
 * The robotrader main class.
 * 
 * @author xcapt
 * @author klinst
 */
public class RobotraderMain 
{

  /**
   * Starts the Robotrader GUI application.
   * 
   * @param args No arguments required
   */
	public static void main(String[] args) 
  {
		BasicConfigurator.configure();
		
		RobotraderApp app = new RobotraderApp();
		app.init();
		app.start();
	}
}
