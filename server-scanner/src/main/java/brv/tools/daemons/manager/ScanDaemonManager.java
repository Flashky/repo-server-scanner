package brv.tools.daemons.manager;

import java.util.List;

import brv.tools.daemons.ScanDaemon;
import brv.tools.daemons.exceptions.ScanDaemonNotFoundException;
import brv.tools.model.ScanDaemonConfiguration;

/**
 * Defines the contract for a container-manager of {@link brv.tools.daemons.ScanDaemon ScanDaemon} objects.
 * <p>
 * The manager adds an abstraction layer which allows to manage and perform actions on ScanDaemons without
 * needing to know anything about them, excepting the id.
 * </p>
 * 
 * <strong>Daemon management</strong>
 * <p>
 * The {@link ScanDaemonManager#add(ScanDaemon) add(ScanDaemon)}, {@link ScanDaemonManager#remove(long) add(long)} and {@link ScanDaemonManager#remove() remove()}
 * allows the client to add or remove daemons to the manager. Whenever a daemon is added, it should also automatically start its execution; if it is removed, it should also automatically stop its execution.
 * </p>
 * 
 * <strong>Daemon actions management</strong>
 * <p>
 * Actions such as {@link brv.tools.daemons.ScanDaemon#start() start()}, {@link brv.tools.daemons.ScanDaemon#stop() stop()} and {@link brv.tools.daemons.ScanDaemon#interrupt() interrupt()} 
 * can be performed at the manager in two different ways:
 * </p>
 * <ul>	
 * 	<li><strong>Individually:</strong> to the daemon which has the specified <code>daemonId</code> obtainable from {@link brv.tools.daemons.ScanDaemon#getId() ScanDaemon.getId()}.</li>
 * 	<li><strong>Massively:</strong> applying the same action to any managed daemons.
 * </ul>
 * 
 * <strong>Information about managed daemons</strong>
 * <p>
 * Finally, the {@link ScanDaemonManager#findAllDaemons() findAllDaemons} should allow to generate a list of {@link brv.tools.model#ScanDaemonConfiguration ScanDaemonConfiguration} for informative uses. 
 * </p>
 * @author flash
 *
 */
public interface ScanDaemonManager {
	
	/**
	 * Adds a ScanDaemon to the manager.
	 * @param daemon - the daemon to be added to the manager.  
	 * @return <code>true</code> - if a daemon has been added and started.
	 */
	boolean add(ScanDaemon daemon);
	
	/**
	 * Resumes the execution of a daemon.
	 * @param daemonId - the unique id which identifies the daemon to be resumed.
	 * @return 	<code>true</code> - if the specified daemon has started running.<br>
	 * 			<code>false</code> - if the specified daemon was already running and no action was taken.
	 * @throws ScanDaemonNotFoundException if no daemon has been found.
	 */
	boolean resume(long daemonId) throws ScanDaemonNotFoundException;
	
	/**
	 * Soft-stops the execution of a daemon.
	 * @param daemonId - the unique id which identifies the daemon to be stopped.
	 * @return <code>true</code> - if a daemon has been stopped.
	 * @throws ScanDaemonNotFoundException if no daemon has been found.
	 */
	boolean stop(long daemonId) throws ScanDaemonNotFoundException;
	
	/**
	 * Hard-stops the execution of a daemon.
	 * @param daemonId - the unique id which identifies the daemon to be interrupted.
	 * @return <code>true</code> - if a daemon has been interrupted.
	 * @throws ScanDaemonNotFoundException if no daemon has been found.
	 */
	boolean interrupt(long daemonId) throws ScanDaemonNotFoundException;
	
	/**
	 * Removes a daemon from the manager.
	 * <p>
	 * Any removed daemons will be also stopped.
	 * </p>
	 * @param daemonId - the unique id which identifies the daemon to be removed.
	 * @return <code>true</code> - if a daemon has been removed and stopped.
	 * @throws ScanDaemonNotFoundException if no daemon has been found.
	 */
	boolean remove(long daemonId) throws ScanDaemonNotFoundException;
	
	/**
	 * Resumes the execution of any stopped daemons.
	 * @return <code>true</code> - if any of the daemons have been resumed.
	 */
	boolean resume();
	
	/**
	 * Stops the execution of any running daemons.
	 * @return <code>true</code> - if any of the daemons have been stopped.
	 */
	boolean stop();
	
	/***
	 * Hard-stops the execution of any running daemons.
	 * @return <code>true</code> - if any of the daemons have been stopped. 
	 */
	boolean interrupt();
	
	/**
	 * Deletes any existing daemons.
	 * <p>
	 * Any removed daemons will be also stopped.
	 * </p>
	 * @return 	<code>true</code> - if any of the removed daemons have also been stopped.<br>
	 * 			<code>false</code> - if there were no daemons to be stopped, regardless of any daemon being removed or not.
	 */
	boolean remove();
	
	/**
	 * Returns a list with the configuration of all the daemons added to the manager.
	 * @return
	 */
	List<ScanDaemonConfiguration> findAllDaemons();

}
