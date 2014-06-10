package dao;

import java.util.List;

import model.Updates;

public interface UpdatesDAO {
	/**
     * Ritorna l'update con id passato, altrimenti null.
     * @param id L'id dell'update ricercato.
     * @return L'update con id passato, altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public Updates find(Long id) throws DAOException;
    
    /**
     * Ritorna una lista di tutti gli update dal DB. La lista non è mai nulla, è vuota se il DB non contiene update.
     * @return una lista di tutti gli update dal DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public List<Updates> list() throws DAOException;

    /**
     * Ritorna l'ultimo update, altrimenti null.
     * @return L'update con id maggiore (quindi l'ultimo), altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public Updates last() throws DAOException;
    
    /**
     * Crea l'update passato nel DB. DECIDERE IMPLEMENTAZIONE
     * 
     * Create the given user in the database. The user ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given user.
     * @param update L'update da aggiungere al database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void create(Updates update) throws DAOException;
    /**
     * Aggiorna l'update passato nel DB. L'id non può essere null o sarà lanciata
     * IllegalArgumentException.
     * @param update L'update da aggiornare nel database.
     * @throws IllegalArgumentException Se l'id dell'update è null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void update(Updates update) throws IllegalArgumentException, DAOException;

    /**
     * Elimina da DB l'update passato. L'id sarà settato null
     * @param update L'update da candellare dal database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void delete(Updates update) throws DAOException;
}
