package dao;

import static dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import model.Manga;

public class MangaDAOJDBC implements MangaDAO {
	
    // Constants ----------------------------------------------------------------------------------

    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM Manga WHERE IDManga = ?";
    
    private static final String SQL_LIST_ORDER_BY_ALIAS =
        "SELECT * FROM Manga ORDER BY Alias";
    
    private static final String SQL_LIST_LAST_20 =
        "SELECT * FROM Manga ORDER BY LastDate DESC LIMIT 20";
    
    private static final String SQL_INSERT =
        "INSERT INTO Manga (IDManga, Alias, IMG, LastDate, Status, Title, Hits, Ch_Len, Lang) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE =
        "UPDATE Manga SET Alias = ?, IMG = ?, LastDate = ?, Status = ?, Title = ?, Hits = ?, Ch_Len = ?, Lang = ?  WHERE IDManga = ?";
    
    private static final String SQL_DELETE =
        "DELETE FROM Manga WHERE IDManga = ?";
    

    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Costruttore Manga DAO per il DAOFactory passato. Package private quindi può essere costruito solo all'interno
     * del DAO package.
     * @param daoFactory Il DAOFactory per il quale costruire il manga DAO.
     */
    MangaDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------

	
	
	@Override
	public Manga find(String IDManga) throws DAOException {
		 return find(SQL_FIND_BY_ID, IDManga);
	}
	
    /**
     * TRADURRE
     * Returns the user from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Manga find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Manga mangas = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	mangas = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return mangas;
    }

	@Override
	public List<Manga> list() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Manga> mangas = new ArrayList<Manga>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_LIST_ORDER_BY_ALIAS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	mangas.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return mangas;
	}

	@Override
	public List<Manga> listlast20() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Manga> mangas = new ArrayList<Manga>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_LIST_LAST_20);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	mangas.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return mangas;
	}

	@Override
	public void create(Manga manga) throws DAOException {
    
        Object[] values = {
        		manga.getIDManga(),
        		manga.getAlias(),
        		manga.getImage(),
        		manga.getLastDate(),
        		manga.getStatus(),
        		manga.getTitle(),
        		manga.getHits(),
        		manga.getChLen(),
        		manga.getLang(),
        		
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_INSERT, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creazione Manga fallita, nessuna riga modifcata.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }
	}

	@Override
	public void update(Manga manga) throws IllegalArgumentException,
			DAOException {
        if (manga.getIDManga() == null) {
            throw new IllegalArgumentException("Manga non ancora creato, l'id ID è null.");
        }

        Object[] values = {
        		manga.getAlias(),
        		manga.getImage(),
        		manga.getLastDate(),
        		manga.getStatus(),
        		manga.getTitle(),
        		manga.getHits(),
        		manga.getChLen(),
        		manga.getLang(),
        		manga.getIDManga(),
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Aggiornamento manga fallito, nessuna riga modificata.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }

	}

	@Override
	public void delete(Manga manga) throws DAOException {
        Object[] values = { 
        		manga.getIDManga()
            };

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = daoFactory.getConnection();
                preparedStatement = prepareStatement(connection, SQL_DELETE, false, values);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Eliminazione manga fallito, nessuna riga modificata.");
                } else {
                	manga.setIDManga(null);
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            } finally {
                close(connection, preparedStatement);
            }

	}
	// Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an User.
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static Manga map(ResultSet resultSet) throws SQLException {
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

}
