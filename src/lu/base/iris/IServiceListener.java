package lu.base.iris;

import java.util.EventListener;

/**
 * IServiceListener description : service listener interface
 */
public interface IServiceListener extends EventListener {
    /**
	 * Method onEvent: called by the service event dispatcher to publish the
	 * event.
	 * @param evt service event
	 */
	void onEvent(ServiceEvent evt);
}