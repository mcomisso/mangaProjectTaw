package dao;

public class DAOException extends RuntimeException {
	 // Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Costruttore con  messaggio.
     * @param message il messaggio di DAOException.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Costruttore con causa.
     * @param cause Causa della DAOException.
     */
    public DAOException(Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore con messaggio e causa.
     * @param message il messaggio di DAOException.
     * @param cause Causa della DAOException.
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
