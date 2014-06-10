package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import static dao.DAOUtil.*;

public class UserDAOJDBC implements UserDAO {
	
	// Constants ----------------------------------------------------------------------------------

    private static final String SQL_FIND_BY_ID =
        "SELECT id, email, firstname, lastname FROM User WHERE id = ?";
    
    private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD =
        "SELECT id, email, firstname, lastname FROM User WHERE email = ? AND password = MD5(?)";
    
    private static final String SQL_LIST_ORDER_BY_ID =
        "SELECT id, email, firstname, lastname FROM User ORDER BY id";
    
    private static final String SQL_INSERT =
        "INSERT INTO User (email, password, firstname, lastname) VALUES (?, MD5(?), ?, ?)";
    
    private static final String SQL_UPDATE =
        "UPDATE User SET email = ?, firstname = ?, lastname = ? WHERE id = ?";
    
    private static final String SQL_DELETE =
        "DELETE FROM User WHERE id = ?";
    
    private static final String SQL_EXIST_EMAIL =
        "SELECT id FROM User WHERE email = ?";
    
    private static final String SQL_CHANGE_PASSWORD =
        "UPDATE User SET password = MD5(?) WHERE id = ?";

    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------
    
    /**
     * Costruttore User DAO per il DAOFactory passato. Package private quindi puo' essere costruito solo all'interno
     * del DAO package.
     * @param daoFactory Il DAOFactory per il quale costruire il User DAO.
     */
    UserDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------

	@Override
	public User find(Long id) throws DAOException {
		return find(SQL_FIND_BY_ID, id);
	}

	@Override
	public User find(String email, String password) throws DAOException {
		return find(SQL_FIND_BY_EMAIL_AND_PASSWORD, email, password);
	}
	
    /**
     * TRADURRE
     * Returns the user from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private User find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, sql, false, values);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return user;
    }
	

	@Override
	public List<User> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }

        return users;
	}

	@Override
	public void create(User user) throws DAOException {
		if (user.getId() != null) {
            throw new IllegalArgumentException("User is already created, the user ID is not null.");
        }

        Object[] values = {
            user.getEmail(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            } else {
                throw new DAOException("Creating user failed, no generated key obtained.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement, generatedKeys);
        }
	}

	@Override
	public void update(User user) throws IllegalArgumentException, DAOException {
		if (user.getId() == null) {
            throw new IllegalArgumentException("User is not created yet, the user ID is null.");
        }

        Object[] values = {
            user.getEmail(),
            user.getFirstname(),
            user.getLastname(),
            user.getId()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_UPDATE, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(connection, preparedStatement);
        }
	}

	@Override
	public void delete(User user) throws DAOException {
		Object[] values = { 
	            user.getId()
	        };

	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        try {
	            connection = daoFactory.getConnection();
	            preparedStatement = prepareStatement(connection, SQL_DELETE, false, values);
	            int affectedRows = preparedStatement.executeUpdate();
	            if (affectedRows == 0) {
	                throw new DAOException("Deleting user failed, no rows affected.");
	            } else {
	                user.setId(null);
	            }
	        } catch (SQLException e) {
	            throw new DAOException(e);
	        } finally {
	            close(connection, preparedStatement);
	        }
	}

	@Override
	public boolean existEmail(String email) throws DAOException {
		Object[] values = { 
	            email
	        };

	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        boolean exist = false;

	        try {
	            connection = daoFactory.getConnection();
	            preparedStatement = prepareStatement(connection, SQL_EXIST_EMAIL, false, values);
	            resultSet = preparedStatement.executeQuery();
	            exist = resultSet.next();
	        } catch (SQLException e) {
	            throw new DAOException(e);
	        } finally {
	            close(connection, preparedStatement, resultSet);
	        }

	        return exist;
	}

	@Override
	public void changePassword(User user) throws DAOException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User is not created yet, the user ID is null.");
        }

        Object[] values = {
            user.getPassword(),
            user.getId()
        };

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = prepareStatement(connection, SQL_CHANGE_PASSWORD, false, values);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Changing password failed, no rows affected.");
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
    private static User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        return user;
    }

}
