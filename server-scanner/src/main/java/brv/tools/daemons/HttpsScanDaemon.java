package brv.tools.daemons;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Scans for HTTPS servers.
 * <p>
 * It will scan on port <code>443</code> by default. <br>
 * The scanned port can be modified using {@link ScanDaemonBuilder#withPort(int)} method.
 * </p>
 * @author flash
 *
 */
public class HttpsScanDaemon extends ScanDaemon {

	protected HttpsScanDaemon(ScanDaemonBuilder builder) throws UnknownHostException {
		
		// Calls the super constructor to validate and initialize all the attributes
		super(builder);
	}
	
	
	@Override
	public boolean ping(String ip) {
		String url = protocol.getScheme() + ip + ":" + port;
		HttpsURLConnection connection = null;
	    try {
	    	connection = (HttpsURLConnection) new URL(url).openConnection();
	        connection.setConnectTimeout(timeout);
	        connection.setReadTimeout(10000);
	        connection.setRequestMethod("HEAD");
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        int responseCode = connection.getResponseCode();
	        
	        return (200 <= responseCode && responseCode <= 399);
	        
	    } catch (IOException exception) {
	        return false;
	    }
	    finally {
	    	if(connection != null)
	    		connection.disconnect();
	    }
	}
}
