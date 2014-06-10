package dao;

import static dao.DAOUtil.close;
import static dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Manga;
import model.User;
import model.User_Manga;

public class User_MangaDAOJDBC implements User_MangaDAO {

	// Constants ----------------------------------------------------------------------------------
	
	/*
	 * private Long id;
	 * private Manga manga;
	 * private User user;
	 */
	 private static final String SQL_FIND_BY_ID =
		    "SELECT * FROM User_Manga WHERE id = ?";
	 
	private static final String SQL_MANGA_LIST_ORDER_BY_LASTDATE =
			"SELECT m.* FROM Manga AS m JOIN User_Manga AS um ON um.mangaID = m.IDManga WHERE um.userID = ? ORDER BY LastDate DESC";
	
    private static final String SQL_MANGA_LIST_LAST_20_ORDER_BY_LASTDATE =
            "SELECT m.* FROM Manga AS m JOIN User_Manga AS um ON um.mangaID = m.IDManga WHERE um.userID = ? ORDER BY LastDate DESC LIMIT 20";
	 
	private static final String SQL_USER_LIST_ORDER_BY_FIRSTNAME =
			"SELECT u.* FROM User AS u JOIN User_Manga AS um ON um.userID = u.id WHERE um.mangaID = ? ORDER BY firstname ASC";
	
	private static final String SQL_INSERT =
	        "INSERT INTO User_Manga (userID, mangaID) VALUES (?, ?)";
	
	private static final String SQL_UPDATE =
	        "UPDATE User_Manga SET userID = ?, mangaID = ? WHERE id = ?";
	
	 private static final String SQL_DELETE =
		        "DELETE FROM User_Manga WHERE id = ?";
	 
	 private static final String SQL_DELETEWITHUSERMANGA = 
			 "DELETE FROM User_Manga WHERE userID = ? AND mangaID = ?";
	
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------
    
    /**
     * Costruttore User DAO per il DAOFactory passato. Package private quindi puo' essere costruito solo all'interno
     * del DAO package.
     * @param daoFactory Il DAOFactory per il quale costruire il User DAO.
     */
    User_MangaDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
	
	
	@Override
	public User_Manga find(String id) throws DAOException {
		return find(SQL_FIND_BY_ID, id);
	}
	
