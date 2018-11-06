package brv.tools.model;

import java.io.Serializable;

/**
 * Class which represents the configuration of an specific {@link brv.tools.daemons.ScanDaemon ScanDaemon}.
 * <p>
 * Configuration includes the following daemon scanning details:
 * </>
 * <ul>
 * 	<li>Protocol used.</li>
 * 	<li>Port used.</li>
 * 	<li>Target network id.</li>
 * 	<li>Connection timeout in milliseconds.</li>
 * 	<li>Sleep time between scans in milliseconds</li>
 * </ul>
 * @author flash
 *
 */
public class ScanDaemonConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 884316947462034057L;
	
	private long id = -1;
	private Protocol protocol = null;
	private String networkId = "";
	private int port = 0;
	private int timeout = 10;
	private int sleep = 15000;
	private boolean running = false;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = Protocol.getFromString(protocol);
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((networkId == null) ? 0 : networkId.hashCode());
		result = prime * result + port;
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScanDaemonConfiguration other = (ScanDaemonConfiguration) obj;
		if (networkId == null) {
			if (other.networkId != null)
				return false;
		} else if (!networkId.equals(other.networkId)) {
			return false;
		}
		if (port != other.port)
			return false;

		return (protocol != other.protocol);
	}

	@Override
	public String toString() {
		return "ScanDaemonDTO [protocol=" + protocol + ", networkId=" + networkId + ", port=" + port + ", timeout="
				+ timeout + ", sleep=" + sleep + "]";
	}
	
	
}
