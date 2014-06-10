package dao;

import java.util.List;

import model.Manga;

public interface MangaDAO {
	// Actions ------------------------------------------------------------------------------------

    /**
     * Ritorna il manga dal corrispondente all'id passato, altrimenti null.
     * @param IDManga L'id del manga ricercato.
     * @return Il manga dal corrispondente all'id passato, altrimenti null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public Manga find(String IDManga) throws DAOException;

    /**
     * Ritorna una lista di tutti i manga dal DB. La lista non è mai nulla, è vuota se il DB non contiene Manga.
     * @return una lista di tutti i manga dal DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public List<Manga> list() throws DAOException;
    
    
    /**
     * Ritorna una lista degli ultimi 20 manga dal DB. La lista non è mai nulla, è vuota se il DB non contiene Manga.
     * @return una lista degli ultimi 20 manga dal DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public List<Manga> listlast20() throws DAOException;
    

    /**
     * Crea il manga passato nel DB. DECIDERE IMPLEMENTAZIONE
     * 
     * Create the given user in the database. The user ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given user.
     * @param manga Il manga da aggiornare database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void create(Manga manga) throws DAOException;

    /**
     * Aggiorna il manga passato nel DB. L'id non può essere null o sarà lanciata
     * IllegalArgumentException.
     * @param manga Il manga da aggiornare nel database.
     * @throws IllegalArgumentException Se l'id del manga è null.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void update(Manga manga) throws IllegalArgumentException, DAOException;

    /**
     * Elimina da DB il manga passato. L'id sarà settato null
     * @param manga Il manga da candellare dal database.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void delete(Manga manga) throws DAOException;

}
