package dao;

import static dao.DAOUtil.close;
import static dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Updates;

public class UpdatesDAOJDBC implements UpdatesDAO {

    // Constants ----------------------------------------------------------------------------------

    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM Updates WHERE id = ?";
    
    private static final String SQL_LIST_ORDER_BY_ID =
        "SELECT * FROM Updates ORDER BY id";
    
    private static final String SQL_LAST =
        "SELECT * FROM Updates WHERE id = (SELECT MAX(id) FROM Updates)";
    
    private static final String SQL_INSERT =
        "INSERT INTO Updates (runDate) VALUES (?)";
    
    private static final String SQL_UPDATE =
        "UPDATE Updates SET runDate = ? WHERE id = ?";
    
    private static final String SQL_DELETE =
        "DELETE FROM Updates WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

 // Constructors -------------------------------------------------------------------------------

    /**
     * Costruttore Updates DAO per il DAOFactory passato. Package private quindi pu˜ essere costruito solo all'interno
     * del DAO package.
     * @param daoFactory Il DAOFactory per il quale costruire il manga DAO.
     */
    UpdatesDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------

	@Override
	public Updates find(Long id) throws DAOException {
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
    private Updates find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Updates update = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	update = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return update;
    }
	
	@Override
	public List<Updates> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Updates> updates = new ArrayList<Updates>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	updates.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return updates;
	}

	@Override
	public Updates last() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Updates update = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_LAST);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	update = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return update;
	}

	@Override
	public void create(Updates update) throws DAOException {
		if (update.getId() != null) {
            throw new IllegalArgumentException("Update is already created, the user ID is not null.");
        }

        Object[] values = {
        	update.getRunDate()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating update failed, no rows affected.");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                update.setId(generatedKeys.getLong(1));
            } else {
                throw new DAOException("Creating update failed, no generated key obtained.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, generatedKeys);
        }
	}

	@Override
	public void update(Updates update) throws IllegalArgumentException, DAOException {
	if (update.getId() == null) {
        throw new IllegalArgumentException("update is not created yet, the user ID is null.");
    }

    Object[] values = {
    	update.getRunDate(),
    	update.getId()
    };

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    try {
        connection = daoFactory.getConnection();
        preparedStatement = prepareStatement(connection, SQL_UPDATE, false, values);
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
            throw new DAOException("Updating update failed, no rows affected.");
        }
    } catch (SQLException e) {
        throw new DAOException(e);
    } finally {
        close(connection, preparedStatement);
    }
	}

	@Override
	public void delete(Updates update) throws DAOException {
		Object[] values = { 
				update.getId()
	        };

	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        try {
	            connection = daoFactory.getConnection();
	            preparedStatement = prepareStatement(connection, SQL_DELETE, false, values);
	            int affectedRows = preparedStatement.executeUpdate();
	            if (affectedRows == 0) {
	                throw new DAOException("Deleting update failed, no rows affected.");
	            } else {
	            	update.setId(null);
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
    private static Updates map(ResultSet resultSet) throws SQLException {
    	Updates update = new Updates();
    	update.setId(resultSet.getLong("id"));
    	update.setRunDate(resultSet.getDouble("runDate"));
        return update;
    }
}
