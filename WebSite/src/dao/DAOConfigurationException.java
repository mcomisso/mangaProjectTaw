package dao;

public class DAOConfigurationException extends RuntimeException {
	 // Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Costruttore con messaggio.
     * @param message Messaggio di DAOConfigurationException.
     */
    public DAOConfigurationException(String message) {
        super(message);
    }

    /**
     * Costruttore con causa.
     * @param cause Causa della DAOConfigurationException.
     */
    public DAOConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore con messaggio e causa.
     * @param message Messaggio di DAOConfigurationException.
     * @param cause Causa della DAOConfigurationException.
     */
    public DAOConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
