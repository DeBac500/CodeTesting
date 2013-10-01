package lu.base.collection;

import java.util.List;
import java.util.ArrayList;
/**
 * Implementation of a queue for objects on the
 * basis of the java.util.Vector
 * <br>
 * Creation date: (27/07/00 09:13:53)
 * @author: Eric Boudinet
 */
public final class ObjectQueue 
{
	private List _queue;
/**
 * TCPConnectionQueue constructor comment.
 */
public ObjectQueue(int initialSize) {
	_queue = new ArrayList(initialSize);
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/00 09:15:51)
 */
public synchronized void add(Object o) {
	_queue.add(o);
	notify();
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/00 09:15:51)
 */
public synchronized Object next() {
	while(_queue.isEmpty()) {
		try {
			wait();
		} catch(InterruptedException _ex) {
			System.err.println("Interrupted exception");	
		}	
	}

	Object o = _queue.get(0);
	_queue.remove(0);
	return o;
	
}
}
