package brv.tools.daemons.exceptions;

public class ScanDaemonNotFoundException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7341222004183503740L;
	
	/**
	 * Default constructor.
	 * <p>
	 * The default constructor will add a default message to the superclass.
	 * </p>
	 */
	public ScanDaemonNotFoundException() {
		super("ScanDaemon has not been found.");
	}
	
	/**
	 * Constructor using daemon id.
	 * <p>
	 * This constructor will add a defult message with additional information about the daemon.
	 * </p>
	 * @param daemonId - the daemon id to be included at the detail message 
	 * 					(which is saved for later retrieval
     *         			by the {@link #getMessage()} method).
	 */
	public ScanDaemonNotFoundException(long daemonId) {
		super("ScanDaemon (id="+daemonId+") has not been found.");
	}
	
	/**
	 * Constructor using message.
	 * <p>
	 * This constructor allows to customize the message, sending anything you
	 * </p>
     * @param  message - the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
	 */
	public ScanDaemonNotFoundException(String message) {
		super(message);
	}
	
	/**
	 * Constructor using daemon id.
	 * <p>
	 * This constructor will add a defult message with additional information about the daemon.
	 * </p>
	 * @param daemonId - the daemon id to be included at the message.
     * @param  cause - the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
	 */
	public ScanDaemonNotFoundException(long daemonId, Throwable cause) {
		super("ScanDaemon (id="+daemonId+") has not been found.", cause);
	}
	
	/**
	 * Constructor using message.
	 * <p>
	 * This constructor allows to customize the message, sending anything you
	 * </p>
     * @param  message - the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause - the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
	 */
	public ScanDaemonNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
