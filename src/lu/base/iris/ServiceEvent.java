package lu.base.iris;

import java.util.EventObject;

/**
 * ServiceEvent description : service event implementation
 */
public class ServiceEvent extends EventObject 
{
	/**
   * The Serial ID
   */
  private static final long serialVersionUID = 5256550379261849551L;

  /** event sent after the service has been initialized */
	public static final int EVT_SERVICE_INITIATED = 101;
	
  /**
   * The type of service
   */
  private int _type;

    /**
	 * ServiceEvent constructor.
	 * @param source event source
	 * @param type event type
	 */
	public ServiceEvent(IService source, int type) 
  {
     super(source);
     _type = type;
  }

    /**
	 * Method getType: retrieves the event type.
	 * @return int event type
	 */
	public int getType() 
  {
        return _type;
  }
}