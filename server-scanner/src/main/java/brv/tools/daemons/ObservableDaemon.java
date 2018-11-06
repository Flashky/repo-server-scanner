package brv.tools.daemons;

import java.util.List;
import java.util.Vector;

import brv.tools.listeners.ServerRemovedListener;
import brv.tools.listeners.ServerUpdatedListener;
import brv.tools.model.ScanResult;

/**
 * Demonio que implementa el patr�n listener para poder enviar notificaciones cuando ocurran los eventos a determinar por las subclases.
 * @author flash
 *
 */
public abstract class ObservableDaemon /*extends Thread*/ {
	
	private List<ServerUpdatedListener> serverUpdatedListeners = new Vector<>();
	private List<ServerRemovedListener> serverRemovedListeners = new Vector<>();
	
	/**
	 * A�ade un listener que escuchará las notificaciones enviadas cuando el escáner encuentre un nuevo servidor.
	 * @param listener
	 */
	public void addServerUpdatedListener(ServerUpdatedListener listener) {
		this.serverUpdatedListeners.add(listener);
	}
	
	
	/**
	 * A�ade un listener que escuchará las notificaciones enviadas cuando el escáner detecte que un servidor ha dejado de estar accesible.
	 * @param listener
	 */
	public void addServerRemovedListener(ServerRemovedListener listener) {
		this.serverRemovedListeners.add(listener);
	}
	
	/**
	 * Notifica a todos los listeners que se ha dejado de detectar un web server en la ip indicada.
	 * @param ip
	 */
	protected void notifyServerUpdatedListeners(ScanResult server) {
		for(ServerUpdatedListener listener : serverUpdatedListeners) {
			listener.serverUpdated(server);
		}
	}
	
	/**
	 * Notifica a todos los listeners que se ha detectado un web server en la ip indicada.
	 * @param ip
	 */
	protected void notifyServerRemovedListeners(ScanResult server) {
		for(ServerRemovedListener listener : serverRemovedListeners) {
			listener.serverRemoved(server);
		}
	}
}
