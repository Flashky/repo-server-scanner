package brv.tools.daemons;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Scans for FTP servers.
 * <p>
 * It will scan on port <code>21</code> by default. <br>
 * The scanned port can be modified using {@link ScanDaemonBuilder#withPort(int)} method.
 * </p>
 * @author flash
 *
 */
public class FtpScanDaemon extends ScanDaemon {

	private Logger logger = Logger.getLogger(FtpScanDaemon.class.getName());
	
	private FTPClient ftp = new FTPClient();
	
	protected FtpScanDaemon(ScanDaemonBuilder builder) throws UnknownHostException {
		
		// Calls the super constructor to validate and initialize all the attributes
		super(builder);
		
		ftp.setConnectTimeout(timeout);
	}

	
	
	@Override
	public boolean ping(String ip) {

		// FTP Server return status:
		// https://en.wikipedia.org/wiki/List_of_FTP_server_return_codes
		
		try {
			ftp.connect(ip,port);
			return (ftp.getReplyCode() == FTPReply.SERVICE_READY);

		} catch (IOException e) {
			return false;
		} finally {
			disconnect(ip);
		}
		
	}
	
	private void disconnect(String ip) {
		try {
			
			ftp.disconnect();
		} catch (IOException e) {
			logger.warning("Couldn't disconnect properly from "+protocol+" server "+ip+ "on port "+port);
		}
	}

}
