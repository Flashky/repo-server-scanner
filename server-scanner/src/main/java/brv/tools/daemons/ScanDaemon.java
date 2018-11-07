package brv.tools.daemons;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import brv.tools.listeners.ServerRemovedListener;
import brv.tools.listeners.ServerUpdatedListener;
import brv.tools.model.Protocol;
import brv.tools.model.ScanDaemonConfiguration;
import brv.tools.model.ScanResult;
import brv.tools.model.ServerStatus;

/**
 * Abstract class for scanning servers as a daemon.
 * <p>
 * Template Method pattern is applied on a generic algorithm to scan a range of ip addresses, sending pings and send the notifications to any listeners.
 * <br>The class delegates the specification of how the ping should be done on the subclasses.
 * </p>
 * @author flash
 *
 */
public abstract class ScanDaemon extends ObservableDaemon implements Runnable {

	private Logger logger = Logger.getLogger(ScanDaemon.class.getName());
	
	// Running thread related attributes
	private Thread worker;
	private AtomicBoolean running = new AtomicBoolean(false);
	
	// Scanning related data - Visibile by any subclasses.
	protected long id;
	protected Protocol protocol;
	protected String networkId = "";
	protected int port = 0;
	protected int timeout = 10;
	protected int sleep = 15000;
	
	// Autoincremental id counters to keep track of the current host scan and the current thread number.
	private int currentHost = 1;
	private static AtomicLong currentId = new AtomicLong(1);
	
	// Cached detected servers to compare and detect any changes on each cycle.
	private Map<String, String> detectedServers = new HashMap<>();
	
	/**
	 * ScanDaemon constructor.
	 * @param builder - The builder containing all the building details for the object.
	 * @throws UnknownHostException When network id was not able to be automatically detected.
	 * @throws IllegalArgumentException When <code>builder.port != </code>{@link Protocol#getDefaultPort() }
	 * @throws IllegalArgumentException When <code>builder.timeout < 1  </code> millisecond
	 * @throws IllegalArgumentException When <code>builder.sleep < 1  </code>millisecond
	 */
	protected ScanDaemon(ScanDaemonBuilder builder) throws UnknownHostException {
		
		// Network id: http://www.firewall.cx/networking-topics/protocols/protocols-ip/165-protocols-ip-network-id.html
		// Actualmente solo está preparado para redes de clase C (192.*)

		int defaultPort = builder.getProtocol().getDefaultPort();
		if((builder.getPort() != defaultPort) &&  (builder.getPort() < 1024))
			throw new IllegalArgumentException("Port must be the default "+builder.getProtocol()+" protocol port ("+defaultPort+") or an user registered port greater than 1023.");
		
		if(builder.getTimeout() < 1)
			throw new IllegalArgumentException("Timeout must be greater than 0.");
		
		if(builder.getSleep() < 1)
			throw new IllegalArgumentException("Sleep must be greater than 0.");

		// Obtains the network id.
		// Example: If the hostAdress is "192.168.1.52", the networkId will be "192.168.1"
		String hostAddress = InetAddress.getLocalHost().getHostAddress();		
		this.networkId = hostAddress.substring(0, hostAddress.lastIndexOf('.'));
		
		this.protocol 	= Objects.requireNonNull(builder.getProtocol());
		this.port 		= builder.getPort();
		this.timeout 	= builder.getTimeout();
		this.sleep 		= builder.getSleep();
		
		// No need to synchronize as it is using AtomicInteger
		this.id = currentId.getAndIncrement();
	}
	
