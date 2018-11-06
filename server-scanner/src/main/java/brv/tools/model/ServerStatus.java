package brv.tools.model;

/**
 * Enumeration of possible status of a server after a scan.
 * @author flash
 *
 */
public enum ServerStatus {
	OFFLINE("offline"),
	ONLINE("online");
	
	private String value;
	
	private ServerStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
