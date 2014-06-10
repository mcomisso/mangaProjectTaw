package webCrawler;

import java.sql.*;

public class DB {
	Statement query;
	PreparedStatement prepQuery;
	ResultSet results;
	Connection connection;
	boolean connectionExists = false;
	
	public DB() { }
	
	public Connection dbConnection() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		if(connectionExists == false)
		{
			Connection connection = DriverManager.getConnection("jdbc:mysql://54.187.6.19:3306/testTaw?user=taw&password=some_pass");
			connectionExists = true;
			return connection;
		}
		else
		{
			connection.close();
			connectionExists = false;
			return null;
		}
	}
	
	public ResultSet select(String id) throws Exception
	{
		if(connectionExists == false) { connection = dbConnection(); }
		prepQuery = connection.prepareStatement("SELECT * FROM Manga WHERE " + "IDManga = ?");
		prepQuery.setString(1, id);
		prepQuery.execute();
		results = prepQuery.getResultSet();
		return results;
	}
	
	public void update(String id) throws Exception
	{
		if(connectionExists == false) { connection = dbConnection(); }
		prepQuery = connection.prepareStatement("UPDATE * FROM Manga WHERE " + "IDManga = ?");
		prepQuery.setString(1, id);
		prepQuery.execute();
		return;
	}
	
	public void insert(String id, String alias, String img, double ld, int status, String title, int hits, int chlen, int lang) throws Exception
	{
		if(connectionExists == false) { connection = dbConnection(); }
		prepQuery = connection.prepareStatement("INSERT INTO Manga" + "(IDManga, Alias, IMG, LastDate, Status, Title, Hits, Ch_Len, Lang) values (?,?,?,?,?,?,?,?,?)");
		prepQuery.setString(1, id);
		prepQuery.setString(2, alias);
		prepQuery.setString(3, img);
		prepQuery.setDouble(4, ld);
		prepQuery.setInt(5, status);
		prepQuery.setString(6, title);
		prepQuery.setInt(7, hits);
		prepQuery.setInt(8, chlen);
		prepQuery.setInt(9, lang);
		prepQuery.execute();
		return;
	}
	
	public void remove(String id) throws Exception
	{
		if(connectionExists == false) { connection = dbConnection(); }
		prepQuery = connection.prepareStatement("REMOVE * FROM Manga WHERE " + "IDManga = ?");
		prepQuery.setString(1, id);
		prepQuery.execute();
		return;
	}
}
