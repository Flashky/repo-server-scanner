package brv.tests.daemons.http;


import static org.junit.Assert.fail;

import java.net.UnknownHostException;

import org.junit.Test;

import brv.tools.daemons.HttpScanDaemon;

@SuppressWarnings("unused")
public class HttpScanDaemonTests {

	/**
	 * Not defining a port will take the default port of the HttpScanDaemon (80).
	 */
	/*
	@Test
	public void testBuildPortUndefined() {
		try {
			HttpScanDaemon object = (HttpScanDaemon) new HttpScanDaemonBuilder().build();
		} catch (UnknownHostException e) {
			fail();
		}
	}
	
	
	@Test
	public void testBuildPort80() {
		try {
			HttpScanDaemon object = (HttpScanDaemon) new HttpScanDaemonBuilder().withPort(80).build();
		} catch (UnknownHostException e) {
			fail();
		}
	}
	
	@Test
	public void testBuildPortNegative() {
		try {
			
			HttpScanDaemon object = (HttpScanDaemon) new HttpScanDaemonBuilder().withPort(-1).build();
			fail("Exception was expected due to negative port.");
		} catch (UnknownHostException e) {
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			// OK
		}
	}
	
	@Test
	public void testBuildPort1023() {
		try {
			HttpScanDaemon object = (HttpScanDaemon) new HttpScanDaemonBuilder().withPort(1023).build();
			fail("Exception was expected due to negative port.");
		} catch (UnknownHostException e) {
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			// OK
		}
	}
	
	@Test
	public void testBuildPort1024() {
		try {
			//HttpScanDaemon object = new HttpScanDaemon.Builder().withPort(1024).build();
			// OK
		} catch (Exception e) {
			fail("Unexpected exception");
		} 
	}
	*/

}
