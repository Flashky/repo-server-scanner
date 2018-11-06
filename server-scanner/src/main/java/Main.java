

import java.net.UnknownHostException;

import brv.tools.daemons.ScanDaemon;
import brv.tools.daemons.ScanDaemonBuilder;
import brv.tools.model.Protocol;

public class Main {
	public static void main(String[] args) throws UnknownHostException {
		
		ScanDaemonBuilder builder = new ScanDaemonBuilder(Protocol.FTP);
		ScanDaemon daemon1 = builder.withPort(2121).withTimeout(50).build();
		
		builder = new ScanDaemonBuilder(Protocol.HTTP);
		ScanDaemon daemon2 = builder.withTimeout(30).build();
		daemon1.start();
		daemon2.start();
				
			
		/*	
		ScanDaemon daemonHttp1 = builder.withTimeout(50).withSleep(10000).build();
		ScanDaemon daemonHttp2 = builder.withPort(5000).build();
	
		ScanDaemonBuilder<HttpsScanDaemon> builder2 = new HttpsScanDaemon.Builder();
		ScanDaemon daemonHttps1 = builder2.withTimeout(50).withSleep(10000).build();
		ScanDaemon daemonHttps2 = builder2.withPort(5001).build();
		System.out.println("Starting");
		
		Executor executor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		executor.execute(daemonHttp1);
		executor.execute(daemonHttp2);
		executor.execute(daemonHttps1);
		executor.execute(daemonHttps2);
		
		System.out.println(Protocol.getFromString("http").toString());
		/*
		daemonHttp1.start();
		daemonHttp2.start();
		daemonHttps1.start();
		daemonHttps2.start();
		*/
		System.out.println("All daemons launched");
		
		//daemonHttps2.start();
		
	}
}
