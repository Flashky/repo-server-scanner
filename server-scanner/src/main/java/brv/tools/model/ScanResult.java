package brv.tools.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author flash
 *
 */
public class ScanResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7136000466592102702L;
	
	private Protocol protocol;
	private String ip;
	private String hostname;
	private int port;
	private Date date;
	private ServerStatus status;
	
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public ServerStatus getStatus() {
		return status;
	}
	
	public void setStatus(ServerStatus status) {
		this.status = status;
	}
	
	
}
