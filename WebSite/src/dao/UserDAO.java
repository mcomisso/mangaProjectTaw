package dao;

import java.util.List;

import model.User;

public interface UserDAO {
	
	// Actions ------------------------------------------------------------------------------------

    /**
     * Ritorna l'utente corrispondente all'id nel DB, altrimenti null.
     * @param id L'id dell'utente da cercare.
     * @return L'utente dal DB che corrisponde all'ID passato, altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public User find(Long id) throws DAOException;

    /**
     * Ritorna l'utente corrispondente alla email ed alla password passati nel DB, altrimenti null.
     * @param email La mail dell'utente da cercare.
     * @param password La password dell'utente da cercare.
     * @return L'utente dal DB che corrisponde alla email ed alla password passati, altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public User find(String email, String password) throws DAOException;

    /**
     * Ritorna una lista di tutti gli utenti nel DB ordinati per ID, la lista non è mai nulla,
     * e' vuota quando il DB non contiene nessun utente
     * @return Una lista di tutti gli utenti nel DB ordinati per ID.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public List<User> list() throws DAOException;

    /**
     * Crea l'utente passato nel DB. L'ID utente deve essere null, altrimenti sara' lanciata 
     * IllegalArgumentException. Dopo la creazione, il DAO settera' l'ID ottenuto nell'utente.
     * @param user L'utente da creare nel database.
     * @throws IllegalArgumentException Se l'ID dell'utente non e' null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void create(User user) throws IllegalArgumentException, DAOException;

    /**
     * Aggiorna l'utente passato nel DB. L'ID utente non deve essere null, altrimenti sara' lanciata 
     * IllegalArgumentException. Note: la password NON sara' aggiornata. Usare changePassword().
     * @param user L'utente da aggiornare nel database.
     * @throws IllegalArgumentException Se l'ID dell'utente e' null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void update(User user) throws IllegalArgumentException, DAOException;

    /**
     * Cancella l'utente passato dal DB. Dopo la cancellazione, il DAO settera' l'ID del utente passato a null.
     * @param user L'utente da cancellare nel database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void delete(User user) throws DAOException;

    /**
     * Ritorna true se la mail passata e' presente nel DB
     * @param email L'indirizzo email da controllare nel DB.
     * @return True Se la mail passata e' presente nel database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public boolean existEmail(String email) throws DAOException;
    
    /**
     * Cambia la passwrod dell'utente passato. L'ID utente non deve essere null, altrimenti 
     * verra' lanciata IllegalArgumentException.
     * @param user L'utente al quale cambiare la password.
     * @throws IllegalArgumentException Se l'ID utente e' null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void changePassword(User user) throws DAOException;

}
