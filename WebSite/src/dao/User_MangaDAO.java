package dao;

import java.util.List;

import model.Manga;
import model.User;
import model.User_Manga;

public interface User_MangaDAO {
    
	/**
	 * Ritorna la connessione tra utente e manga con id passato, altrimenti null.
	 * @param id L'id della connessione tra utente e manga cercata.
	 * @return La connessione tra utente e manga con id passato, altrimenti null.
	 * @throws DAOException Se qualcosa va male a livello DB.
	 */
	public User_Manga find(String id) throws DAOException;
	
	/**
	 * Ritorna una lista di tutti i manga collegati ad un utente. La lista non e' mai nulla, e' vuota se l'utente non ha Manga associati.
	 * @param user L'utente per il quale cercare i manga.
	 * @return
	 * @throws DAOException
	 */
	public List<Manga> listMangaOfUser(User user) throws DAOException;

	/**
	 * Ritorna una lista composta dai primi 20 manga dell'utente, ordinati dal piu recente al piu' vecchio.
	 * @param user L'utente per il quale cercare i manga
	 * @return
	 * @throws DAOException
	 */
	public List<Manga> listLast20MangaOfUser(User user) throws DAOException;

	/**
	 * Ricerca tutti gli utenti associati al manga passato.
	 * @param manga Il manga per il quale eseguire la ricerca.
	 * @return La lista di tutti gli utenti associati al manga passato.
	 * @throws DAOException
	 */
	public List<User> findUsersByManga(Manga manga) throws DAOException;

	/**
     * Crea la connesione tra utente e manga passata nel DB. L'id della connessione deve essere null, altrimenti sara' lanciata 
     * IllegalArgumentException. Dopo la creazione, il DAO settera' l'id ottenuto nell'connessione.
     * @param userManga La connessione tra utente e manga da creare nel database.
     * @throws IllegalArgumentException Se l'id della connessione non e' null, oppure l'utente o il manga associato non sono prensenti in DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void create(User_Manga userManga) throws IllegalArgumentException, DAOException;
	
	/**
	 * Cancella l'associazione tra un utente ed un manga
	 * @param userManga da cancellare dal DB
	 * @throws DAOException
	 */
	public void delete(User_Manga userManga) throws DAOException;
	
	/**
	 * Cancella l'associazione tra un utente ed un manga, partendo da parametri separati di user e manga
	 * @param Utente e Manga da cancellare dalla tabella di associazione
	 * @throws DAOException
	 */
	public void deleteWithUserAndMangaID(User user, Manga manga) throws DAOException;
	
	/**
     * Aggiorna la connessione tra utente e manga nel DB. L'ID della connessione non deve essere null, altrimenti sara' lanciata 
     * IllegalArgumentException.
     * @param userManga La connessione tra utente e manga da aggiornare nel database.
     * @throws IllegalArgumentException Se l'id della connessione e' null, oppure l'utente o il manga associato non sono prensenti in DB.
     * @throws DAOException Se qualcosa va male a livello DB.
     */
    public void update(User_Manga userManga) throws IllegalArgumentException, DAOException;
	
}