	/**
     * TRADURRE
     * Returns the user from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private User_Manga find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User_Manga usermanga = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	usermanga = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return usermanga;
    }

	@Override
	public List<Manga> listMangaOfUser(User user) throws DAOException {
		// Controllo se l'utente esiste in DB
		CheckUserInDB(user);
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Manga> mangas = new ArrayList<Manga>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_MANGA_LIST_ORDER_BY_LASTDATE, false, user.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	mangas.add(mapManga(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return mangas;
	}

	@Override
	public List<Manga> listLast20MangaOfUser(User user) throws DAOException {
		// Controllo se l'utente esiste in DB
		CheckUserInDB(user);
				
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Manga> mangas = new ArrayList<Manga>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_MANGA_LIST_LAST_20_ORDER_BY_LASTDATE, false, user.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	mangas.add(mapManga(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return mangas;
	}

	@Override
	public List<User> findUsersByManga(Manga manga) throws DAOException {
		CheckMangaInDB(manga);
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_USER_LIST_ORDER_BY_FIRSTNAME, false, manga.getIDManga());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return users;
	}

	@Override
	public void create(User_Manga userManga) throws IllegalArgumentException,
			DAOException {
		
		// Controllo se l'utente ed il manga associati a userManga esistono in DB
		CheckUserInDB(userManga.getUser());
		CheckMangaInDB(userManga.getManga());
		
		if (userManga.getId() != null) {
            throw new IllegalArgumentException("userManga esiste gi�', l'id di userManga non e' null.");
        }

        Object[] values = {
        	userManga.getUser().getId(),
       		userManga.getManga().getIDManga()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creazione userManga fallita, nessuna riga modificata.");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
            	userManga.setId(generatedKeys.getLong(1));
            } else {
                throw new DAOException("Creazione userManga fallita, nessuna chiave generata ottenuta.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, generatedKeys);
        }


	}

	@Override
	public void delete(User_Manga userManga) throws DAOException {
		Object[] values = { 
				userManga.getId()
	    };

	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    try {
	    	connection = daoFactory.getConnection();
	        preparedStatement = prepareStatement(connection, SQL_DELETE, false, values);
	        int affectedRows = preparedStatement.executeUpdate();
	        if (affectedRows == 0) {
	        	throw new DAOException("Eliminazione userManga fallita, nessuna riga modificata.");
	        	} else {
	        		userManga.setId(null);
	        	}
	        } catch (SQLException e) {
	            throw new DAOException(e);
	        } finally {
	            close(connection, preparedStatement);
	        }
	}
	
	@Override
	public void deleteWithUserAndMangaID(User user, Manga manga) throws DAOException {
		Object[] values = {
				user.getId(),
				manga.getIDManga()
		};
		
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		try{
			connection = daoFactory.getConnection();
			prepareStatement = prepareStatement(connection, SQL_DELETEWITHUSERMANGA, false, values);
			int affectedRows = prepareStatement.executeUpdate();
			if (affectedRows == 0) {
	        	throw new DAOException("Eliminazione userManga fallita, nessuna riga modificata.");
	        	} else {
	        		//DONE
	        	}
	        } catch (SQLException e) {
	            throw new DAOException(e);
	        } finally {
	            close(connection, prepareStatement);
	        }
	}

	@Override
	public void update(User_Manga userManga) throws IllegalArgumentException,
			DAOException {
		
		// Controllo se l'utente ed il manga associati a userManga esistono in DB
		CheckUserInDB(userManga.getUser());
		CheckMangaInDB(userManga.getManga());
		
		if (userManga.getId() == null) {
            throw new IllegalArgumentException("userManga non e' stato ancora creato, l'id e' null.");
        }

        Object[] values = {
        	userManga.getUser().getId(),
        	userManga.getManga().getIDManga(),
       		userManga.getId()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Aggiornamento fallito, nessuna riga modificata.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }

	}
	
	
    // Helpers ------------------------------------------------------------------------------------
    /**
     * TRADURRE
     * Map the current row of the given ResultSet to an User.
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static User_Manga map(ResultSet resultSet) throws SQLException {
    	
    	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
    	UserDAO userDAO = testTaw.getUserDAO();
    	User user = userDAO.find(resultSet.getLong("userID"));
    	MangaDAO mangaDAO = testTaw.getMangaDAO();
    	Manga manga = mangaDAO.find(resultSet.getString("mangaID"));
    
    	User_Manga usermanga = new User_Manga();
    	usermanga.setId(resultSet.getLong("id"));	
    	usermanga.setManga(manga);
    	usermanga.setUser(user);
    	
    	return usermanga;
    }
    
    /**
     * TRADURRE
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static Manga mapManga(ResultSet resultSet) throws SQLException {
    	Manga manga = new Manga();
        manga.setIDManga(resultSet.getString("IDManga"));
    	manga.setAlias(resultSet.getString("Alias"));
    	manga.setImage(resultSet.getString("IMG"));
    	manga.setLastDate(resultSet.getDouble("LastDate"));
    	manga.setStatus(resultSet.getInt("Status"));
    	manga.setTitle(resultSet.getString("Title"));
    	manga.setHits(resultSet.getInt("Hits"));
    	manga.setChLen(resultSet.getInt("Ch_Len"));
    	manga.setLang(resultSet.getInt("Lang"));
        return manga;
    }
    
    /**
     * Map the current row of the given ResultSet to an User.
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        return user;
    }
    
    private static void CheckUserInDB(User user){
    	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		UserDAO userDAO = testTaw.getUserDAO();
		if (userDAO.find(user.getId())==null) throw new IllegalArgumentException("L'utente presente in userManga non esiste in DB.");
		return;
    }
    
    private static void CheckMangaInDB(Manga manga){
    	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		if (mangaDAO.find(manga.getIDManga())==null) throw new IllegalArgumentException("Il manga presente in userManga non esiste in DB.");
		return;
    }

}
