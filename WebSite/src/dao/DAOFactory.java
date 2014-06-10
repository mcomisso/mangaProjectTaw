package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class DAOFactory {
	// Constants ----------------------------------------------------------------------------------

    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_DRIVER = "driver";
    private static final String PROPERTY_USERNAME = "********";
    private static final String PROPERTY_PASSWORD = "********";

    // Actions ------------------------------------------------------------------------------------
    /**
     * Ritorna una nuova istanza DAOFactory per il database di nome dato.
     * @param name Il nome del database per il quale ritornare l'istanza.
     * @return Una nuova istanza di DAOFactory per il databese di nome dato.
     * @throws DAOConfigurationException Se il nome del database è null, se il file delle proprietà
     * è mancante nel classpath o non può essere caricato, è mancate una proprietà nel file delle proprietà,
     * se il driver non può essere caricato o la datasource non può essere trovata.
     */
    public static DAOFactory getInstance(String name) throws DAOConfigurationException {
        if (name == null) {
            throw new DAOConfigurationException("Il nome del Database è null.");
        }

        DAOProperties properties = new DAOProperties(name);
        String url = properties.getProperty(PROPERTY_URL, true);
        String driverClassName = properties.getProperty(PROPERTY_DRIVER, false);
        String password = properties.getProperty(PROPERTY_PASSWORD, false);
        String username = properties.getProperty(PROPERTY_USERNAME, password != null);
        DAOFactory instance;

        // If driver is specified, then load it to let it register itself with DriverManager.
        if (driverClassName != null) {
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new DAOConfigurationException(
                    "La classe driver '" + driverClassName + "' è mancante nel classpath.", e);
            }
            instance = new DriverManagerDAOFactory(url, username, password);
        }

        // Else assume URL as DataSource URL and lookup it in the JNDI.
        else {
            DataSource dataSource;
            try {
                dataSource = (DataSource) new InitialContext().lookup(url);
            } catch (NamingException e) {
                throw new DAOConfigurationException(
                    "DataSource '" + url + "' è mancante in JNDI.", e);
            }
            if (username != null) {
                instance = new DataSourceWithLoginDAOFactory(dataSource, username, password);
            } else {
                instance = new DataSourceDAOFactory(dataSource);
            }
        }

        return instance;
    }
    
    /**
    * Returns a connection to the database. Package private so that it can be used inside the DAO
    * package only.
    * @return A connection to the database.
    * @throws SQLException If acquiring the connection fails.
    */
   abstract Connection getConnection() throws SQLException;

   // DAO implementation getters -----------------------------------------------------------------

   /**
    * Returns the User DAO associated with the current DAOFactory.
    * @return The User DAO associated with the current DAOFactory.
    */
   public MangaDAO getMangaDAO() {
       return new MangaDAOJDBC(this);
   }
   
   public UserDAO getUserDAO() {
       return new UserDAOJDBC(this);
   }
   
   public UserSessionDAO getUserSessionDAO() {
       return new UserSessionDAOJDBC(this);
   }
   public User_MangaDAO getUser_MangaDAO() {
       return new User_MangaDAOJDBC(this);
   }

   // You can add more DAO implementation getters here.
}
//Default DAOFactory implementations -------------------------------------------------------------

/**
* Il DriverManager DAOFactory.
*/
class DriverManagerDAOFactory extends DAOFactory {
 private String url;
 private String username;
 private String password;

 DriverManagerDAOFactory(String url, String username, String password) {
     this.url = url;
     this.username = username;
     this.password = password;
 }

 @Override
 Connection getConnection() throws SQLException {
     return DriverManager.getConnection(url, username, password);
 }
}

/**
 * DataSource DAOFactory.
 */
class DataSourceDAOFactory extends DAOFactory {
    private DataSource dataSource;

    DataSourceDAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

/**
 * DataSource-with-Login  DAOFactory.
 */
class DataSourceWithLoginDAOFactory extends DAOFactory {
    private DataSource dataSource;
    private String username;
    private String password;

    DataSourceWithLoginDAOFactory(DataSource dataSource, String username, String password) {
        this.dataSource = dataSource;
        this.username = username;
        this.password = password;
    }

    @Override
    Connection getConnection() throws SQLException {
        return dataSource.getConnection(username, password);
    }
}
