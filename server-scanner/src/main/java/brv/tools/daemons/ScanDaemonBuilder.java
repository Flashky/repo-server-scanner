package brv.tools.daemons;

import java.net.UnknownHostException;
import java.util.Objects;

import brv.tools.model.Protocol;

/**
 * Generic Builder for ScanDaemon subclasses.
 * @author flash
 */
public class ScanDaemonBuilder{
	
	private Protocol protocol;
	
	private boolean defaultPort = true;
	private int port = 0;
	private int timeout = 10;
	private int sleep = 15000;
	
	/**
	 * Constructor for ScanDaemonBuilder.
	 * <p>
	 * Sets the minimum required data for constructing a ScanDaemon.
	 * </p>
	 * @param protocol - The protocol on which a ScanDaemon will be built.
	 * @see Protocol
	 */
	public ScanDaemonBuilder(Protocol protocol) {
		Objects.requireNonNull(protocol, "A protocol must be selected.");
		this.protocol = protocol;
	}
	
	/**
	 * Sets the port the ScanDaemon will be using for scanning.
	 * @param port
	 * @return
	 */
	public ScanDaemonBuilder withPort(int port) {
		this.port = port;
		this.defaultPort = false;
		return this;
	}
	
	/**
	 * Sets the maximum timeout (in milliseconds) before the
	 * @param timeout
	 * @return
	 */
	public ScanDaemonBuilder withTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * Sets the maximum time (in milliseconds) the daemon will go to sleep after a completed cycle.
	 * @param sleep
	 * @return
	 */
	public ScanDaemonBuilder withSleep(int sleep) {
		this.sleep = sleep;
		return this;
	}
	
	
	
	public Protocol getProtocol() {
		return protocol;
	}

	public int getPort() {
		return port;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getSleep() {
		return sleep;
	}

	/**
	 * Builds an ScanDaemon instance.
	 * <p>
	 * As of now, this builder is able to construct daemons for the following protocols:
	 * </p>
	 * <ul>
	 * 	<li>{@link Protocol.HTTP}</li>
	 *  <li>{@link Protocol.HTTPS}</li>
	 * </ul>
	 * <p>
	 * @return <code>ScanDaemon</code> - A daemon which is ready to be launched.
	 * @throws UnknownHostException
	 * @throws UnsupportedOperationException if protocol is not one of the supported ones.
	 */
	public ScanDaemon build() throws UnknownHostException {
		
		ScanDaemon result;
		
		// If no port has been initialized, the default port will be the one defined by the Protocol enum.
		if(defaultPort)
			this.port = protocol.getDefaultPort();
		
		switch(protocol) {
			case ICMP:
				result = new IcmpScanDaemon(this);
				break;
			case HTTP:
				result = new HttpScanDaemon(this);
				break;
			case HTTPS:
				result = new HttpsScanDaemon(this);
				break;
			case FTP:
				result = new FtpScanDaemon(this);
				break;
			default:
				//
				throw new UnsupportedOperationException();
		}
		
		return result;
		
	}
}
