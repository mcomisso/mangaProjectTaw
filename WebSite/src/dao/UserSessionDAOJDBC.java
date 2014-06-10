
package dao;

import static dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import model.UserSession;

public class UserSessionDAOJDBC implements UserSessionDAO {

	// Constants ----------------------------------------------------------------------------------

    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM UserSession WHERE cookieId = ?";
    
    private static final String SQL_FIND_BY_USER =
            "SELECT * FROM UserSession WHERE UserID = ?";
    
    private static final String SQL_INSERT =
            "INSERT INTO UserSession (cookieId, UserID, creationDate, lastVisit, hits) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE =
            "UPDATE UserSession SET  UserID = ?, creationDate = ?, lastVisit = ?, hits = ? WHERE cookieId = ?";
    
    private static final String SQL_DELETE =
            "DELETE FROM UserSession WHERE cookieId = ?";
      
	
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------
    /**
     * Costruttore User DAO per il DAOFactory passato. Package private quindi puo' essere costruito solo all'interno
     * del DAO package.
     * @param daoFactory Il DAOFactory per il quale costruire il User DAO.
     */
    UserSessionDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

	@Override
	public UserSession find(String cookieId) throws DAOException {
		return find(SQL_FIND_BY_ID, cookieId);
	}
	
	@Override
	public UserSession find(User user) throws IllegalArgumentException, DAOException {
		// Controllo se l'utente  esiste in DB
		CheckUserInDB(user);
		
		return find(SQL_FIND_BY_USER, user.getId());
	}
    
	/**
     * TRADURRE
     * Returns the user from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private UserSession find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserSession usersession = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	usersession = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return usersession;
    }

	@Override
	public void create(UserSession usersession)
			throws IllegalArgumentException, DAOException {
		// Controllo se l'utente associato a usersession esiste in DB
		CheckUserInDB(usersession.getUser());

		Object[] values = {
				usersession.getCookieID(),
				usersession.getUser().getId(),
				toSqlDate(usersession.getCreationDate()),
				toSqlDate(usersession.getLastVisit()),
				usersession.getHits()
				};
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_INSERT, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creazione UserSession fallita, nessuna riga modifcata.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }
	}

	@Override
	public void update(UserSession usersession)
			throws IllegalArgumentException, DAOException {
		
		// Controllo se l'utente associato a usersession esiste in DB
		CheckUserInDB(usersession.getUser());
		
		if (usersession.getCookieID() == null) {
            throw new IllegalArgumentException("User Session non ancora creato, cookieId è null.");
        }
        Object[] values = {
        		
        		usersession.getUser().getId(),
        		usersession.getCreationDate(),
        		usersession.getLastVisit(),
        		usersession.getHits(),
        		usersession.getCookieID()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Aggiornamento User session fallito, nessuna riga modificata.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }
}

	@Override
	public void delete(UserSession usersession) throws DAOException {
        Object[] values = { 
        		usersession.getCookieID()
            };

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = daoFactory.getConnection();
                preparedStatement = prepareStatement(connection, SQL_DELETE, false, values);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Eliminazione sessione utente fallita, nessuna riga modificata.");
                } else {
                	usersession.setCookieID(null);
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            } finally {
                close(connection, preparedStatement);
            }
	}
	
	
    /**
     * TRADURRE
     * Map the current row of the given ResultSet to an User.
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static UserSession map(ResultSet resultSet) throws SQLException {
    	
    	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
    	UserDAO userDAO = testTaw.getUserDAO();
    	
    	UserSession usersession = new UserSession();
    	usersession.setCookieID(resultSet.getString("cookieId"));
    	usersession.setCreationDate(resultSet.getDate("creationDate"));
    	usersession.setHits(resultSet.getInt("hits"));
    	usersession.setLastVisit(resultSet.getDate("lastVisit"));
    	
    	User user = userDAO.find(resultSet.getLong("UserID"));
    	usersession.setUser(user);
    	
    	return usersession;
    }
    
    private static void CheckUserInDB(User user){
    	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		UserDAO userDAO = testTaw.getUserDAO();
		if (userDAO.find(user.getId())==null) throw new IllegalArgumentException("L'utente presente in userManga non esiste in DB.");
		return;
    }

}