	/**
	 * Starts the daemon.
	 * <p>
	 * If the daemon is already running it performs no operation and will return false.
	 * </p>
	 * @return 
	 * <code>true</code> - if the daemon has started running.<br>
	 * <code>false</code> - if the daemon was already running and no action was taken.
	 */
	public boolean start() {
		
		boolean result = false;
		
		if(!running.get()) {
			worker = new Thread(this);
			worker.start();
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Stops the daemon performing a soft stop.
	 * <p>
	 * The daemon <strong>will not</strong> shutdown immediately, but when it finishes its current execution cycle.
	 *<br> 
	 * If the daemon is already stopped, it performs no operation.
	 * </p>
	 * @return 
	 * <code>true</code> - if the daemon has been flagged to be stopped on the next cycle.<br>
	 * <code>false</code> - if the daemon was already stopped and no action was taken.
	 */
	public boolean stop() {
		boolean result = false;
		
		if(running.get()) {
			running.set(false);
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Stops the daemon performing a hard stop.
	 * <p>
	 * The daemon <strong>will</strong> shutdown immediately, aborting any pending tasks.
	 * <br> 
	 * If the daemon is already stopped, it performs no operation.
	 * </p>
	 * @return
	 * <code>true</code> - if the daemon has been stopped.<br>
	 * <code>false</code> - if the daemon was already stopped and no action was taken.
	 */
	public boolean interrupt() {
		
		boolean result = false;
		
		if((worker != null) && (running.get())) {
			running.set(false);
			worker.interrupt();
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Checks if the daemon is currently running.
	 * @return <code>true</code> - if the daemon is currently running.<br>
	 */
	public boolean isRunning() {
		return running.get();
	}
	
	/**
	 * Checks if the daemon is currently stopped.
	 * @return <code>true</code> - if the daemon is currently stopped.<br>
	 */
	public boolean isStopped() {
		return !running.get();
	}
	
	/**
	 * Runnable interface implementation.
	 * <p>
	 * Loops over all the LAN hosts, scanning them until it gets an Interruption signal or 
	 * the methods {@link ScanDaemon#stop()} or {@link ScanDaemon#interrupt()} are invoked.
	 * </p>
	 * <p>
	 * The daemon will go to sleep after each completed cycle of scans.
	 * </p>
	 */
    public void run() 
    { 
    	running.set(true);
    	while(running.get()) {
    		
			// Obtain the next ip to scan
			String ip = nextHost();	
			
			// Send a request to the listening protocol (template method pattern, to implement by subclasses).
			if(ping(ip))
				checkRegisteredServer(ip);
			else 
				checkUnregisteredServer(ip);
    	}
    } 
    
	/**
	 * Obtains the next host ip address to be scanned.
	 * @return <code>String</code> - a valid ip address between <code>[a.b.c.1]</code> and <code>[a.b.c.254]</code>.
	 */
	private String nextHost() {
		String nextHost = networkId + "." + currentHost;
		
		if(currentHost < 254) // 254
			currentHost++;
		else
			currentHost = 1;
			
		return nextHost;
	}
	
    /**
     * Returns an object representing the daemon configuration.
     * @return {@link brv.tools.model.ScanDaemonConfiguration ScanDaemonConfiguration}
     */
    public ScanDaemonConfiguration getConfiguration() {
    	ScanDaemonConfiguration configuration = new ScanDaemonConfiguration();
    	
    	configuration.setId(this.id);
    	configuration.setProtocol(this.protocol.getName());
    	configuration.setNetworkId(this.networkId);
    	configuration.setPort(this.port);
    	configuration.setTimeout(this.timeout);
    	configuration.setSleep(this.sleep);
    	configuration.setRunning(this.isRunning());
    	
    	return configuration;
    }
	
    /**
     * Retrieve this daemon unique id.
     * <p>
     * This identifier <strong>is not</strong> the {@link Thread#getId()} value, but a single autoincremental value.
     * </p>
     * @return <code>long</code> - The unique identifier of the daemon. 
     */
	public long getId() {
		return id;
	}
	
	/**
	 * Attempts to add an ip address from the detected servers cache.
	 * <p>
	 * If an ip address is added to the cache, a notification will be send to all the registered listeners.
	 * </p>
	 * @param ip - the ip address to be checked.
	 * @see ServerUpdatedListener
	 */
	private void checkRegisteredServer(String ip) {
		
		// Se obtiene el hostname y se verifica si es igual al que había guardado
		String hostname = getHostname(ip);
		if(!hostname.equals(detectedServers.get(ip))) 
		{
			logger.info(protocol +" response from " + ip + " ("+hostname+") on port "+ port + " (max timeout: " + timeout + ")");
			detectedServers.put(ip, hostname);
			
			notifyServerUpdatedListeners(generateResult(ip, hostname, ServerStatus.ONLINE));
		}
	}
	
	/**
	 * Attempts to delete an ip address from the detected servers cache.
	 * <p>
	 * If an ip address is deleted from the cache, a notification will be send to all the registered listeners.
	 * </p>
	 * @param ip - the ip address to be checked.
	 * @see ServerRemovedListener
	 */
	private void checkUnregisteredServer(String ip) {
		
		// Si el server estaba registrado previamente
		if(detectedServers.containsKey(ip))
		{
			String hostname = detectedServers.get(ip);
			logger.info("No response from " + ip + " ("+hostname+") on port "+ port + ". Removed from cache.");
			detectedServers.remove(ip);
			
			notifyServerRemovedListeners(generateResult(ip, hostname, ServerStatus.OFFLINE));
		}
	}
	
	/**
	 * Generates a scan result with useful information about the scanned server.
	 * <p>
	 * Scanned information includes:
	 * </p>
	 * <ul>
	 * 	<li>Server ip</li>
	 *	<li>Server hostname</li>
	 *	<li>Server protocol</li>
	 *	<li>Server port</li>
	 *	<li>Ping result</li>
	 *	<li>Generation date of the result</li>
	 * </ul>
	 * @param ip - the scanned ip address
	 * @param hostname - the scanned hostname
	 * @param status - the current server {@link brv.tools.model.ServerStatus status}.
	 * @return <code>{@link brv.tools.model.ScanResult ScanResult}</code> - an object containing useful information about the scanned server.
	 */
	private ScanResult generateResult(String ip, String hostname, ServerStatus status) {
		
		ScanResult result = new ScanResult();
		
		result.setIp(ip);
		result.setHostname(hostname);
		result.setProtocol(protocol);
		result.setPort(port);
		result.setStatus(status);
		result.setDate(new Date());
		
		return result;
	}

	/**
	 * Retrieves the hostname from an ip address.
	 * @param ip - The ip address to retrieve the hostname from.
	 * @return <code>hostname</code> - if it was possible to retrieve it. An empty <code>String</code> will be returned otherwise.
	 */
	private String getHostname(String ip) {
		InetAddress address;
		String hostname;
		try {
			address = InetAddress.getByName(ip);
			hostname = address.getHostName();
		} catch (UnknownHostException e) {
			hostname = "";
		}
		return hostname;
	}
	
	/**
	 * Pings a determined url.
	 * <p>
	 * Subclasses must implement this method specifying how the ping or handshaking should be done
	 * depending of the protocol and server type.
	 * </p>
	 * @param ip the ip to be pinged.
	 * @return <code>true</code> - if a server gives a successful reply.
	 */
	public abstract boolean ping(String ip);

}
