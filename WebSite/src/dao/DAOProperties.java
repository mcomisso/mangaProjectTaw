package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOProperties {
	// Constants ----------------------------------------------------------------------------------

    private static final String PROPERTIES_FILE = "dao.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);
        System.out.print(classLoader.getResourceAsStream(PROPERTIES_FILE));
        if (propertiesFile == null) {
            throw new DAOConfigurationException(
                "Il file con le proprietà: '" + PROPERTIES_FILE + "' non è presente nel classpath.");
        }

        try {
            PROPERTIES.load(propertiesFile);
        } catch (IOException e) {
            throw new DAOConfigurationException(
                "Impossibile caricare il file '" + PROPERTIES_FILE + "'.", e);
        }
    }

    // Vars ---------------------------------------------------------------------------------------

    private String specificKey;
    
    // Constructors -------------------------------------------------------------------------------

    /**
     * Costuttore di un'istanza DAOProperties con la chiave specifica.
     * @param specificKey Chiave specifica.
     * @throws DAOConfigurationException Può lanciarla se il file proprietà è mancante o non può essere caricato.
     */
    public DAOProperties(String specificKey) throws DAOConfigurationException {
        this.specificKey = specificKey;
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Ritorna l'istanza per una specifica chiave associata, da la possibilità di indicare se è obbligatoria o meno.
     * @param key La chiave associata ad una specifica istanza.
     * @param mandatory Imposta se la stringa ritornata dev'essere null o vuota.
     * @return L'istanza DAOProperties specifica per la chiave data.
     * @throws DAOConfigurationException Scatta in caso sia stata segnata obbligatoria e si ha ritorno null o vuota.
     */
    public String getProperty(String key, boolean mandatory) throws DAOConfigurationException {
        String fullKey = specificKey + "." + key;
        String property = PROPERTIES.getProperty(fullKey);

        if (property == null || property.trim().length() == 0) {
            if (mandatory) {
                throw new DAOConfigurationException("La chiave '" + fullKey + "'"
                    + " è mancante nel file: '" + PROPERTIES_FILE + "'.");
            } else {
                // Make empty value null. Empty Strings are evil.
                property = null;
            }
        }

        return property;
    }
}
