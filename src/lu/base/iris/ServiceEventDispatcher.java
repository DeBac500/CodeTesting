package lu.base.iris;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import lu.base.collection.ObjectQueue;

/**
 * ServiceEventDispatcher description : service event dispatcher implementation
 */
public class ServiceEventDispatcher extends Thread
    implements IServiceEventDispatcher {
    /** standard trace object */
    private static Logger trace = Logger.getLogger(
                                            ServiceEventDispatcher.class.getName());
    /** event queue */                                   
    private ObjectQueue _evtqueue = new ObjectQueue(5);
    private HashMap _srvlisteners = new HashMap();
    private boolean _running = true;

    /**
	 * @see lu.base.iris.IServiceEventDispatcher#addListener(lu.base.iris.IService, lu.base.iris.IServiceListener)
	 */
	public void addListener(IService service, IServiceListener listener) {
        Object o = _srvlisteners.get(service);
        if(o==null)
        {
        	ArrayList listeners = new ArrayList();
        	listeners.add(listener);
        	_srvlisteners.put(service,listeners);
        }
        else
        {
        	((ArrayList)o).add(listener);
        }
    }

    /**
	 * @see lu.base.iris.IServiceEventDispatcher#post(lu.base.iris.ServiceEvent)
	 */
	public void post(ServiceEvent evt) {
        _evtqueue.add(evt);
    }

    /**
	 * Method dispatch.
	 * @param evt
	 */
	private void dispatch(ServiceEvent evt) 
    {
    	Object o = _srvlisteners.get(evt.getSource());
    	if(o==null) return;
    	    	
        ArrayList listeners = (ArrayList)o;


        for (Iterator it=listeners.iterator();it.hasNext();) {
            try {
                ((IServiceListener) it.next()).onEvent(evt);
            } catch (Throwable e) {
                trace.error("exception occured when dispatching event", e);
            }
        }
    }

    /**
     * processes the event queue and dispatches it to the listeners
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
        ServiceEvent evt = null;


        while (_running) {
            evt = (ServiceEvent) _evtqueue.next();
            dispatch(evt);
        }
    }

    /**
	 * Method setRunning: sets the running flag used in the thread loop.
	 * @param running false to stop the thread, true and a call to start to
	 * start the thread
	 */
	private void setRunning(boolean running) {
        _running = running;
    }
    
    /**
	 * @see lu.base.iris.IServiceEventDispatcher#startDispatch()
	 */
	public synchronized void startDispatch()
    {
    	setRunning(true);
    	start();
    	
    }
    
    /**
	 * @see lu.base.iris.IServiceEventDispatcher#stopDispatch()
	 */
	public synchronized void stopDispatch()
    {
    	setRunning(false);
    }
}