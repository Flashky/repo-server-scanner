package brv.tools.daemons;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Scans for HTTP servers.
 * <p>
 * It will scan on port <code>80</code> by default. <br>
 * The scanned port can be modified using {@link ScanDaemonBuilder#withPort(int)} method.
 * </p>
 * @author flash
 *
 */
public class HttpClientScanDaemon extends ScanDaemon {

	
	CloseableHttpClient httpclient;
	
	protected HttpClientScanDaemon(ScanDaemonBuilder builder) throws UnknownHostException {
		
		// Calls the super constructor to validate and initialize all the attributes
		super(builder);
		RequestConfig requestConfig = RequestConfig.custom()
										.setSocketTimeout(10000)
										.setConnectTimeout(timeout)
										.build();
		
		httpclient = HttpClientBuilder.create()
										.setDefaultRequestConfig(requestConfig)
										.build();
		
	}
	
	
	@Override
	public boolean ping(String ip) {
		
		boolean result;
		HttpHead headMethod = null;
		CloseableHttpResponse response = null;
		try {
			String url = protocol.getScheme() + ip + ":" + port;
			
			headMethod = new HttpHead(url);
			response = httpclient.execute(headMethod);
			int responseCode = response.getStatusLine().getStatusCode();
			
			// Any codes between 200 and 399 should be ok.
			result = (HttpStatus.SC_OK <= responseCode && responseCode < HttpStatus.SC_BAD_REQUEST);
			 
		} catch (IOException e) {
			result = false;
		} finally {
			disconnect(headMethod, response);
		}
		
		return result;
	}
	
	private void disconnect(HttpHead method, CloseableHttpResponse response) {
		
		if(method != null) {
			method.releaseConnection();
		}
		
		if(response != null) {
			try {
				response.close();
			} catch (IOException e) {
			}
		}
		
	}
}
