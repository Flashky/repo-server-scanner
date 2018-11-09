package brv.tools.daemons.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import brv.tools.daemons.ScanDaemon;
import brv.tools.daemons.exceptions.ScanDaemonNotFoundException;
import brv.tools.model.ScanDaemonConfiguration;

/**
 * Implements the {@link ScanDaemonManager} interface as a map of daemons.
 * <p>
 * The manager adds an abstraction layer which allows to manage and perform actions on ScanDaemons without 
 * needing to know anything about them, excepting the id. 
 * </p>
 * <p>
 * On this implementation, any daemons will be stored as a map of using {@link ScanDaemon#getId()} as an unique key and {@link ScanDaemon} as a value.
 * </p>
 * @author flash
 *
 */
public class ScanDaemonManagerMap implements ScanDaemonManager {

	/**
	 * ScanDaemon container.
	 * <p>
	 * The long corresponds to the {@link ScanDaemon#getId()} unique id.
	 * </p>
	 */
	private Map<Long, ScanDaemon> daemons = new HashMap<>();
	

	@Override
	public boolean add(ScanDaemon daemon) {
		Objects.requireNonNull(daemon);
		
		daemons.put(daemon.getId(), daemon);
		return daemon.start();
	}
	
	public boolean resume(long daemonId) throws ScanDaemonNotFoundException {

		ScanDaemon daemon = daemons.get(daemonId);
		
		if(daemon == null)
			throw new ScanDaemonNotFoundException(daemonId);
		
		return daemon.start();
		
	}

	public boolean stop(long daemonId) throws ScanDaemonNotFoundException {
		
		ScanDaemon daemon = daemons.get(daemonId);
		
		if(daemon == null)
			throw new ScanDaemonNotFoundException(daemonId);
		
		return daemon.stop();
	}
	
	public boolean interrupt(long daemonId) throws ScanDaemonNotFoundException {
		

		ScanDaemon daemon = daemons.get(daemonId);
		
		if(daemon == null)
			throw new ScanDaemonNotFoundException(daemonId);
		
		return daemon.interrupt();

	}
	

	public boolean remove(long daemonId) throws ScanDaemonNotFoundException {
		
		ScanDaemon daemon = daemons.get(daemonId);
		
		if(daemon == null)
			throw new ScanDaemonNotFoundException(daemonId);
		
		daemon.stop();
		daemons.remove(daemonId);
		
		return true;
	}

	@Override
	public boolean resume() {
		
		
		boolean result = false;
		
		for(Entry<Long,ScanDaemon> entry : daemons.entrySet()) {
			result |= entry.getValue().start();
		}
		
		return result;
	}

	public boolean stop() {
		
		boolean result = false;
		
		for(Entry<Long,ScanDaemon> entry : daemons.entrySet()) {
			result |= entry.getValue().stop();
		}
		
		return result;
	}
	
	public boolean interrupt() {
		
		boolean result = false;
		
		for(Entry<Long,ScanDaemon> entry : daemons.entrySet()) {
			result |= entry.getValue().interrupt();
		}
		
		return result;
	}
	
	public boolean remove() {
		
		boolean result = false;
		
		Iterator<Long> iterator = daemons.keySet().iterator();
		Long daemonId;
		
		while (iterator.hasNext())
		{
			daemonId = iterator.next();
			result |= daemons.get(daemonId).stop();
			iterator.remove();
		 }
		
		return result;
	}

	public boolean contains(long daemonId) {
		return daemons.containsKey(daemonId);
	}
	
	public ScanDaemonConfiguration find(long daemonId) {

		ScanDaemon daemon = daemons.get(daemonId);
		return (daemon != null) ? daemon.getConfiguration() : null;	
	}

	public List<ScanDaemonConfiguration> find() {
		List<ScanDaemonConfiguration> result = new LinkedList<>();

		for (Entry<Long,ScanDaemon> entry : daemons.entrySet()) {
			result.add(entry.getValue().getConfiguration());
		  }
		
		return result;
	}






}
