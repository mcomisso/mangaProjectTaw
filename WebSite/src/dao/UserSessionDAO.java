package dao;


import model.User;
import model.UserSession;

public interface UserSessionDAO {
    /**
     * Ritorna la sessione utente corrispondente al cookieId nel DB, altrimenti null.
     * @param cookieId L'id della sessione utente da cercare.
     * @return La sessione utente dal DB che corrisponde al cookieId passato, altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public UserSession find(String cookieId) throws DAOException;

    /**
     * Ritorna la sessione utente corrispondente all'utente DB, altrimenti null.
     * @param user L'utente per il quale cercare la sessione.
     * @return La sessione utente dal DB che corrisponde all'utente passato, altrimenti null.
     * @throws IllegalArgumentException Se l'utente associato a usersession non esiste in DB
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public UserSession find(User user) throws IllegalArgumentException, DAOException;
    
    /**
     * DESCRIVERE
     * @param usersession La sessione utente da creare nel database.
     * @throws IllegalArgumentException Se l'utente associato a usersession non esiste in DB
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void create(UserSession usersession) throws IllegalArgumentException, DAOException;

    /**
     * DESCRIVERE
     * @param usersession La sessione utente da aggiornare nel database.
     * @throws IllegalArgumentException Se l'ID della sessione utente e' null, o l'utente associato non e' presente in DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void update(UserSession usersession) throws IllegalArgumentException, DAOException;

    /**
     * Cancella l'utente passato dal DB. Dopo la cancellazione, il DAO settera' l'ID del utente passato a null.
     * @param user L'utente da cancellare nel database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void delete(UserSession usersession) throws DAOException;
}
