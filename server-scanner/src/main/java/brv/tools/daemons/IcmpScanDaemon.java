package brv.tools.daemons;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import brv.tools.model.Protocol;

/**
 * Scans computers by a simple ICMP echo request.
 * <p>
 * There is no TCP or UDP port number associated with ICMP packets as these numbers are associated with the transport layer above.
 * However, the port <code>1</code> is assigned as it's the <em>"reserved"</em> port as of 
 * <a href="https://tools.ietf.org/html/rfc792">RFC792</a> specification.
 * </p>
 * @author flash
 *
 */
public class IcmpScanDaemon extends ScanDaemon {

	protected IcmpScanDaemon(ScanDaemonBuilder builder) throws UnknownHostException {
		super(builder);
		
		// ICMP port is always 1, so we overwrite any other value entered by the builder.
		this.port = Protocol.ICMP.getDefaultPort();
	}

	@Override
	public boolean ping(String ip) {
		
		try {
			InetAddress address = InetAddress.getByName(ip);
			return address.isReachable(timeout);
		} catch (IOException e) {
			return false;
		}
	}

}
